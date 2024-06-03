package bot.data;

public class Links {
    public static final String OKX_GET_ORDERS_URL = "https://www.okx.com/v3/c2c/tradingOrders/getMarketplaceAdsPrelogin?side=sell&paymentMethod=Wise&userType=all&hideOverseasVerificationAds=false&sortType=price_asc&limit=100&cryptoCurrency=%s&fiatCurrency=%s&currentPage=1&numberPerPage=1000&t=%s";
    public static final String BYBIT_GET_ORDERS_URL = "https://api2.bybit.com/fiat/otc/item/online";
    public static final String OKX_MERCHANT_URL = "https://www.okx.com/ru/p2p/ads-merchant?publicUserId=%s";
    public static final String BYBIT_MERCHANT_URL = "https://www.bybit.com/fiat/trade/otc/profile/%s/USDT/%s/item";
    public static final String HUOBI_GET_ORDERS_URL = "https://www.htx.com/-/x/otc/v1/data/trade-market?coinId=2&currency=%s&tradeType=sell&currPage=%s&payMethod=34&acceptOrder=0&country=&blockType=general&online=1&range=0&amount=&isThumbsUp=false&isMerchant=false&isTraded=false&onlyTradable=false&isFollowed=false&makerCompleteRate=0";
    public static final String HUOBI_MERCHANT_URL = "https://www.htx.com/ru-ru/fiat-crypto/trader/%s";
}

