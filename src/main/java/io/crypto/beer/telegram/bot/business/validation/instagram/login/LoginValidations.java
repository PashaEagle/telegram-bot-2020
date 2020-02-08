package io.crypto.beer.telegram.bot.business.validation.instagram.login;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.exception.ValidationErrorException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoginValidations {

    public static void validateName(Message m, ApplicationContext ctx) {

        System.out.println("Some empty validation for login name..");
    }

    public static void validatePassword(Message m, ApplicationContext ctx) {

        System.out.println("Some empty validation for login password..");
    }

    public static void validateFieldsAreSet(Message m, ApplicationContext ctx) {

        if (m.getSession().getInstagramSession().getAccountName() == null || m.getSession().getInstagramSession().getPassword() == null)
            throw new ValidationErrorException("Validation failed -> not all fields are set.");
    }
}
