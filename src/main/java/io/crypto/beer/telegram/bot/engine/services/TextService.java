package io.crypto.beer.telegram.bot.engine.services;

import static io.crypto.beer.telegram.bot.engine.utils.LocalizationService.getMessage;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.entity.Session;
import io.crypto.beer.telegram.bot.engine.entity.config.TextConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TextService {

    private static final String BASE_TEXT_PATH = "io.crypto.beer.telegram.bot.business.text.message.";

    @Resource
    private ApplicationContext ctx;
    private final MethodCallService methodCallService;

    public void generateMessageText(Message message) {
        Session session = message.getSession();
        TextConfig textConfig = session.getMessageConfig().getText();

        if (Objects.isNull(textConfig.getKey())) {
            String text = null;
            try {
                text = (String) methodCallService
                    .invokeMethod(null, buildFullTextPath(textConfig.getArgGenerationMethodPath()),
                                  new Class[] { Message.class, ApplicationContext.class }, message, ctx);
            } catch (InvocationTargetException e) {
                log.error("Error during getting text for user with chat id {}:{}",
                          session.getTelegramProfile().getTelegramId(), e.getMessage());
                throw new RuntimeException(String.format("Error during getting text for user with chat id %s:%s",
                                                         session.getTelegramProfile().getTelegramId(), e.getMessage()));
            }
            message.setText(text);
        } else {
            Object[] textArgs = null;
            if (Objects.nonNull(textConfig.getArgGenerationMethodPath())) {
                try {
                    textArgs = (Object[]) methodCallService
                        .invokeMethod(null, buildFullTextPath(textConfig.getArgGenerationMethodPath()),
                                      new Class[] { Session.class }, session);
                } catch (InvocationTargetException e) {
                    log.error("Error during getting args for text for user with chat id {}:{}",
                              session.getTelegramProfile().getTelegramId(), e.getMessage());
                    throw new RuntimeException(
                            String.format("Error during getting args for text for user with chat id %s:%s",
                                          session.getTelegramProfile().getTelegramId(), e.getMessage()));
                }
            }
            message.setText(getMessage(textConfig.getKey(), session.getLocale(), textArgs));
        }
        log.info("TextService: text was successfully populated for user {}",
                 session.getTelegramProfile().getUserName());
    }

    public static final String buildFullTextPath(String argGenerationMethodPath) {
        return BASE_TEXT_PATH + argGenerationMethodPath;
    }
}
