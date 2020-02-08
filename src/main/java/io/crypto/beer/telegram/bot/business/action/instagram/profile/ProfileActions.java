package io.crypto.beer.telegram.bot.business.action.instagram.profile;

import io.crypto.beer.telegram.bot.business.constant.KeyboardPath;
import io.crypto.beer.telegram.bot.business.instagram.entity.InstagramSession;
import io.crypto.beer.telegram.bot.business.text.message.instagram.login.LoginArgGenerator;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.services.ResourceHandlerService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramGetChallengeRequest;
import org.brunocvcunha.instagram4j.requests.InstagramResetChallengeRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSelectVerifyMethodRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSendSecurityCodeRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetChallengeResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramLoginResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSelectVerifyMethodResult;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Objects;

import static io.crypto.beer.telegram.bot.engine.entity.enums.ValidationKey.INSTAGRAM_NAME_EDITED;
import static io.crypto.beer.telegram.bot.engine.entity.enums.ValidationKey.INSTAGRAM_PASSWORD_EDITED;
import static io.crypto.beer.telegram.bot.engine.validation.ValidateConsumers.AFTER_ACTION_SUCCESS;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileActions {

    static Instagram4j instagram4j;

    public static void findUser(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method findUser");


    }

    public static void findPost(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method findPost");


    }

    public static void makePost(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method makePost");


    }
}
