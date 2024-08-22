package bot.data.exchangedata.binance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class TradeMethod {
    private String payId;
    private String payMethodId;
    private String payType;
    private String payAccount;
    private String payBank;
    private String paySubBank;
    private String identifier;
    private String iconUrlColor;
    private String tradeMethodName;
    private String tradeMethodShortName;
    private String tradeMethodBgColor;

    // Getters and Setters
}