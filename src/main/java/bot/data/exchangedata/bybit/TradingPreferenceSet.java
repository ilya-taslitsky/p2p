package bot.data.exchangedata.bybit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TradingPreferenceSet {
    private int hasRegisterTime; // 30+
    private int orderFinishNumberDay30; // 0
    private int hasCompleteRateDay30; // 0

    // Getters and setters
}

