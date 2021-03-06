package io.crypto.beer.telegram.bot.business.action.instagram.profile;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramFollowRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowersRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowingRequest;
import org.brunocvcunha.instagram4j.requests.InstagramLikeRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUnfollowRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUnlikeRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetCurrentUserProfileResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
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

    public static void seeProfile(Message m, ApplicationContext ctx) {
        log.info("Call FindUserViewActions method seeProfile");
        instagram4j = m.getSession().getInstagramSession().getInstagram4j();

        Integer index = m.getSession().getInstagramSession().getCurrentIndexInUserList();
        String username = m.getSession().getInstagramSession().getUserList().get(index).getUsername();
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

    public static void seePosts(Message m, ApplicationContext ctx) {
        log.info("Call FindUserViewActions method seePosts");
        instagram4j = m.getSession().getInstagramSession().getInstagram4j();

        InstagramUser user = m.getSession().getInstagramSession().getInstagramUser();

        try {
            InstagramFeedResult userPosts = instagram4j.sendRequest(new InstagramUserFeedRequest(user.getPk()));
            m.getSession().getInstagramSession().setPostList(userPosts.getItems());
            m.getSession().getInstagramSession().setCurrentIndexInPostList(0);
            m.getSession().getInstagramSession().setCurrentPost(userPosts.getItems().get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void seePostPrev(Message m, ApplicationContext ctx) {
        log.info("Call FindUserViewActions method seePostPrev");
        instagram4j = m.getSession().getInstagramSession().getInstagram4j();

        List<InstagramFeedItem> posts = m.getSession().getInstagramSession().getPostList();
        Integer index = m.getSession().getInstagramSession().getCurrentIndexInPostList();

        if (index == 0) { //if first in list
            index = posts.size() - 1;
        } else {
            index--;
        }

        m.getSession().getInstagramSession().setCurrentIndexInPostList(index);
        m.getSession().getInstagramSession().setCurrentPost(posts.get(index));
    }

    public static void seePostNext(Message m, ApplicationContext ctx) {
        log.info("Call FindUserViewActions method seePostNext");
        instagram4j = m.getSession().getInstagramSession().getInstagram4j();

        List<InstagramFeedItem> posts = m.getSession().getInstagramSession().getPostList();
        Integer index = m.getSession().getInstagramSession().getCurrentIndexInPostList();

        if (index == posts.size() - 1) { //if last in list
            index = 0;
        } else {
            index++;
        }

        m.getSession().getInstagramSession().setCurrentIndexInPostList(index);
        m.getSession().getInstagramSession().setCurrentPost(posts.get(index));
    }

    public static void likeAllPosts(Message m, ApplicationContext ctx) {
        log.info("Call FindUserViewActions method likeAllPosts");
        instagram4j = m.getSession().getInstagramSession().getInstagram4j();

        InstagramUser user = m.getSession().getInstagramSession().getInstagramUser();
        try {
            InstagramFeedResult userPosts = instagram4j.sendRequest(new InstagramUserFeedRequest(user.getPk()));
            if (m.getSession().getInstagramSession().isAllPostsLiked()){
                for (InstagramFeedItem post : userPosts.getItems()) {
                    instagram4j.sendRequest(new InstagramUnlikeRequest(post.getPk()));
                    m.getSession().getInstagramSession().setAllPostsLiked(false);
                }
            } else {
                for (InstagramFeedItem post : userPosts.getItems()) {
                    instagram4j.sendRequest(new InstagramLikeRequest(post.getPk()));
                    m.getSession().getInstagramSession().setAllPostsLiked(true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
