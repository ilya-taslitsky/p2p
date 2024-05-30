package bot.data;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class FilterRequest {
    private String tokenId;
    private List<String> payment;
    private String side;// Assuming side is an integer (1 for Buy, 0 for Sell)
    private int recentOrderNum; // < 100
    private Double maxAmount;
    private Double minAmount;
    private int hasRegisterTime; // 30+
    private int paymentsCount; // < 3
    private Double lastQuantity; // cколько осталось
}
