package bot.data.exchangedata.binance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataItem {
    private Adv adv;
    private Advertiser advertiser;

    // Getters and Setters
}