package bot.service.telegram;

import bot.data.telegram.UserState;
import bot.data.telegram.Constants;
import org.telegram.abilitybots.api.db.DBContext;
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


public class ResponseHandler {
    private static final String PASS = "fwefwe3423r23waefdwqw!c";
    private final SilentSender sender;
    private final Map<Long, UserState> chatStates;

    public ResponseHandler(SilentSender sender, DBContext db) {
        this.sender = sender;
        chatStates = db.getMap(Constants.CHAT_STATES);
    }

    public void replyToStart(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(Constants.START_TEXT);
        sender.execute(message);
        chatStates.put(chatId, UserState.NOT_AUTHENTICATED);
    }

    public void replyToPass(long chatId, Message message) {
        String password = message.getText();
        UserState userState = chatStates.get(chatId);
        if (userState != null && userState.equals(UserState.AUTHENTICATED)) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Жека, долбаеб, ты уже сука аунтефицированный, не пиши сюда нихуя!");
            sender.execute(sendMessage);
        } else if (password.equals(PASS)) {
            chatStates.put(chatId, UserState.AUTHENTICATED);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Your are authenticated.");
            sender.execute(sendMessage);
        } else {
            chatStates.put(chatId, UserState.NOT_AUTHENTICATED);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Your are not authenticated");
            sender.execute(sendMessage);
        }
    }
    public boolean sendMessage(String text) {
        AtomicBoolean isSent = new AtomicBoolean(true);
        chatStates.forEach((chatId, state) -> {
            if (state.equals(UserState.AUTHENTICATED)) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText(text);
                Optional<Message> execute = sender.execute(sendMessage);
                if (execute.isEmpty()) {
                    isSent.set(false);
                }
            }
        });
        return isSent.get();
    }

    private void promptWithKeyboardForState(long chatId, String text, ReplyKeyboard YesOrNo, UserState awaitingReorder) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(YesOrNo);
        sender.execute(sendMessage);
        chatStates.put(chatId, awaitingReorder);
    }

    private ReplyKeyboard getStartOrStop(){
        KeyboardRow row = new KeyboardRow();
        row.add("Start");
        row.add("Stop");
        return new ReplyKeyboardMarkup(List.of(row));
    }

    private ReplyKeyboard getStart(){
        KeyboardRow row = new KeyboardRow();
        row.add("Start");
        return new ReplyKeyboardMarkup(List.of(row));
    }

    private ReplyKeyboard getStop(){
        KeyboardRow row = new KeyboardRow();
        row.add("Stop");
        return new ReplyKeyboardMarkup(List.of(row));
    }

    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }


}