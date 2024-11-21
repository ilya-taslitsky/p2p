package bot.service.impl;

import bot.data.*;
import bot.data.exchangedata.okx.OkxRequest;
import bot.dao.OkxClient;
import bot.service.ExchangeService;
import bot.service.Mapper;
import com.google.common.collect.Multimap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "OKX")
@RequiredArgsConstructor
@Component
@Slf4j
public class OkxService implements ExchangeService {
    private String baseUrl = Links.OKX_GET_ORDERS_URL;
    private final OkxClient okxClient;
    private final Mapper mapper;

    public List<P2PResponse> processResponses(List<P2PResponse> items, Filter filter, Multimap<ExchangeEnum, String> userIdCache) {
        double lastQuantity = filter.getLastQuantity() == null ? 0 : filter.getLastQuantity();
        return items.stream()
                .filter(
                        item -> !userIdCache.containsEntry(ExchangeEnum.BYBIT, item.getUserId())
                        && item.getAuthStatus() == 2
                        && item.getPremium().equals("0")
                        && item.getCompleteOrderRate() == 0
                        && item.getPayments().size() <= filter.getPaymentsCount()
                        && item.getPayments().contains("Wise")
                        && item.getCompletedOrderQuantity() <= filter.getRecentOrderNum()
                        && item.getMaxCompletedOrderQuantity() == 0
                        && item.getLastQuantity() <= filter.getMaxAmount()
                        && item.getLastQuantity() >= lastQuantity)
                .toList();
    }

    @Override
    public Map<String, String> getAvailableOrderUrls(P2PRequest request, Filter filter, Multimap<ExchangeEnum, String> userIdCache, Multimap<ExchangeEnum, String> foundUserIds) {
        Map<String, String> foundOrderUrls = new HashMap<>();
        OkxRequest okxRequest = mapper.mapToOkxRequest(request);
        String urlWithParams = String.format(baseUrl, okxRequest.getCryptoCurrency(), okxRequest.getCurrency(), okxRequest.getTimestamp());
        List<P2PResponse> responses = new ArrayList<>(okxClient.findOrdersWithFilter(urlWithParams));
        List<P2PResponse> processResponses = processResponses(responses, filter, userIdCache);
        processResponses
                .forEach(item -> {
                    userIdCache.put(ExchangeEnum.OKX,item.getUserId());
                    foundUserIds.put(ExchangeEnum.OKX, item.getUserId());
                    foundOrderUrls.put(String.format(Links.OKX_MERCHANT_URL, item.getUserId()), item.getUserId());
                });
        return foundOrderUrls;
    }
}
