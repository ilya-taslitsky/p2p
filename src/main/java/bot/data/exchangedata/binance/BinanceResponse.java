package bot.data.exchangedata.binance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BinanceResponse {
    private List<DataItem> data;
    private boolean success;

    // Getters and Setters
}