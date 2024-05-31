package bot.service.telegram;

import bot.data.AppContext;
import bot.data.telegram.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.BiConsumer;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;


@Component("p2pBot")
public class P2PBot extends AbilityBot {
    private ResponseHandler responseHandler;
    private AppContext appContext;
    @Autowired
    public P2PBot(@Value("${telegram.bot.token}") String botToken, @Value("${telegram.bot.name}") String botName,
                 AppContext appContext, ResponseHandler responseHandler) {
        super(botToken, botName);
        this.appContext = appContext;
        this.responseHandler = responseHandler;
        appContext.setResponseHandler(responseHandler);
        responseHandler.setSender(silent);
        responseHandler.setChatStates(db.getMap(Constants.CHAT_STATES));
    }

    public Ability startBot() {
        return Ability
                .builder()
                .name("start")
                .info(Constants.START_DESCRIPTION)
                .locality(USER)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> responseHandler.replyToStart(ctx.chatId()))
                .build();
    }


    public Reply replyToButtons() {
        BiConsumer<BaseAbilityBot, Update> action = (abilityBot, upd) -> responseHandler.replyToMessage(getChatId(upd), upd.getMessage());
        return Reply.of(action, Flag.TEXT, upd -> responseHandler.userIsActive(getChatId(upd)));
    }

    @Override
    public long creatorId() {
        return 1L;
    }

}
