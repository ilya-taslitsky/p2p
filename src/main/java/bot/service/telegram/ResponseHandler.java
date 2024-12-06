package bot.service.telegram;

import bot.data.Exchange;
import bot.data.PaymentMethod;
import bot.data.telegram.UserState;
import bot.data.telegram.Constants;
import bot.service.ExchangeSubscriberService;
import bot.service.P2PScheduler;
import bot.service.impl.BinanceService;
import bot.service.impl.BybitService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


@Component
@Setter
@RequiredArgsConstructor
@Slf4j
public class ResponseHandler {
    @Value("${telegram.password.hash}")
    private String passwordHash;
    private SilentSender sender;
    private Map<Long, UserState> chatStates;
    private final P2PScheduler p2PScheduler;
    private final ExchangeSubscriberService exchangeSubscriberService;
    private final BinanceService binanceService;
    private final BybitService bybitService;



    public void replyToStart(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(Constants.START_TEXT);
        sender.execute(message);
        chatStates.put(chatId, UserState.NOT_AUTHENTICATED);
    }

    public void replyToMessage(long chatId, Message message) {
        String messageText = message.getText();
        UserState userState = chatStates.get(chatId);

        if (messageText.equals("Exit") && userState != UserState.NOT_AUTHENTICATED) {
            promptWithKeyboardForState(chatId, "Exited", getButtons());
            chatStates.put(chatId, UserState.AUTHENTICATED);
            return;
        }


        switch (userState) {
            case NOT_AUTHENTICATED -> {
                String userPassHash = getPasswordHash(messageText);
                if (passwordHash.equals(userPassHash)) {
                    promptWithKeyboardForState(chatId, "You are authenticated", getButtons());
                    chatStates.put(chatId, UserState.AUTHENTICATED);
                } else {
                    sendMessage(chatId, "You are not authenticated");
                }
            }
            case ADD_EXCHANGE -> {
                Exchange exchange = Exchange.fromString(messageText);
                exchangeSubscriberService.subscribe(exchange);
                promptWithKeyboardForState(chatId, "Exchange added", getButtons());
                chatStates.put(chatId, UserState.AUTHENTICATED);
            }
            case REMOVE_EXCHANGE -> {
                Exchange exchange = Exchange.fromString(messageText);
                exchangeSubscriberService.unsubscribe(exchange);
                promptWithKeyboardForState(chatId, "Exchange removed", getButtons());
                chatStates.put(chatId, UserState.AUTHENTICATED);

            }
            case SELECT_EXCHANGE_ADD_PAYMENT_METHOD -> {
                Exchange exchange = Exchange.fromString(messageText);
                promptWithKeyboardForState(chatId, "Select payment method to add", getPaymentMethods(true, exchange));
                chatStates.put(chatId, UserState.ADD_PAYMENT_METHOD);
            }
            case SELECT_EXCHANGE_REMOVE_PAYMENT_METHOD -> {
                Exchange exchange = Exchange.fromString(messageText);
                promptWithKeyboardForState(chatId, "Select payment method to remove", getPaymentMethods(false, exchange));
                chatStates.put(chatId, UserState.REMOVE_PAYMENT_METHOD);
            }
            case ADD_PAYMENT_METHOD -> {
                String[] split = messageText.split(":");
                Exchange exchange = Exchange.fromString(split[0]);
                PaymentMethod paymentMethod = PaymentMethod.fromString(split[1]);
                switch (exchange) {
                    case BINANCE -> {
                        binanceService.getPaymentMethods().add(paymentMethod);
                    }
                    case BYBIT -> {
                        bybitService.getPaymentMethods().add(paymentMethod);
                    }
                }
                promptWithKeyboardForState(chatId, "Payment method added", getButtons());
                chatStates.put(chatId, UserState.AUTHENTICATED);
            }
            case REMOVE_PAYMENT_METHOD -> {
                String[] split = messageText.split(":");
                Exchange exchange = Exchange.fromString(split[0]);
                PaymentMethod paymentMethod = PaymentMethod.fromString(split[1]);
                switch (exchange) {
                    case BINANCE -> {
                        binanceService.getPaymentMethods().remove(paymentMethod);
                    }
                    case BYBIT -> {
                        bybitService.getPaymentMethods().remove(paymentMethod);
                    }
                }
                promptWithKeyboardForState(chatId, "Payment method removed", getButtons());
                chatStates.put(chatId, UserState.AUTHENTICATED);
            }
            case AUTHENTICATED ->  {
                reactToButtons(chatId, messageText);
            }
        }
    }

    public boolean sendOrders(String text) {
        AtomicBoolean isSent = new AtomicBoolean(true);
        chatStates.forEach((chatId, state) -> {
            if (!state.equals(UserState.NOT_AUTHENTICATED)) {
                Optional<Message> execute = sendMessage(chatId, text);
                if (execute.isEmpty()) {
                    isSent.set(false);
                }
            }
        });
        return isSent.get();
    }

    private void reactToButtons(long chatId, String buttonText) {
        switch (buttonText) {
            case Constants.BOT_BOT_STATUS -> {
                String message = p2PScheduler.isStarted() ? "Bot is started" : "Bot is stopped";
                sendMessage(chatId, message + "\nStatus: " + p2PScheduler.getLastRequest() + "\n" + exchangeSubscriberService.getAllExchanges());
            }
            case Constants.START_BOT -> {
                p2PScheduler.start();
                sendMessage(chatId, "Bot started");
            }
            case Constants.STOP_BOT -> {
                p2PScheduler.stop();
                sendMessage(chatId, "Bot stopped");
            }
            case Constants.ADD_EXCHANGE -> {
                chatStates.put(chatId, UserState.ADD_EXCHANGE);
                promptWithKeyboardForState(chatId, "Select exchange to add", getExchanges(true));

            }
            case Constants.REMOVE_EXCHANGE -> {
                chatStates.put(chatId, UserState.REMOVE_EXCHANGE);
                promptWithKeyboardForState(chatId, "Select exchange to remove", getExchanges(false));
            }
            case Constants.SELECT_EXCHANGE_ADD_PAYMENT_METHOD -> {
                chatStates.put(chatId, UserState.SELECT_EXCHANGE_ADD_PAYMENT_METHOD);
                promptWithKeyboardForState(chatId, "Select exchange to add payment method", getExchanges(false));
            }
            case Constants.SELECT_EXCHANGE_REMOVE_PAYMENT_METHOD -> {
                chatStates.put(chatId, UserState.SELECT_EXCHANGE_REMOVE_PAYMENT_METHOD);
                promptWithKeyboardForState(chatId, "Select exchange to remove payment method", getExchanges(false));
            }
            default -> sendMessage(chatId, "Unknown command");
        }
    }

    private ReplyKeyboard getExchanges(boolean isAdd) {
        KeyboardRow row = new KeyboardRow();
        Collection<Exchange> selectedExchanges = exchangeSubscriberService.getAllExchanges();
        if (!isAdd) {
            selectedExchanges.stream().map(Enum::name).forEach(row::add);
        } else {
            // add all exchanges that are not in selectedExchanges
            for (Exchange exchange : Exchange.values()) {
                if (!selectedExchanges.contains(exchange)) {
                    row.add(exchange.name());
                }
            }
        }
        row.add("Exit");
        return new ReplyKeyboardMarkup(List.of(row));
    }

    private ReplyKeyboard getPaymentMethods(boolean isAdd, Exchange exchange) {
        List<KeyboardRow> rows = new ArrayList<>();
        Collection<PaymentMethod> selectedPaymentMethods = exchange.equals(Exchange.BINANCE) ? binanceService.getPaymentMethods() : bybitService.getPaymentMethods();
        if (!isAdd) {
            selectedPaymentMethods.stream().map(Enum::name).filter(str -> !str.contains("SEPA")).forEach(str ->{
                KeyboardRow row = new KeyboardRow();
                row.add(exchange + ":" + str);
                rows.add(row);
            });
        } else {
            // add all exchanges that are not in selectedExchanges
            if (exchange.equals(Exchange.BINANCE)) {
                for (PaymentMethod paymentMethod : PaymentMethod.values()) {
                    if (!selectedPaymentMethods.contains(paymentMethod) && !paymentMethod.name().contains("SEPA") && !paymentMethod.equals(PaymentMethod.WISE)) {
                        KeyboardRow row = new KeyboardRow();
                        row.add(exchange + ":" + paymentMethod.name());
                        rows.add(row);
                    }
                }
            } else {
                for (PaymentMethod paymentMethod : PaymentMethod.values()) {
                    if (!selectedPaymentMethods.contains(paymentMethod) && (paymentMethod.equals(PaymentMethod.WISE) || paymentMethod.equals(PaymentMethod.SkrillMoneybookers) || paymentMethod.equals(PaymentMethod.AirTM) || paymentMethod.equals(PaymentMethod.Zinli))) {
                        KeyboardRow row = new KeyboardRow();
                        row.add(exchange + ":" + paymentMethod.name());
                        rows.add(row);
                    }
                }
            }
        }
        KeyboardRow exitRow = new KeyboardRow();
        exitRow.add("Exit");

        rows.add(exitRow);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(rows);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        return keyboardMarkup;
    }

    private void promptWithKeyboardForState(long chatId, String text, ReplyKeyboard keyboard) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(keyboard);
        sender.execute(sendMessage);
    }

    private Optional<Message> sendMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sender.execute(sendMessage);
    }

    private ReplyKeyboard getButtons(){
        KeyboardRow row1 = new KeyboardRow();
        row1.add(Constants.BOT_BOT_STATUS);
        row1.add(Constants.START_BOT);
        row1.add(Constants.STOP_BOT);
        row1.add(Constants.ADD_EXCHANGE);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(Constants.REMOVE_EXCHANGE);
        row2.add(Constants.SELECT_EXCHANGE_ADD_PAYMENT_METHOD);
        row2.add(Constants.SELECT_EXCHANGE_REMOVE_PAYMENT_METHOD);

        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(rows);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        return keyboardMarkup;
    }


    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }

    private String getPasswordHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
           log.error("Failed to hash password" + e.getMessage());
        }
        return null;
    }

    public void removeChatState(long chatId) {
        chatStates.remove(chatId);
    }
}