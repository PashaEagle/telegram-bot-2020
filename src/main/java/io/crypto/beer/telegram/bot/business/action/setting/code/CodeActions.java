package io.crypto.beer.telegram.bot.business.action.setting.code;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.Locale;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CodeActions {

    public static void activateHiddenFeature(Message m, ApplicationContext ctx) {
        log.info("Call CodeActions method activateHiddenFeature");

    }
}
