package io.crypto.beer.telegram.bot.business.action.setting.language;

import java.util.Locale;

import org.springframework.context.ApplicationContext;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LanguageActions {

    public static void populateUkraine(Message m, ApplicationContext ctx) {
        log.info("Call LanguageActions method populateUkraine");
        m.getSession().setLocale(new Locale("ua"));
    }

    public static void populateEnglish(Message m, ApplicationContext ctx) {
        log.info("Call LanguageActions method populateEnglish");
        m.getSession().setLocale(new Locale("en"));
    }
}
