package io.crypto.beer.telegram.bot.engine.services;

import static io.crypto.beer.telegram.bot.engine.services.ResourceHandlerService.fillMessageConfig;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.entity.Session;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class BotOrchestration {

    private final TextService textService;
    private final KeyboardService keyboardService;
    private final ActionHandlerService actionHandlerService;
    private final MessageHandlerService messageHandlerService;

    @Value("${message.config.path}")
    private String messageConfigPath;
    private static final String START = "/start";

    private static final Predicate<String> TEXT_IS_START = text -> text.startsWith(START);
    private static final Predicate<String> IS_NOT_NULL_TEXT = text -> Optional.ofNullable(text).isPresent();

    public void adaptCallback(Update update, Session session) {

        boolean isKeyboardExpired = session.getLastMessageId()
            .compareTo(update.getCallbackQuery().getMessage().getMessageId()) > 0;
        if (isKeyboardExpired) {
            messageHandlerService.sendExpiredMessage(session.getLocale(), session.getTelegramProfile().getTelegramId(),
                                                     update.getCallbackQuery().getMessage().getMessageId());
            return;
        }

        // TODO if data is null what next? I think we should have some default
        // filling of MessageConfig

        setActiveButton(session);
        String keyboardName = session.getActiveButton().getNext();
        if (IS_NOT_NULL_TEXT.test(keyboardName)) {
            fillMessageConfig(session, String.format("%s%s", messageConfigPath, keyboardName));
        }

        List<Message> messages = actionHandlerService.generateCallbackMessage(update, session);
        adaptMessageConfig(messages);
    }

    public void adaptMessage(Update update, Session session) {

        // TODO if text is not valid what next? I think we should have some
        // default filling of MessageConfig
        if (IS_NOT_NULL_TEXT.and(TEXT_IS_START).test(session.getInputData())) {
            session.setActiveButton(null);
            fillMessageConfig(session, String.format("%sstart.json", messageConfigPath));
        }

        List<Message> messages = actionHandlerService.generateMessage(update, session);
        adaptMessageConfig(messages);
    }

    private void setActiveButton(Session session) {
        session.getCurrentButtons()
            .stream()
            .filter(b -> containsIgnoreCase(b.getId(), session.getCallbackData()))
            .findAny()
            .ifPresent(session::setActiveButton);
    }

    private void adaptMessageConfig(List<Message> messages) {
        messages.stream().forEach(m -> {
            textService.generateMessageText(m);
            keyboardService.generateKeyboard(m);
        });

        messageHandlerService.sendMessage(messages);
    }
}
