package io.crypto.beer.telegram.bot.business.action.setting.profile;

import static io.crypto.beer.telegram.bot.engine.entity.enums.ValidationKey.NAME_EDITED;
import static io.crypto.beer.telegram.bot.engine.entity.enums.ValidationKey.PHONE_EDITED;
import static io.crypto.beer.telegram.bot.engine.validation.ValidateConsumers.AFTER_ACTION_SUCCESS;

import org.springframework.context.ApplicationContext;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.entity.TelegramProfile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileActions {

    public static void editName(Message m, ApplicationContext ctx) {
        TelegramProfile profile = m.getSession().getTelegramProfile();
        profile.setFullName(m.getSession().getInputData());

        AFTER_ACTION_SUCCESS.accept(m, NAME_EDITED.getKey());
        log.info("Validation success -> name changed: %s", profile.getFullName());
    }

    public static void editPhone(Message m, ApplicationContext ctx) {
        TelegramProfile profile = m.getSession().getTelegramProfile();
        profile.setPhone(m.getSession().getInputData());

        AFTER_ACTION_SUCCESS.accept(m, PHONE_EDITED.getKey());
        log.info("Validation success -> phone changed: {}", profile.getPhone());
    }
}
