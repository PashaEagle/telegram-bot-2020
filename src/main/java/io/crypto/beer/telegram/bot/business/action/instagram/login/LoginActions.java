package io.crypto.beer.telegram.bot.business.action.instagram.login;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.payload.InstagramLoginResult;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Locale;

import static io.crypto.beer.telegram.bot.engine.entity.enums.ValidationKey.INSTAGRAM_NAME_EDITED;
import static io.crypto.beer.telegram.bot.engine.entity.enums.ValidationKey.INSTAGRAM_PASSWORD_EDITED;
import static io.crypto.beer.telegram.bot.engine.entity.enums.ValidationKey.NAME_EDITED;
import static io.crypto.beer.telegram.bot.engine.validation.ValidateConsumers.AFTER_ACTION_SUCCESS;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoginActions {

    public static void editName(Message m, ApplicationContext ctx) {
        log.info("Call LoginActions method editName");

        System.out.println(m.getSession().getInputData());
        AFTER_ACTION_SUCCESS.accept(m, INSTAGRAM_NAME_EDITED.getKey());
        log.info("Validation success -> name changed: {}", m.getSession().getInputData());
    }

    public static void editPassword(Message m, ApplicationContext ctx) {
        log.info("Call LoginActions method editPassword");

        System.out.println(m.getSession().getInputData());
        AFTER_ACTION_SUCCESS.accept(m, INSTAGRAM_PASSWORD_EDITED.getKey());
        log.info("Validation success -> password changed: {}", m.getSession().getInputData());
    }

    public static void authorize(Message m, ApplicationContext ctx) {
        log.info("Call LoginActions method login");
        Instagram4j instagram = Instagram4j.builder().username("pasha__eagle").password("iloveira").build();
        instagram.setup();
        try {
            InstagramLoginResult result = instagram.login();
            String confirmationUrl = result.getChallenge().getUrl();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
