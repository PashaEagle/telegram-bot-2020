package io.crypto.beer.telegram.bot.business.action.instagram.profile;

import io.crypto.beer.telegram.bot.business.constant.KeyboardPath;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.services.ResourceHandlerService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramFollowRequest;
import org.brunocvcunha.instagram4j.requests.InstagramLikeRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUnfollowRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUnlikeRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramLikeResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
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
            if (!m.getSession().getInstagramSession().isCurrentPostLiked()){
                instagram4j.sendRequest(new InstagramLikeRequest(post.getPk()));
                m.getSession().getInstagramSession().setCurrentPostLiked(true);
                int likes = m.getSession().getInstagramSession().getCurrentPost().getLike_count();
                m.getSession().getInstagramSession().getCurrentPost().setLike_count(likes + 1);
            }
            else {
                instagram4j.sendRequest(new InstagramUnlikeRequest(post.getPk()));
                m.getSession().getInstagramSession().setCurrentPostLiked(false);
                int likes = m.getSession().getInstagramSession().getCurrentPost().getLike_count();
                m.getSession().getInstagramSession().getCurrentPost().setLike_count(likes - 1);
            }
        } catch (IOException e) {
            log.info("Exception while liking post");
            e.printStackTrace();
        }

    }

    public static void seeAuthor(Message m, ApplicationContext ctx) {
        log.info("Call FindPostViewActions method seeAuthor");
        instagram4j = m.getSession().getInstagramSession().getInstagram4j();

        String username = m.getSession().getInstagramSession().getCurrentPost().getUser().getUsername();
        InstagramSearchUsernameResult userResult = null;
        try {
            userResult = instagram4j.sendRequest(new InstagramSearchUsernameRequest(username));
            if (userResult.getUser() == null) {
                System.out.println("User not found");
                m.getSession().getMessageConfig().getText().setKey("message.instagram.profile.find-user-not-found");
                m.getSession().getMessageConfig().getText().setArgGenerationMethodPath("instagram.profile" +
                        ".ProfileArgGenerator.getLastEnteredInput");
            } else {
                m.getSession().getInstagramSession().setInstagramUser(userResult.getUser());
            }
        } catch (Exception e) {
            System.out.println("Some exception when searching user.");
            e.printStackTrace();
        }
    }
}
