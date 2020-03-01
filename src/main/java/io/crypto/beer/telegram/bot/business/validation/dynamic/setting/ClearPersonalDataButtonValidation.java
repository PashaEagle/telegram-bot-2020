package io.crypto.beer.telegram.bot.business.validation.dynamic.setting;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowingRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramLoggedUser;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.List;

public class ClearPersonalDataButtonValidation {

    static Instagram4j instagram4j;

    public static boolean validate(Message m, ApplicationContext ctx) {

        return !m.getSession().getInstagramSession().isPersonalDataCleared();
    }
}
