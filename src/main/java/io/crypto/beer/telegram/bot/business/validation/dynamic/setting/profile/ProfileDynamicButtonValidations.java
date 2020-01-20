package io.crypto.beer.telegram.bot.business.validation.dynamic.setting.profile;

import java.util.Objects;

import org.springframework.context.ApplicationContext;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileDynamicButtonValidations {

    public static boolean validatePhoneButton(Message message, ApplicationContext ctx) {
        return Objects.isNull(message.getSession().getTelegramProfile().getPhone());
    }
}
