package bot.service.impl;

import bot.data.exchangedata.bybit.Item;
import bot.dao.BybitClient;
import bot.data.Filter;
import bot.data.Links;
import bot.data.P2PRequest;
import bot.data.P2PResponse;
import bot.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "BYBIT")
@Slf4j
@RequiredArgsConstructor
public class BybitServiceImpl implements ExchangeService {
    private final BybitClient bybitClient;

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
                        && item.getMaxAmount() <= filter.getMaxAmount()
                        && item.getLastQuantity() >= lastQuantity)
                .toList();
    }

    @Override
    public List<String> getAvailableOrderUrls(P2PRequest request, Filter filter, Set<String> userIdCache,  List<String> foundUserIds) {
        List<P2PResponse> responses = new ArrayList<>();
        List<String> foundOrderUrls = new ArrayList<>();
        List<Item> items;
        int page = 1;
        do {
            request.setPage(String.valueOf(page));
            items = bybitClient.findOrdersWithFilter(request);
            if(items == null) {
                log.warn("Failed to send request");
                continue;
            }
            responses.addAll(items);
            page++;
        } while (!Objects.requireNonNull(items).isEmpty());
        List<P2PResponse> processResponses = processResponses(responses, filter);

        processResponses.stream()
                .filter(item -> !userIdCache.contains(item.getUserId()))
                .forEach(item -> {
                    userIdCache.add(item.getUserId());
                    foundUserIds.add(item.getUserId());
                    foundOrderUrls.add(String.format(Links.BYBIT_MERCHANT_URL, item.getUserId(), request.getCurrencyId()));
                });
        return foundOrderUrls;
    }
}
