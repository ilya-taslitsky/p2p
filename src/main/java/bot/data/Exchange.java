package bot.data;

public enum Exchange {
    BYBIT,
    OKX,
    HUOBI;

    public static Exchange fromString(String exchange) {
        return Exchange.valueOf(exchange.toUpperCase());
    }
}
