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

@Service(value = "BINANCE")
@Slf4j
@RequiredArgsConstructor
public class BinanceService implements ExchangeService {
    private final BinanceClient binanceClient;
    private final ObjectMapper objectMapper;
    @Getter
    private List<PaymentMethod> paymentMethods = new ArrayList<>();
    @PostConstruct
    public void init() {
        paymentMethods.add(PaymentMethod.BANK);
    }
    public List<DataItem> processResponses(List<DataItem> items, Filter filter) {
        double lastQuantity = filter.getLastQuantity() == null ? 0 : filter.getLastQuantity();
        return items.stream()
                .filter(item -> {
                    boolean isOkBeforeOrdersCheck =
                            item.getAdv().getTradeMethods().size() <= filter.getPaymentsCount()
                            && item.getAdvertiser().getMonthOrderCount() <= filter.getRecentOrderNum()
                            && Double.parseDouble(item.getAdv().getTradableQuantity()) <= filter.getMaxAmount()
                            && Double.parseDouble(item.getAdv().getTradableQuantity()) >= lastQuantity;
                    if (!isOkBeforeOrdersCheck) {
                        return false;
                    }

                    String response = binanceClient.get(String.format(Links.BINANCE_USER_ORDERS_URL, item.getAdvertiser().getUserNo()));
                    if (response == null) {
                        return false;
                    }

                    try {
                        JsonNode rootNode = objectMapper.readTree(response);
                        return rootNode.at("/data/userDetailVo/userStatsRet/completedOrderNum").asInt() <= lastQuantity;
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
        if (request.getCurrencyId().equals("EUR")) {
            return foundOrderUrls;
        }
        // TODO: refactor this shit
        BinanceRequest binanceRequest = new BinanceRequest();
        List<String> payTypes = binanceRequest.getPayTypes();
        if (request.getCurrencyId().equals("USD") && paymentMethods.contains(PaymentMethod.Zelle)) {
            payTypes.add(PaymentMethod.Zelle.name());
        }
        if (paymentMethods.contains(PaymentMethod.BANK)) {
            payTypes.add(PaymentMethod.BANK.name());
        }

        binanceRequest.setFiat(request.getCurrencyId());
        binanceRequest.setAsset(request.getTokenId());

        List<DataItem> items;
        List<DataItem> foundOrders = new ArrayList<>();
        int page = 1;
        do {
           binanceRequest.setPage(page);
            items = binanceClient.findOrdersWithFilter(binanceRequest);
            if(items == null) {
                log.warn("Failed to send binance request");
                continue;
            }
            foundOrders.addAll(items);
            page++;
        } while (!Objects.requireNonNull(items).isEmpty());

        List<DataItem> processResponses = processResponses(foundOrders, filter);


        processResponses.stream()
                .filter(item -> !userIdCache.containsEntry(Exchange.BINANCE, item.getAdvertiser().getUserNo()))
                .forEach(item -> {
                    userIdCache.put(Exchange.BINANCE,  item.getAdvertiser().getUserNo());
                    foundUserIds.put(Exchange.BINANCE,  item.getAdvertiser().getUserNo());
                    foundOrderUrls.put(String.format(Links.BINANCE_MERCHANT_URL,  item.getAdvertiser().getUserNo()), item.getAdvertiser().getUserNo());
                });
        return foundOrderUrls;
    }


}
