package bot.data.exchangedata.bybit;

import bot.data.P2PResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
public class Item implements P2PResponse  {
    private String userId; //
    private String tokenId; // USDT
    private String currencyId; // USD/EUR
    private String price; // price
    private String premium; // should be zero
    private String lastQuantity; // сколько осталось
    private String remark; // !contains scam
    private List<String> payments; // 78
    private int recentOrderNum; // < 100
    private TradingPreferenceSet tradingPreferenceSet;
    private String maxAmount;
    private String minAmount;
    private int authStatus; // should be 2
    // Getters and setters


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return item.userId.equals(userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getCurrency() {
        return currencyId;
    }

    @Override
    public String getCryptoCurrency() {
        return tokenId;
    }

    @Override
    public String getSide() {
        return null;
    }

    @Override
    public Double getPrice() {
        return Double.parseDouble(price);
    }

    @Override
    public String getPremium() {
        return premium;
    }

    @Override
    public int getCompleteOrderRate() {
        return tradingPreferenceSet.getHasCompleteRateDay30();
    }

    @Override
    public Double getLastQuantity() {
        return Double.parseDouble(lastQuantity);
    }

    @Override
    public List<String> getPayments() {
        List<String> payments = new ArrayList<>(this.payments);
        for (int i = 0; i < payments.size(); i++) {
            if ("78".equals(payments.get(i))) {
                payments.set(i, "Wise");
            }
        }
        return payments;
    }

    @Override
    public int getMaxCompletedOrderQuantity() {
        if (tradingPreferenceSet == null)
            return 0;
        return tradingPreferenceSet.getOrderFinishNumberDay30();
    }

    @Override
    public int getCompletedOrderQuantity() {
        return recentOrderNum;
    }

    @Override
    public Double getMaxAmount() {
       return maxAmount == null ? 0 : Double.parseDouble(maxAmount);
    }

    @Override
    public int getAuthStatus() {
        return authStatus;
    }
}

