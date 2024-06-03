package bot.data.exchangedata.huobi;


import bot.data.P2PResponse;
import lombok.Setter;

import java.util.List;

@Setter
 public class Data implements P2PResponse {
    private long uid;
    private int merchantLevel; // should be 0
    private String maxTradeLimit;
    private String tradeCount;
    private List<PayMethod> payMethods;
    private int totalTradeOrderCount; // must be less than 100

   @Override
   public String getUserId() {
      return String.valueOf(uid);
   }

   @Override
   public String getCurrency() {
      return null;
   }

   @Override
   public String getCryptoCurrency() {
      return null;
   }

   @Override
   public String getSide() {
      return null;
   }

   @Override
   public Double getPrice() {
      return null;
   }

   @Override
   public String getPremium() {
      return null;
   }

   @Override
   public int getCompleteOrderRate() {
      return 0;
   }

   @Override
   public Double getLastQuantity() {
      return Double.valueOf(tradeCount);
   }

   @Override
   public List<String> getPayments() {
      return payMethods.stream().map(PayMethod::getName).toList();
   }

   @Override
   public int getMaxCompletedOrderQuantity() {
      return 0;
   }

   @Override
   public int getCompletedOrderQuantity() {
      return totalTradeOrderCount;
   }

   @Override
   public Double getMaxAmount() {
      return Double.valueOf(maxTradeLimit);
   }

   @Override
   public int getAuthStatus() {
      return merchantLevel;
   }

   // Getters and setters
}
