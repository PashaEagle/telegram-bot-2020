package io.crypto.beer.telegram.bot.business.validation.dynamic.instagram;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowingRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.List;

public class UserProfileButtonsValidation {

    public static boolean isAccountPublic(Message m, ApplicationContext ctx) {

        Instagram4j instagram4j = m.getSession().getInstagramSession().getInstagram4j();
        InstagramUser instagramUser = m.getSession().getInstagramSession().getInstagramUser();

        if (!instagramUser.is_private()) {
            return true;
        } else {
            boolean followingHim = false;
            try {
                InstagramGetUserFollowersResult myFollowings =
                        instagram4j.sendRequest(new InstagramGetUserFollowingRequest(
                                m.getSession().getInstagramSession().getInstagramLoginResult().getLogged_in_user().getPk()));
                List<InstagramUserSummary> users = myFollowings.getUsers();
                for (InstagramUserSummary user : users) {
                    System.out.println("I follow " + user.getUsername());
                    if (user.getUsername().equals(instagramUser.getUsername())) {
                        followingHim = true;
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return followingHim;
        }
    }
}
