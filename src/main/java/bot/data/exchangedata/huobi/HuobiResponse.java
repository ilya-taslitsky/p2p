package bot.data.exchangedata.huobi;

import java.util.List;

@lombok.Data
public class HuobiResponse {
    private List<Data> data;
    private boolean success;

    // Getters and setters
}