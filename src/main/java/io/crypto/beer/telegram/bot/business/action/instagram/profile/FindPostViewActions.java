package io.crypto.beer.telegram.bot.business.action.instagram.profile;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramFollowRequest;
import org.brunocvcunha.instagram4j.requests.InstagramLikeRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUnfollowRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramLikeResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FindPostViewActions {

    static Instagram4j instagram4j;

    public static void likePost(Message m, ApplicationContext ctx) {
        log.info("Call FindPostViewActions method likePost");
        instagram4j = m.getSession().getInstagramSession().getInstagram4j();

        InstagramFeedItem post = m.getSession().getInstagramSession().getCurrentPost();
        try {
            instagram4j.sendRequest(new InstagramLikeRequest(post.getPk()));
        } catch (IOException e) {
            log.info("Exception while liking post");
            e.printStackTrace();
        }

    }

    public static void seeFollowers(Message m, ApplicationContext ctx) {
        log.info("Call FindUserViewActions method seeFollowers");
        instagram4j = m.getSession().getInstagramSession().getInstagram4j();
    }
}
