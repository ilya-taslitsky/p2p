package bot.data;

public class Links {
    public static final String OKX_GET_ORDERS_URL = "https://www.okx.com/v3/c2c/tradingOrders/getMarketplaceAdsPrelogin?side=sell&paymentMethod=Wise&userType=all&hideOverseasVerificationAds=false&sortType=price_asc&limit=100&cryptoCurrency=%s&fiatCurrency=%s&currentPage=1&numberPerPage=1000&t=%s";
    public static final String BYBIT_GET_ORDERS_URL = "https://api2.bybit.com/fiat/otc/item/online";
    public static final String OKX_MERCHANT_URL = "https://www.okx.com/ru/p2p/ads-merchant?publicUserId=%s";
    public static final String BYBIT_MERCHANT_URL = "https://www.bybit.com/fiat/trade/otc/profile/%s/USDT/%s/item";
}
