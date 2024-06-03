package bot.data;


public enum Currency {
    AUD(7),
    CAD(6),
    CZK(37),
    NZD(24),
    NOK(27),
    SEK(29),
    PLN(43),
    GBP(12),
    USD(2),
    EUR(14);
    private final int huobiValue;

    Currency(int huobiValue) {
        this.huobiValue = huobiValue;
    }

    public int getHuobiValue() {
        return huobiValue;
    }

    public static Currency fromString(String currency) {
        for (Currency c : Currency.values()) {
            if (c.name().equalsIgnoreCase(currency)) {
                return c;
            }
        }
        return null;
    }
}
