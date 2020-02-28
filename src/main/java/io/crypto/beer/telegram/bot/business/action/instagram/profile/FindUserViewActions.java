package io.crypto.beer.telegram.bot.business.action.instagram.profile;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramFollowRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowersRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowingRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUnfollowRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FindUserViewActions {

    static Instagram4j instagram4j;

    public static void followUser(Message m, ApplicationContext ctx) {
        log.info("Call FindUserViewActions method followUser");
        instagram4j = m.getSession().getInstagramSession().getInstagram4j();

        InstagramUser user = m.getSession().getInstagramSession().getInstagramUser();

        try {
            if (!m.getSession().getInstagramSession().isCurrentUserFollowed()) {
                //make follow
                instagram4j.sendRequest(new InstagramFollowRequest(user.getPk()));
            } else {
                //make unfollow
                instagram4j.sendRequest(new InstagramUnfollowRequest(user.getPk()));
            }
        } catch (IOException e) {
            log.info("Error occurred when trying to follow/unfollow {}", user.getUsername());
            e.printStackTrace();
        }
    }

    public static void seeFollowers(Message m, ApplicationContext ctx) {
        log.info("Call FindUserViewActions method seeFollowers");

        instagram4j = m.getSession().getInstagramSession().getInstagram4j();
        InstagramUser user = m.getSession().getInstagramSession().getInstagramUser();
        InstagramGetUserFollowersResult userFollowers = null;
        try {
            userFollowers = instagram4j.sendRequest(new InstagramGetUserFollowersRequest(user.getPk()));

            List<InstagramUserSummary> users = userFollowers.getUsers();
            m.getSession().getInstagramSession().setUserList(users);
            m.getSession().getInstagramSession().setCurrentIndexInUserList(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void seeFollowersNext(Message m, ApplicationContext ctx) {
        log.info("Call FindUserViewActions method seeFollowersNext");

        List<InstagramUserSummary> users = m.getSession().getInstagramSession().getUserList();
        Integer index = m.getSession().getInstagramSession().getCurrentIndexInUserList();

        if (index == users.size() - 1) { //if last in list
            index = 0;
        } else {
            index++;
        }

        m.getSession().getInstagramSession().setCurrentIndexInUserList(index);
    }

    public static void seeFollowersPrev(Message m, ApplicationContext ctx) {
        log.info("Call FindUserViewActions method seeFollowersPrev");

        List<InstagramUserSummary> users = m.getSession().getInstagramSession().getUserList();
        Integer index = m.getSession().getInstagramSession().getCurrentIndexInUserList();

        if (index == 0) { //if first in list
            index = users.size() - 1;
        } else {
            index--;
        }

        m.getSession().getInstagramSession().setCurrentIndexInUserList(index);
    }

    public static void seeFollowings(Message m, ApplicationContext ctx) {
        log.info("Call FindUserViewActions method seeFollowings");

        instagram4j = m.getSession().getInstagramSession().getInstagram4j();
        InstagramUser user = m.getSession().getInstagramSession().getInstagramUser();
        InstagramGetUserFollowersResult userFollowings = null;
        try {
            userFollowings = instagram4j.sendRequest(new InstagramGetUserFollowingRequest(user.getPk()));

            List<InstagramUserSummary> users = userFollowings.getUsers();
            m.getSession().getInstagramSession().setUserList(users);
            m.getSession().getInstagramSession().setCurrentIndexInUserList(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
