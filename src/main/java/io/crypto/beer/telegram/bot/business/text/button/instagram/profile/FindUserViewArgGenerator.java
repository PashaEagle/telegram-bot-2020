package io.crypto.beer.telegram.bot.business.text.button.instagram.profile;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.utils.LocalizationService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowingRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FindUserViewArgGenerator {


    public static String getFollowButtonText(Message m, ApplicationContext ctx) {
        log.info("Call FindUserViewArgGenerator method getFollowButtonText");

        String follow = LocalizationService.getMessage("keyboard.instagram.profile.find-user.follow",
                m.getSession().getLocale());
        String unfollow = LocalizationService.getMessage("keyboard.instagram.profile.find-user.unfollow",
                m.getSession().getLocale());
        String requested = LocalizationService.getMessage("keyboard.instagram.profile.find-user.requested",
                m.getSession().getLocale());

        Instagram4j instagram4j = m.getSession().getInstagramSession().getInstagram4j();

        InstagramUser instagramUser = m.getSession().getInstagramSession().getInstagramUser();
        InstagramGetUserFollowersResult myFollowings = null;
        String result;
        try {
            myFollowings = instagram4j.sendRequest(new InstagramGetUserFollowingRequest(
                    m.getSession().getInstagramSession().getInstagramLoginResult().getLogged_in_user().getPk()));
            List<InstagramUserSummary> users = myFollowings.getUsers();
            boolean followingHim = false;
            for (InstagramUserSummary user : users) {
                System.out.println("I follow " + user.getUsername());
                if (user.getUsername().equals(instagramUser.getUsername())) {
                    followingHim = true;
                    break;
                }
            }

            if (followingHim) {
                result = unfollow;
                m.getSession().getInstagramSession().setCurrentUserFollowed(true);
            } else {
                if (instagramUser.is_private) {
                    if (!m.getSession().getInstagramSession().isCurrentUserFollowed()) {
                        result = requested;
                        m.getSession().getInstagramSession().setCurrentUserFollowed(true);
                    } else {
                        result = follow;
                        m.getSession().getInstagramSession().setCurrentUserFollowed(false);
                    }
                } else {
                    result = follow;
                    m.getSession().getInstagramSession().setCurrentUserFollowed(false);
                }
            }

        } catch (IOException e) {
            System.out.println("Some error occurred while getting or processing all followings" + instagramUser.getUsername());
            e.printStackTrace();
            result = "❔ Unknown";
            m.getSession().getInstagramSession().setCurrentUserFollowed(false);
        }

        return result;
    }
}
