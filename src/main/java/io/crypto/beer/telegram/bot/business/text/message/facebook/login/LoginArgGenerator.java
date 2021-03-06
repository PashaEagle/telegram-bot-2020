package io.crypto.beer.telegram.bot.business.text.message.facebook.login;

import io.crypto.beer.telegram.bot.engine.entity.Session;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoginArgGenerator {

    public static String ERROR_MESSAGE = "No error :\\";

    public static Object[] getArgs(Session session) {
        log.info("Call LoginArgGenerator method getArgs");
        return new Object[]{
            session.getFacebookSession().getLoginUrl()
        };
    }

}