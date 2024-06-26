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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service(value = "OKX")
@RequiredArgsConstructor
@Component
@Slf4j
public class OkxService implements ExchangeService {
    private String baseUrl = Links.OKX_GET_ORDERS_URL;
    private final OkxClient okxClient;
    private final Mapper mapper;

    @Override
    public List<P2PResponse> processResponses(List<P2PResponse> items, Filter filter) {
        double lastQuantity = filter.getLastQuantity() == null ? 0 : filter.getLastQuantity();
        return items.stream()
                .filter(item -> item.getAuthStatus() == 2
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
    public List<String> getAvailableOrderUrls(P2PRequest request, Filter filter, Multimap<Exchange, String> userIdCache, Multimap<Exchange, String> foundUserIds) {
        List<String> foundOrderUrls = new ArrayList<>();
        OkxRequest okxRequest = mapper.mapToOkxRequest(request);
        String urlWithParams = String.format(baseUrl, okxRequest.getCryptoCurrency(), okxRequest.getCurrency(), okxRequest.getTimestamp());
        List<P2PResponse> responses = new ArrayList<>(okxClient.findOrdersWithFilter(urlWithParams));
        List<P2PResponse> processResponses = processResponses(responses, filter);
        processResponses.stream()
                .filter(item -> !userIdCache.containsEntry(Exchange.OKX, item.getUserId()))
                .forEach(item -> {
                    userIdCache.put(Exchange.OKX,item.getUserId());
                    foundUserIds.put(Exchange.OKX, item.getUserId());
                    foundOrderUrls.add(String.format(Links.OKX_MERCHANT_URL, item.getUserId()));
                });
        return foundOrderUrls;
    }
}
