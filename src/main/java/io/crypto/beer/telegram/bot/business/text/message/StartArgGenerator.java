package io.crypto.beer.telegram.bot.business.text.message;

import io.crypto.beer.telegram.bot.engine.entity.Session;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StartArgGenerator {

    public static Object[] getArgs(Session session) {
        log.info("Call StartArgGenerator method getArgs");
        return new Object[] { session.getTelegramProfile().getFullName() };
    }
}