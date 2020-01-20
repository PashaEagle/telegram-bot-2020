package io.crypto.beer.telegram.bot.business.text.message.setting.profile;

import io.crypto.beer.telegram.bot.engine.entity.TelegramProfile;
import io.crypto.beer.telegram.bot.engine.entity.Session;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileArgGenerator {

    public static Object[] getArgs(Session session) {
        TelegramProfile profile = session.getTelegramProfile();
        log.info("Call ProfileArgGenerator method getArgs");
        return new Object[] { profile.getFullName(), profile.getPhone() };
    }

    public static Object[] getName(Session session) {
        log.info("Call ProfileArgGenerator method getName");
        return new Object[] { session.getTelegramProfile().getFullName() };
    }

    public static Object[] getPhone(Session session) {
        log.info("Call ProfileArgGenerator method getPhone");
        return new Object[] { session.getTelegramProfile().getPhone() };
    }
}
