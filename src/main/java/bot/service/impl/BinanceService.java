package bot.service.impl;

import bot.dao.BinanceClient;
import bot.data.*;
import bot.data.exchangedata.binance.BinanceRequest;
import bot.data.exchangedata.binance.DataItem;
import bot.service.ExchangeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service(value = "BINANCE")
@Slf4j
@RequiredArgsConstructor
public class BinanceService implements ExchangeService {
    private final BinanceClient binanceClient;
    private final ObjectMapper objectMapper;
    @Getter
    private List<PaymentMethod> paymentMethods = new ArrayList<>();
    private final List<String> fiats = List.of("BTC", "USDT", "USDC");
    private Pattern wisePattern = Pattern.compile("(?i)w[\\W_]*i[\\W_]*s[\\W_]*e(?![\\W_]*other)");
    private Pattern revoPattern = Pattern.compile("(?i)r[\\W_]*e[\\W_]*v[\\W_]*o([\\W_]*l[\\W_]*u[\\W_]*t[\\W_]*e)?");
    private String wiseSiren = "\uD83D\uDEA8 WISE \uD83D\uDEA8 \n";
    private String revoSiren = "\uD83D\uDEA8 REVOLUT \uD83D\uDEA8 \n";

    @PostConstruct
    public void init() {
        paymentMethods.add(PaymentMethod.BANK);
    }
    private List<DataItem> processResponses(List<DataItem> items, Filter filter, Multimap<Exchange, String> userIdCache) {
        double lastQuantity = filter.getLastQuantity() == null ? 0 : filter.getLastQuantity();
        return items.stream()
                .filter(item -> {
                    if (userIdCache.containsEntry(Exchange.BINANCE, item.getAdvertiser().getUserNo())) {
                        return false;
                    }
                    boolean isOkBeforeOrdersCheck =
                    item.getAdv().getTradeMethods().size() <= filter.getPaymentsCount()
                            && item.getAdvertiser().getMonthOrderCount() <= filter.getRecentOrderNum()
                            && Double.parseDouble(item.getAdv().getTradableQuantity()) <= filter.getMaxAmount()
                            && Double.parseDouble(item.getAdv().getTradableQuantity()) >= lastQuantity;
                    if (!isOkBeforeOrdersCheck) {
                        return false;
                    }

                    String response = binanceClient.get(item.getAdvertiser().getUserNo());
                    if (response == null) {
                        return false;
                    }

                    try {
                        JsonNode rootNode = objectMapper.readTree(response);
                        return rootNode.at("/data/userDetailVo/userStatsRet/completedOrderNum").asInt() <= filter.getRecentOrderNum();
                    } catch (JsonProcessingException e) {
                        log.warn("Failed to parse response", e);
                        return false;
                    }
                })
                .toList();


    }

    @Override
    public Map<String, String> getAvailableOrderUrls(P2PRequest request, Filter filter, Multimap<Exchange, String> userIdCache, Multimap<Exchange, String> foundUserIds) {
        Map<String, String> foundOrderUrls = new HashMap<>();
        // TODO: refactor this shit
        BinanceRequest binanceRequest = new BinanceRequest();
        List<String> payTypes = binanceRequest.getPayTypes();
        if (request.getCurrencyId().equals("USD") && paymentMethods.contains(PaymentMethod.Zelle)) {
            payTypes.add(PaymentMethod.Zelle.name());
        }
        if (paymentMethods.contains(PaymentMethod.BANK)) {
            payTypes.add(PaymentMethod.BANK.name());
        }
        if (request.getCurrencyId().equals("EUR")) {
            payTypes.add(PaymentMethod.SEPAinstant.name());
            payTypes.add(PaymentMethod.SEPA.name());
            if (paymentMethods.contains(PaymentMethod.ZEN)) {
                payTypes.add(PaymentMethod.ZEN.name());
            }
        }
        if (paymentMethods.contains(PaymentMethod.SkrillMoneybookers)) {
            payTypes.add(PaymentMethod.SkrillMoneybookers.name());
        }

        binanceRequest.setFiat(request.getCurrencyId());
        for (String fiat : fiats) {
            binanceRequest.setAsset(fiat);
            List<DataItem> items;
            List<DataItem> foundOrders = new ArrayList<>();
            int page = 1;
            do {
                binanceRequest.setPage(page);
                items = binanceClient.findOrdersWithFilter(binanceRequest);
                if (items == null) {
                    log.warn("Failed to send binance request");
                    continue;
                }
                foundOrders.addAll(items);
                page++;
            } while (!Objects.requireNonNull(items).isEmpty());

            List<DataItem> processResponses = processResponses(foundOrders, filter, userIdCache);


            processResponses
                    .forEach(item -> {
                        userIdCache.put(Exchange.BINANCE, item.getAdvertiser().getUserNo());
                        foundUserIds.put(Exchange.BINANCE, item.getAdvertiser().getUserNo());
                        Matcher wiseMatcher = wisePattern.matcher(item.getAdvertiser().getNickName());
                        Matcher revoMatcher = revoPattern.matcher(item.getAdvertiser().getNickName());
                        boolean isWiseFound = false;
                        boolean isRevoFound = false;
                        if (wiseMatcher.find()) {
                            isWiseFound = true;
                        } else if(revoMatcher.find()) {
                            isRevoFound = true;
                        } else {
                            String userDetails = binanceClient.getUserDetails(item.getAdv().getAdvNo());
                            try {
                                JsonNode rootNode = objectMapper.readTree(userDetails);
                                String remarks = rootNode.at("/data/remarks").asText();
                                wiseMatcher = wisePattern.matcher(remarks);
                                revoMatcher = revoPattern.matcher(remarks);
                                if (wiseMatcher.find()) {
                                    isWiseFound = true;
                                } else if(revoMatcher.find()) {
                                    isRevoFound = true;
                                }
                            } catch (JsonProcessingException e) {
                               log.warn("Failed to parse order remarks response", e);
                            }
                        }
                        String url = String.format(Links.BINANCE_MERCHANT_URL, item.getAdvertiser().getUserNo());
                        if (isWiseFound) {
                            url = wiseSiren + url;
                        }
                        if (isRevoFound) {
                            url = revoSiren + url;
                        }
                        foundOrderUrls.put(url, item.getAdvertiser().getUserNo());
                    });
        }

        return foundOrderUrls;
    }


}
