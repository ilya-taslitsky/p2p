package bot.service.impl;

import bot.dao.BinanceClient;
import bot.data.*;
import bot.data.exchangedata.binance.BinanceRequest;
import bot.data.exchangedata.binance.DataItem;
import bot.service.ExchangeService;
import com.google.common.collect.Multimap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "BINANCE")
@Slf4j
@RequiredArgsConstructor
public class BinanceService implements ExchangeService {
    private final BinanceClient binanceClient;

    public List<DataItem> processResponses(List<DataItem> items, Filter filter) {
        double lastQuantity = filter.getLastQuantity() == null ? 0 : filter.getLastQuantity();
        return items.stream()
                .filter(item -> item.getAdv().getTradeMethods().size() <= filter.getPaymentsCount()
                        && item.getAdvertiser().getMonthOrderCount() <= filter.getRecentOrderNum()
                        && Double.parseDouble(item.getAdv().getTradableQuantity()) <= filter.getMaxAmount()
                        && Double.parseDouble(item.getAdv().getTradableQuantity()) >= lastQuantity)
                .toList();
    }

    @Override
    public Map<String, String> getAvailableOrderUrls(P2PRequest request, Filter filter, Multimap<Exchange, String> userIdCache, Multimap<Exchange, String> foundUserIds) {
        Map<String, String> foundOrderUrls = new HashMap<>();
        if (!request.getCurrencyId().equals("USD")) {
            return foundOrderUrls;
        }
        BinanceRequest binanceRequest = new BinanceRequest();
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