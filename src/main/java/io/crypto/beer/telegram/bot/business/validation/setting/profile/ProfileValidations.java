package io.crypto.beer.telegram.bot.business.validation.setting.profile;

import static io.crypto.beer.telegram.bot.engine.validation.ValidateConsumers.IS_NAME;
import static io.crypto.beer.telegram.bot.engine.validation.ValidateConsumers.IS_PHONE;

import org.springframework.context.ApplicationContext;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.exception.ValidationErrorException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileValidations {

    public static void validateName(Message m, ApplicationContext ctx) {
        if (!IS_NAME.test(m.getSession().getInputData())) {
            throw new ValidationErrorException(String.format("Validation failed -> name didn't change: %s",
                                                             m.getSession().getTelegramProfile().getFullName()));
        }
    }

    public static void validatePhone(Message m, ApplicationContext ctx) {
        if (!IS_PHONE.test(m.getSession().getInputData())) {
            throw new ValidationErrorException(String.format("Validation failed -> phone didn't change: %s",
                                                             m.getSession().getTelegramProfile().getPhone()));
        }
    }
}
