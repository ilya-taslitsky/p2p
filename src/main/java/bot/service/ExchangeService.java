package bot.service;

import bot.data.P2PResponse;
import bot.data.Filter;

import java.util.List;

public abstract class ExchangeService {
    protected List<P2PResponse> processResponses(List<P2PResponse> items, Filter filter) {
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
}
