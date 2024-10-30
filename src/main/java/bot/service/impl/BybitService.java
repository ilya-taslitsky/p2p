package bot.service.impl;

import bot.data.*;
import bot.data.exchangedata.bybit.Item;
import bot.dao.BybitClient;
import bot.service.ExchangeService;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service(value = "BYBIT")
@Slf4j
@RequiredArgsConstructor
public class BybitService implements ExchangeService {
    private final BybitClient bybitClient;
    @Getter
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    @PostConstruct
    public void init() {
        paymentMethods.add(PaymentMethod.WISE);
    }

    private List<P2PResponse> processResponses(List<P2PResponse> items, Filter filter, Multimap<Exchange, String> userIdCache) {
        double lastQuantity = filter.getLastQuantity() == null ? 0 : filter.getLastQuantity();
        return items.stream()
                .filter(
                        item -> !userIdCache.containsEntry(Exchange.BYBIT, item.getUserId())
                        && item.getAuthStatus() == 2
                        && item.getCompleteOrderRate() == 0
                        && item.getPayments().size() <= filter.getPaymentsCount()
                        && item.getCompletedOrderQuantity() <= filter.getRecentOrderNum()
                        && item.getMaxCompletedOrderQuantity() == 0
                        && item.getLastQuantity() <= filter.getMaxAmount()
                        && item.getLastQuantity() >= lastQuantity)
                .toList();
    }

    @Override
    public Map<String, String> getAvailableOrderUrls(P2PRequest request, Filter filter, Multimap<Exchange, String> userIdCache,  Multimap<Exchange, String> foundUserIds) {
        List<P2PResponse> responses = new ArrayList<>();
        Map<String, String> foundOrderUrls = new HashMap<>();
        request.setPayment(paymentMethods.stream().map(PaymentMethod::getBybitValue).toList());
        List<Item> items;
        int page = 1;
        do {
            request.setPage(String.valueOf(page));
            items = bybitClient.findOrdersWithFilter(request);
            if(items == null) {
                log.warn("Failed to send bybit request");
                continue;
            }
            responses.addAll(items);
            page++;
        } while (!Objects.requireNonNull(items).isEmpty());
        List<P2PResponse> processResponses = processResponses(responses, filter, userIdCache);

        processResponses
                .forEach(item -> {
                    userIdCache.put(Exchange.BYBIT, item.getUserId());
                    foundUserIds.put(Exchange.BYBIT, item.getUserId());
                    foundOrderUrls.put(String.format(Links.BYBIT_MERCHANT_URL, item.getUserId(), request.getCurrencyId()), item.getUserId());
                });
        return foundOrderUrls;
    }
}
