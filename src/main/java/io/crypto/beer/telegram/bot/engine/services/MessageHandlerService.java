package io.crypto.beer.telegram.bot.engine.services;

import static io.crypto.beer.telegram.bot.engine.utils.Transformer.TO_EDIT_MESSAGE_TEXT;
import static io.crypto.beer.telegram.bot.engine.utils.Transformer.TO_EDIT_MESSAGE_TEXT_WITH_EXPIRED_KEYBOARD;
import static io.crypto.beer.telegram.bot.engine.utils.Transformer.TO_SEND_MESSAGE_WITH_INLINE;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import io.crypto.beer.telegram.bot.engine.Bot;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageHandlerService {

    private final Bot bot;

    public void sendMessage(List<Message> messages) {
        messages.stream().forEach(m -> {
            try {
                switch (m.getModifier()) {
                    case CREATE:
                        m.getSession()
                            .setLastMessageId(bot.execute(TO_SEND_MESSAGE_WITH_INLINE.apply(m)).getMessageId());
                        break;
                    case EDIT:
                    default:
                        bot.execute(TO_EDIT_MESSAGE_TEXT.apply(m));
                        break;
                }
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }

    public void sendExpiredMessage(Locale locale, Long profileId, Integer messageId) {
        try {
            bot.execute(TO_EDIT_MESSAGE_TEXT_WITH_EXPIRED_KEYBOARD.apply(locale, profileId, messageId));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
