package io.crypto.beer.telegram.bot.business.text.message.instagram.login;

import io.crypto.beer.telegram.bot.business.instagram.entity.InstagramSession;
import io.crypto.beer.telegram.bot.engine.entity.Session;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoginArgGenerator {

    public static Object[] getArgs(Session session) {
        log.info("Call LoginArgGenerator method getArgs");
        return new Object[] { session.getInstagramSession().getAccountName(), session.getInstagramSession().getPassword()};
    }

    public static Object[] getConfirmationLink(Session session) {
        log.info("Call LoginArgGenerator method getConfirmationLink");
        return new Object[] { session.getInstagramSession().getConfirmationUrl() };
    }
}