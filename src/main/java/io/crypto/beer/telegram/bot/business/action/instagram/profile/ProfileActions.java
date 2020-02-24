package io.crypto.beer.telegram.bot.business.action.instagram.profile;

import io.crypto.beer.telegram.bot.business.constant.KeyboardPath;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.services.ResourceHandlerService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramTagFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileActions {

    static Instagram4j instagram4j;

    public static void findUser(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method findUser");

        String username = m.getSession().getInputData();
        instagram4j = m.getSession().getInstagramSession().getInstagram4j();

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
                ResourceHandlerService.fillMessageConfig(m.getSession(), String.format("%s%s",
                        KeyboardPath.BASE_PATH.getPath(),
                        KeyboardPath.FIND_USER_VIEW.getPath()));
            }
        } catch (Exception e) {
            System.out.println("Some exception when searching user.");
            e.printStackTrace();
        }
    }

    public static void findPost(Message m, ApplicationContext ctx) throws IOException {
        log.info("Call ProfileActions method findPost");

        String hashtag = m.getSession().getInputData();
        instagram4j = m.getSession().getInstagramSession().getInstagram4j();

        InstagramFeedResult tagFeed = instagram4j.sendRequest(new InstagramTagFeedRequest(hashtag));
        for (InstagramFeedItem feedResult : tagFeed.getItems()) {
            System.out.println("Post ID: " + feedResult.getPk());
        }

        m.getSession().getInstagramSession().setCurrentPost(tagFeed.getItems().get(0));
        ResourceHandlerService.fillMessageConfig(m.getSession(), String.format("%s%s",
                KeyboardPath.BASE_PATH.getPath(),
                KeyboardPath.FIND_POST_VIEW.getPath()));
    }

    public static void makePost(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method makePost");


    }
}
