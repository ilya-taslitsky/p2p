package bot.data;

import java.util.List;

public interface P2PResponse {
    String getUserId();

    String getCurrency();

    String getCryptoCurrency();

    String getSide();

    Double getPrice();

    String getPremium();
    int getCompleteOrderRate();

    Double getLastQuantity(); // more than 100 usdt

    List<String> getPayments();

    int getMaxCompletedOrderQuantity();

    int getCompletedOrderQuantity();

    int getAuthStatus();
}
