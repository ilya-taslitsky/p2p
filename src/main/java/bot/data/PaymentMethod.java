package bot.data;

public enum PaymentMethod {
    BANK("bank"), Zelle("Zelle"), SEPA("Sepa"), SEPAinstant("SepaInstant"), ZEN("Zen"), SkrillMoneybooker("162"), WISE("78");
    private final String bybitValue;

    PaymentMethod(String bybitValue) {
        this.bybitValue = bybitValue;
    }

    public String getBybitValue() {
        return bybitValue;
    }

    public static PaymentMethod fromString(String paymentMethod) {
        return PaymentMethod.valueOf(paymentMethod);
    }
}
