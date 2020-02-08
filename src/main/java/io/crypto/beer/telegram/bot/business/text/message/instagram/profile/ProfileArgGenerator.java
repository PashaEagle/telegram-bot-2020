package io.crypto.beer.telegram.bot.business.text.message.instagram.profile;

import io.crypto.beer.telegram.bot.engine.entity.Session;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileArgGenerator {

    public static Object[] getMainPageArgs(Session session) {
        log.info("Call ProfileArgGenerator method getMainPageArgs");
        return new Object[]{
                session.getInstagramSession().getInstagramLoginResult().getLogged_in_user().getUsername(),
                session.getInstagramSession().getInstagramLoginResult().getLogged_in_user().getProfile_pic_url(),
                session.getInstagramSession().getInstagramLoginResult().getLogged_in_user().getFull_name()
        };
    }
}
