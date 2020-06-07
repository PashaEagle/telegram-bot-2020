package io.crypto.beer.telegram.bot.business.text.message.facebook.menu;

import io.crypto.beer.telegram.bot.engine.entity.Session;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MenuArgGenerator {

    public static Object[] getMainPageArgs(Session session) {
        log.info("Call MenuArgGenerator method getMainPageArgs");
        return new Object[]{
                session.getFacebookSession().getLoggedUser().getName()
        };
    }

}
