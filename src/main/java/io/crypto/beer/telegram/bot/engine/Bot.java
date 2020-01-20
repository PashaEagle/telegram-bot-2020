package io.crypto.beer.telegram.bot.engine;

import static org.telegram.abilitybots.api.objects.Flag.CALLBACK_QUERY;
import static org.telegram.abilitybots.api.objects.Flag.MESSAGE;

import java.util.function.Consumer;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.objects.Update;

import io.crypto.beer.telegram.bot.engine.config.EngineProperties;
import io.crypto.beer.telegram.bot.engine.entity.Session;
import io.crypto.beer.telegram.bot.engine.services.BotOrchestration;
import io.crypto.beer.telegram.bot.engine.services.SessionService;

@Component
public class Bot extends AbilityBot {

    private final EngineProperties properties;
    private final BotOrchestration botOrchestration;
    private final SessionService sessionService;

    @Resource
    private ApplicationContext ctx;

    @Lazy
    @Autowired
    public Bot(EngineProperties properties, BotOrchestration botOrchestration, SessionService sessionService) {
        super(properties.getToken(), properties.getUsername());
        this.properties = properties;
        this.botOrchestration = botOrchestration;
        this.sessionService = sessionService;
    }

    public Reply replyToMessage() {
        Consumer<Update> consumer = u -> {
            Session session = sessionService.fetchSession(u, u.getMessage().getChatId(), u.getMessage().getText());
            botOrchestration.adaptMessage(u, session);
            sessionService.update(session, u.getMessage().getChatId());
        };
        return Reply.of(consumer, MESSAGE);
    }

    public Reply replyToCallBack() {
        Consumer<Update> consumer = u -> {
            Session session = sessionService.fetchSession(u, u.getCallbackQuery().getMessage().getChatId(),
                                                          u.getCallbackQuery().getData());
            botOrchestration.adaptCallback(u, session);
            sessionService.update(session, u.getCallbackQuery().getMessage().getChatId());
        };
        return Reply.of(consumer, CALLBACK_QUERY);
    }

    @Override
    public int creatorId() {
        return properties.getCreator().getId();
    }

}