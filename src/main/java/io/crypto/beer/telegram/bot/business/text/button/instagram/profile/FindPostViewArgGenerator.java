package io.crypto.beer.telegram.bot.business.text.button.instagram.profile;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowingRequest;
import org.brunocvcunha.instagram4j.requests.InstagramTagFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FindPostViewArgGenerator {

    static Instagram4j instagram4j;

    public static String getLikeButtonText(Message m, ApplicationContext ctx) {
        log.info("Call FindPostViewArgGenerator method getLikeButtonText");

        instagram4j = m.getSession().getInstagramSession().getInstagram4j();

        InstagramFeedItem post = m.getSession().getInstagramSession().getCurrentPost();
        boolean liked = m.getSession().getInstagramSession().isCurrentPostLiked();

        if (!liked){
            return "❤️ " + post.getLike_count();
        } else {
            return "\uD83D\uDC94️ " + post.getLike_count();
        }
    }
}
