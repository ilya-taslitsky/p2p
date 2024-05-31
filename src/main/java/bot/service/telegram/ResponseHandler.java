package bot.service.telegram;

import bot.data.telegram.UserState;
import bot.data.telegram.Constants;
import bot.service.ClientService;
import bot.service.P2PScheduler;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


@Component
@Setter
@RequiredArgsConstructor
public class ResponseHandler {
    private String passwordHash = "fwefwe3423r23waefdwqw!c";
    private SilentSender sender;
    private Map<Long, UserState> chatStates;
    private final ClientService clientService;
    private final P2PScheduler p2PScheduler;

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
        switch (userState) {
            case DELETE_ID -> {
                if (clientService.deleteById(messageText)) {
                    promptWithKeyboardForState(chatId, "Deleted", getButtons());
                } else {
                    promptWithKeyboardForState(chatId, "ID not found", getButtons());
                }
                chatStates.put(chatId, UserState.AUTHENTICATED);
            }
            case NOT_AUTHENTICATED -> {
                if (messageText.equals(passwordHash)) {
                    promptWithKeyboardForState(chatId, "You are authenticated", getButtons());
                    chatStates.put(chatId, UserState.AUTHENTICATED);
                } else {
                    sendMessage(chatId, "You are not authenticated");
                }
            }
            case AUTHENTICATED -> {
                reactToButtons(chatId, messageText);
            }
        }
    }

    public boolean sendOrders(String text) {
        AtomicBoolean isSent = new AtomicBoolean(true);
        chatStates.forEach((chatId, state) -> {
            if (state.equals(UserState.AUTHENTICATED)) {
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
            case Constants.DELETE_BY_ID -> {
                sendMessage(chatId, "Enter id to delete");
                chatStates.put(chatId, UserState.DELETE_ID);
            }
            case Constants.BOT_BOT_STATUS -> {
                String message = p2PScheduler.isStarted() ? "Bot is started" : "Bot is stopped";
                sendMessage(chatId, message + "\nStatus: " + p2PScheduler.getLastRequest());
            }
            default -> sendMessage(chatId, "Unknown command");
        }
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
        KeyboardRow row = new KeyboardRow();
        row.add(Constants.DELETE_BY_ID);
        row.add(Constants.BOT_BOT_STATUS);
        return new ReplyKeyboardMarkup(List.of(row));
    }


    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }
}