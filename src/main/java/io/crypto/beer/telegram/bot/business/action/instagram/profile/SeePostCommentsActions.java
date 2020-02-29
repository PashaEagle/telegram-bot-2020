package io.crypto.beer.telegram.bot.business.action.instagram.profile;

import io.crypto.beer.telegram.bot.engine.entity.Button;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramLikeRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUnlikeRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SeePostCommentsActions {

    static Instagram4j instagram4j;

    public static void comment(Message m, ApplicationContext ctx) {
        log.info("Call SeePostCommentsActions method setBackButtonForUserPosts");
        instagram4j = m.getSession().getInstagramSession().getInstagram4j();

    }
}