package io.crypto.beer.telegram.bot.business.action.instagram.profile;

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

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SeeUserPostsActions {

    static Instagram4j instagram4j;

    public static void likePost(Message m, ApplicationContext ctx) {
        log.info("Call SeeUserPostsActions method likePost");
        instagram4j = m.getSession().getInstagramSession().getInstagram4j();

        Integer index = m.getSession().getInstagramSession().getCurrentIndexInPostList();
        InstagramFeedItem post = m.getSession().getInstagramSession().getPostList().get(index);

        try {
            if (!post.isHas_liked()) {
                instagram4j.sendRequest(new InstagramLikeRequest(post.getPk()));
                post.setHas_liked(true);
                post.setLike_count(post.getLike_count() + 1);
            } else {
                instagram4j.sendRequest(new InstagramUnlikeRequest(post.getPk()));
                post.setHas_liked(false);
                post.setLike_count(post.getLike_count() - 1);
            }
            m.getSession().getInstagramSession().getPostList().set(index, post);
        } catch (IOException e) {
            log.info("Exception while liking post");
            e.printStackTrace();
        }
    }
}