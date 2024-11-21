package bot.service.impl;

import bot.dao.HuobiClient;
import bot.data.*;
import bot.data.CurrencyEnum;
import bot.data.exchangedata.huobi.Data;
import bot.service.ExchangeService;
import com.google.common.collect.Multimap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "HUOBI")
@RequiredArgsConstructor
@Slf4j
public class HuobiService implements ExchangeService {
    private final HuobiClient huobiClient;
    private String baseUrl = Links.HUOBI_GET_ORDERS_URL;

    public List<P2PResponse> processResponses(List<P2PResponse> items, Filter filter) {
        double lastQuantity = filter.getLastQuantity() == null ? 0 : filter.getLastQuantity();
        return items.stream()
                .filter(item -> item.getAuthStatus() == 0
                        && item.getPayments().size() <= filter.getPaymentsCount()
                        && item.getCompletedOrderQuantity() <= filter.getRecentOrderNum()
                        && item.getLastQuantity() <= filter.getMaxAmount()
                        && item.getLastQuantity() >= lastQuantity)
                .toList();
    }

    @Override
    public Map<String, String> getAvailableOrderUrls(P2PRequest request, Filter filter, Multimap<ExchangeEnum, String> userIdCache, Multimap<ExchangeEnum, String> foundUserIds) {
        List<P2PResponse> responses = new ArrayList<>();
        Map<String, String> foundOrderUrls = new HashMap<>();
        List<Data> items;
        int page = 1;
        do {
            int huobiValue = CurrencyEnum.fromString(request.getCurrencyId()).getHuobiValue();
            String urlWithParams = String.format(baseUrl, huobiValue, page);
            items = huobiClient.findOrdersWithFilter(urlWithParams);
            if(items == null) {
                log.warn("Failed to send request");
                continue;
            }
            responses.addAll(items);
            page++;
        } while (!Objects.requireNonNull(items).isEmpty());
        List<P2PResponse> processResponses = processResponses(responses, filter);

        processResponses.stream()
                .filter(item -> !userIdCache.containsEntry(ExchangeEnum.HUOBI, item.getUserId()))
                .forEach(item -> {
                    userIdCache.put(ExchangeEnum.HUOBI, item.getUserId());
                    foundUserIds.put(ExchangeEnum.HUOBI, item.getUserId());
                    foundOrderUrls.put(String.format(Links.HUOBI_MERCHANT_URL, item.getUserId()), item.getUserId());
                });
        return foundOrderUrls;
    }
}
