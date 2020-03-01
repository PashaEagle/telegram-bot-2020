package io.crypto.beer.telegram.bot.business.text.button.instagram.profile;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.springframework.context.ApplicationContext;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HiddenFeaturesArgGenerator {

    static Instagram4j instagram4j;

    public static String getLikeAllButtonText(Message m, ApplicationContext ctx) {
        log.info("Call SeeUserPostsArgGenerator method getLikeButtonText");

        instagram4j = m.getSession().getInstagramSession().getInstagram4j();

        if (m.getSession().getInstagramSession().isAllPostsLiked())
            return "\uD83D\uDC94 Unlike all posts";
        else
            return "\uD83D\uDC93 Like all posts";

    }
}
