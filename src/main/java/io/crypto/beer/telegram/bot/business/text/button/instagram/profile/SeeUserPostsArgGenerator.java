package io.crypto.beer.telegram.bot.business.text.button.instagram.profile;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.springframework.context.ApplicationContext;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SeeUserPostsArgGenerator {

    static Instagram4j instagram4j;

    public static String getLikeButtonText(Message m, ApplicationContext ctx) {
        log.info("Call SeeUserPostsArgGenerator method getLikeButtonText");

        instagram4j = m.getSession().getInstagramSession().getInstagram4j();

        Integer index = m.getSession().getInstagramSession().getCurrentIndexInPostList();
        InstagramFeedItem post = m.getSession().getInstagramSession().getPostList().get(index);

        if (!post.isHas_liked())
            return "❤️ " + post.getLike_count();
        else
            return "\uD83D\uDC94️ " + post.getLike_count();
    }
}
