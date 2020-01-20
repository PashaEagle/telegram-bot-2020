package io.crypto.beer.telegram.bot.engine.services;

import static io.crypto.beer.telegram.bot.engine.utils.LocalizationService.getMessage;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import io.crypto.beer.telegram.bot.engine.entity.Button;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.entity.Session;
import io.crypto.beer.telegram.bot.engine.entity.config.ButtonConfig;
import io.crypto.beer.telegram.bot.engine.entity.config.LabelConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class KeyboardService {

    private static final String BASE_DYNAMIC_VALIDAION_PATH = "io.crypto.beer.telegram.bot.business.validation.dynamic.";
    private static final String BASE_BUTTON_TEXT_GENERATOR_METHOD_PATH = "io.crypto.beer.telegram.bot.business.text.button.";

    @Resource
    private ApplicationContext ctx;
    private final MethodCallService methodCallService;
    private final PaginationService paginationService;

    public void generateKeyboard(Message message) {

        paginationService.preparePagination(message);

        Session session = message.getSession();
        List<List<ButtonConfig>> callbacks = session.getMessageConfig().getCallbacks();

        message.setCallbacks(callbacks.stream()
            .map(row -> row.stream()
                .filter(buttonConf -> checkDynamicCondition(buttonConf.getDynamicValidationMethodPath(), message))
                .map(buttonConf -> Button.builder()
                    .key(extractTextFromLabelConfig(buttonConf.getLabel(), message))
                    .url(buttonConf.getUrl())
                    .action(buttonConf.getAction())
                    .id(buttonConf.getId())
                    .next(buttonConf.getNext())
                    .inputDataValidator(buttonConf.getInputDataValidationMethodPath())
                    .build())
                .collect(Collectors.toList()))
            .filter(row -> !row.isEmpty())
            .collect(Collectors.toList()));
        fillSessionWithInfo(session, message.getCallbacks());

        log.info("KeyboardService: keyboard was successfully created for user {}",
                 session.getTelegramProfile().getUserName());
    }

    private String extractTextFromLabelConfig(LabelConfig labelConfig, Message message) {
        if (Objects.nonNull(labelConfig.getText())) {
            return labelConfig.getText();
        } else if (Objects.nonNull(labelConfig.getKey())) {
            return getMessage(labelConfig.getKey(), message.getSession().getLocale());
        } else {
            return getButtonTextFromMethod(labelConfig.getMethodPath(), message);
        }
    }

    private void fillSessionWithInfo(Session session, List<List<Button>> keyboard) {
        session.setCurrentButtons(keyboard.stream().flatMap(List::stream).collect(Collectors.toSet()));
    }

    private boolean checkDynamicCondition(String dynamicValidationMethodPath, Message message) {
        if (Objects.isNull(dynamicValidationMethodPath)) {
            return true;
        }
        try {
            return (boolean) methodCallService
                .invokeMethod(null, String.format("%s%s", BASE_DYNAMIC_VALIDAION_PATH, dynamicValidationMethodPath),
                              new Class[] { Message.class, ApplicationContext.class }, message, ctx);
        } catch (InvocationTargetException e) {
            log.error("Exception during checking dynamic condition from method {}:{}", dynamicValidationMethodPath,
                      e.getMessage());
            return false;
        }
    }

    private String getButtonTextFromMethod(String buttonTextGeneratorMethodPath, Message message) {
        try {
            return (String) methodCallService.invokeMethod(null,
                                                           String.format("%s%s", BASE_BUTTON_TEXT_GENERATOR_METHOD_PATH,
                                                                         buttonTextGeneratorMethodPath),
                                                           new Class[] { Message.class, ApplicationContext.class },
                                                           message, ctx);
        } catch (InvocationTargetException e) {
            log.error("Exception during getting button text from method {}:{}", buttonTextGeneratorMethodPath,
                      e.getMessage());
            return "Default value";
        }
    }
}