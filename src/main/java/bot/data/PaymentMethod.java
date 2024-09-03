package bot.data;

public enum PaymentMethod {
    BANK, Zelle;
    public static PaymentMethod fromString(String paymentMethod) {
        return PaymentMethod.valueOf(paymentMethod);
    }
}
