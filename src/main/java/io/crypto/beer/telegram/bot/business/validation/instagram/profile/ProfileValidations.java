package io.crypto.beer.telegram.bot.business.validation.instagram.profile;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.exception.ValidationErrorException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileValidations {

    public static void validateUsername(Message m, ApplicationContext ctx) {

        System.out.println("Some empty validation for username..");
    }

    public static void validatePostHashTag(Message m, ApplicationContext ctx) {

        System.out.println("Some empty validation for hashtag..");
    }

    public static void validateComment(Message m, ApplicationContext ctx) {

        System.out.println("Some empty validation for comment..");
    }
}
