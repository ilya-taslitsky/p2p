package bot.data;

public enum PaymentMethod {
    BANK, Zelle, SEPA, SEPAinstant, ZEN;
    public static PaymentMethod fromString(String paymentMethod) {
        return PaymentMethod.valueOf(paymentMethod);
    }
}
