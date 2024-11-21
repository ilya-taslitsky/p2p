package bot.data;

public enum PaymentMethodEnum {
    BANK("bank"), Zelle("Zelle"), SEPA("Sepa"), SEPAinstant("SepaInstant"), ZEN("Zen"), SkrillMoneybookers("162"), WISE("78");
    private final String bybitValue;

    PaymentMethodEnum(String bybitValue) {
        this.bybitValue = bybitValue;
    }

    public String getBybitValue() {
        return bybitValue;
    }

    public static PaymentMethodEnum fromString(String paymentMethod) {
        return PaymentMethodEnum.valueOf(paymentMethod);
    }
}
