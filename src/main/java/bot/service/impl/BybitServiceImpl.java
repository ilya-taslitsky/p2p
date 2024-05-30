package bot.service.impl;

import bot.data.exchangedata.bybit.Item;
import bot.service.BybitService;
import bot.dao.BybitClient;
import bot.data.Filter;
import bot.data.Links;
import bot.data.P2PRequest;
import bot.data.P2PResponse;
import bot.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class BybitServiceImpl extends ExchangeService implements BybitService {
    private String baseUrl = Links.BYBIT_GET_ORDERS_URL;
    private final BybitClient bybitClient;

    @Override
    public List<String> getAvailableOrderUrls(P2PRequest request, Filter filter, Set<String> foundUserIds) {
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
                .filter(item -> !foundUserIds.contains(item.getUserId()))
                .forEach(item -> {
                    foundUserIds.add(item.getUserId());
                    foundOrderUrls.add(String.format(Links.BYBIT_MERCHANT_URL, item.getUserId(), request.getCurrencyId()));
                });
        return foundOrderUrls;
    }
}
