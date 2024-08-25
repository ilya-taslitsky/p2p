package bot.data.exchangedata.binance;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class BinanceRequest {
    private String fiat;
    private int page;
    private int rows = 10;
    private String tradeType = "BUY";
    private String asset;
    private List<String> countries = new ArrayList<>();
    private boolean proMerchantAds = false;
    private boolean shieldMerchantAds = false;
    private String filterType;
    private List<String> periods = new ArrayList<>();
    private int additionalKycVerifyFilter = 0;
    private String publisherType;
    private List<String> payTypes = new ArrayList<>();
    private List<String> classifies = List.of("mass");
}
