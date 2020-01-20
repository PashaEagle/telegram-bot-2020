package io.crypto.beer.telegram.bot.engine.services;

import static io.crypto.beer.telegram.bot.engine.entity.enums.Modifier.CREATE;
import static io.crypto.beer.telegram.bot.engine.entity.enums.Modifier.EDIT;
import static io.crypto.beer.telegram.bot.engine.utils.Transformer.TO_MESSAGE;
import static io.crypto.beer.telegram.bot.engine.validation.ValidateConsumers.AFTER_ACTION_FAILURE;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.entity.Session;
import io.crypto.beer.telegram.bot.engine.entity.enums.Modifier;
import io.crypto.beer.telegram.bot.engine.exception.ValidationErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ActionHandlerService {

    @Resource
    private ApplicationContext ctx;
    private final MethodCallService methodCallService;

    private static final String BASE_ACTION_PATH = "io.crypto.beer.telegram.bot.business.action.";
    private static final String BASE_VALIDAION_PATH = "io.crypto.beer.telegram.bot.business.validation.";

    public List<Message> generateMessage(Update update, Session session) {
        return generateMessages(update, session, CREATE);
    }

    public List<Message> generateCallbackMessage(Update update, Session session) {
        return generateMessages(update, session, EDIT);
    }

    private List<Message> generateMessages(Update update, Session session, Modifier modifier) {
        List<Message> messages = new ArrayList<>();

        Message message = TO_MESSAGE.apply(update, session);
        message.setModifier(modifier);
        messages.add(message);
        log.info("ActionHandlerService: message was built for user {}", session.getTelegramProfile().getUserName());
        processMethod(message);

        return messages;
    }

    private void processMethod(Message message) {
        Optional.ofNullable(message.getSession().getActiveButton()).ifPresent(b -> {
            try {
                if (message.getModifier().equals(CREATE)) {
                    Optional.ofNullable(b.getInputDataValidator())
                        .ifPresent(v -> invokeMethod(message, BASE_VALIDAION_PATH, v));
                    Optional.ofNullable(b.getAction()).ifPresent(a -> invokeMethod(message, BASE_ACTION_PATH, a));
                } else if (Objects.isNull(b.getInputDataValidator())) {
                    Optional.ofNullable(b.getAction()).ifPresent(a -> invokeMethod(message, BASE_ACTION_PATH, a));
                }
            } catch (ValidationErrorException e) {
                AFTER_ACTION_FAILURE.accept(message, buildValidationErrorMessage(b.getId().toLowerCase()));
            }
        });
    }

    private void invokeMethod(Message message, String basePath, String method) {
        try {
            methodCallService.invokeMethod(null, String.format("%s%s", basePath, method),
                                           new Class[] { Message.class, ApplicationContext.class }, message, ctx);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof ValidationErrorException) {
                log.error("Error during method invocation in action handler service: {}", e.getCause().getMessage());
                throw (ValidationErrorException) e.getCause();
            }
            log.error("Error during method invocation in action handler service: {}", e.getMessage());
        }
    }

    private String buildValidationErrorMessage(String key) {
        String errorKey = key.substring(key.lastIndexOf('/') + 1, key.length());
        return String.format("error.validation.%s", errorKey);
    }
}
