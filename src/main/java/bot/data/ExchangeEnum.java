package bot.data;

public enum ExchangeEnum {
    BYBIT,
    OKX,
    HUOBI,
    BINANCE;

    public static ExchangeEnum fromString(String exchange) {
        return ExchangeEnum.valueOf(exchange.toUpperCase());
    }
}
