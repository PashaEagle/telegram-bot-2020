package io.crypto.beer.telegram.bot.business.action.facebook.profile;

import com.restfb.Connection;
import com.restfb.Parameter;
import com.restfb.types.Page;
import com.restfb.types.Photo;
import com.restfb.types.Post;
import com.restfb.types.User;
import io.crypto.beer.telegram.bot.business.action.facebook.configuration.FacebookConfig;
import io.crypto.beer.telegram.bot.business.instagram.entity.FacebookSession;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileActions {

    public static void myProfile(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method myProfile");

        FacebookSession facebookSession = m.getSession().getFacebookSession();

        facebookSession.setCurrentUser(facebookSession.getLoggedUser());
        facebookSession.setCurrentUserId(facebookSession.getLoggedUserId());
        facebookSession.setCurrentUserPictureUrl(facebookSession.getLoggedUserPictureUrl());

        m.getSession().setFacebookSession(facebookSession);
    }

    public static void getDetails(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method getDetails");
        if (m.getSession().getFacebookSession().getCurrentUser() == null) {
            System.out.println("FATAL ERROR FATAL ERROR CURRENT USER CANNOT BE NULL!");
            throw new RuntimeException("FATAL ERROR FATAL ERROR CURRENT USER CANNOT BE NULL!");
        }
    }

    public static void getPhotos(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method getPhotos");
        if (m.getSession().getFacebookSession().getCurrentUser() == null) {
            System.out.println("FATAL ERROR FATAL ERROR CURRENT USER CANNOT BE NULL!");
            throw new RuntimeException("FATAL ERROR FATAL ERROR CURRENT USER CANNOT BE NULL!");
        }
        Connection<Photo> photos =
                FacebookConfig.userFacebookClient.fetchConnection(m.getSession().getFacebookSession().getCurrentUserId() + "/photos/uploaded", Photo.class,
                        Parameter.with("fields", "id,created_time,from,link,images,icon,album,place,comments{message," +
                                "from," +
                                "created_time}"));
        System.out.println("Received response on get user photos");
        System.out.println("Amount of photos = " + photos.getData().size());
        m.getSession().getFacebookSession().setCurrentUserPhotos(photos);
        if (photos.getData().isEmpty()) m.getSession().getFacebookSession().setCurrentUserPhotoIndex(null);
        else m.getSession().getFacebookSession().setCurrentUserPhotoIndex(0);
    }

    public static void seePrevPhoto(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method seePrevPhoto");

        Connection<Photo> photos = m.getSession().getFacebookSession().getCurrentUserPhotos();
        Integer index = m.getSession().getFacebookSession().getCurrentUserPhotoIndex();

        if (index == 0) { //if first in list
            index = photos.getData().size() - 1;
        } else {
            index--;
        }

        m.getSession().getFacebookSession().setCurrentUserPhotoIndex(index);
    }

    public static void seeNextPhoto(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method seeNextPhoto");

        Connection<Photo> photos = m.getSession().getFacebookSession().getCurrentUserPhotos();
        Integer index = m.getSession().getFacebookSession().getCurrentUserPhotoIndex();

        if (index == photos.getData().size() - 1) { //if last in list
            index = 0;
        } else {
            index++;
        }

        m.getSession().getFacebookSession().setCurrentUserPhotoIndex(index);
    }

    public static void getFeed(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method getFeed");
        if (m.getSession().getFacebookSession().getCurrentUser() == null) {
            System.out.println("FATAL ERROR FATAL ERROR CURRENT USER CANNOT BE NULL!");
            throw new RuntimeException("FATAL ERROR FATAL ERROR CURRENT USER CANNOT BE NULL!");
        }
        Connection<Post> feed =
                FacebookConfig.userFacebookClient.fetchConnection(m.getSession().getFacebookSession().getCurrentUserId() + "/feed", Post.class,
                        Parameter.with("fields", "description,created_time,from,full_picture,link,message,name,place," +
                                "type," +
                                "caption,attachments{description,title,url}"));
        System.out.println("Received response on get user feed");
        System.out.println("Posts in feed = " + feed.getData().size());
        m.getSession().getFacebookSession().setCurrentUserFeed(feed);
        if (feed.getData().isEmpty()) m.getSession().getFacebookSession().setCurrentUserFeedIndex(null);
        else m.getSession().getFacebookSession().setCurrentUserFeedIndex(0);
    }

    public static void seePrevFeed(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method seePrevFeed");

        Connection<Post> feed = m.getSession().getFacebookSession().getCurrentUserFeed();
        Integer index = m.getSession().getFacebookSession().getCurrentUserFeedIndex();

        if (index == 0) { //if first in list
            index = feed.getData().size() - 1;
        } else {
            index--;
        }

        m.getSession().getFacebookSession().setCurrentUserFeedIndex(index);
    }

    public static void seeNextFeed(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method seeNextFeed");

        Connection<Post> feed = m.getSession().getFacebookSession().getCurrentUserFeed();
        Integer index = m.getSession().getFacebookSession().getCurrentUserFeedIndex();

        if (index == feed.getData().size() - 1) { //if last in list
            index = 0;
        } else {
            index++;
        }

        m.getSession().getFacebookSession().setCurrentUserFeedIndex(index);
    }

    public static void getGroups(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method getGroups");
        if (m.getSession().getFacebookSession().getCurrentUser() == null) {
            System.out.println("FATAL ERROR FATAL ERROR CURRENT USER CANNOT BE NULL!");
            throw new RuntimeException("FATAL ERROR FATAL ERROR CURRENT USER CANNOT BE NULL!");
        }
        Connection<Page> groups =
                FacebookConfig.userFacebookClient.fetchConnection(m.getSession().getFacebookSession().getCurrentUserId() + "/likes", Page.class,
                        Parameter.with("fields", "about,category,checkins,company_overview,cover,description," +
                                "emails,fan_count,founded,general_info,genre,hometown,link,name,username,website,bio"));
        System.out.println("Received response on get user groups");
        System.out.println("Groups amount = " + groups.getData().size());
        m.getSession().getFacebookSession().setCurrentUserGroups(groups);
        if (groups.getData().isEmpty()) m.getSession().getFacebookSession().setCurrentUserGroupIndex(null);
        else m.getSession().getFacebookSession().setCurrentUserGroupIndex(0);
    }

    public static void seePrevGroup(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method seePrevGroup");

        Connection<Page> groups = m.getSession().getFacebookSession().getCurrentUserGroups();
        Integer index = m.getSession().getFacebookSession().getCurrentUserGroupIndex();

        if (index == 0) { //if first in list
            index = groups.getData().size() - 1;
        } else {
            index--;
        }

        m.getSession().getFacebookSession().setCurrentUserGroupIndex(index);
    }

    public static void seeNextGroup(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method seeNextGroup");

        Connection<Page> groups = m.getSession().getFacebookSession().getCurrentUserGroups();
        Integer index = m.getSession().getFacebookSession().getCurrentUserGroupIndex();

        if (index == groups.getData().size() - 1) { //if last in list
            index = 0;
        } else {
            index++;
        }

        m.getSession().getFacebookSession().setCurrentUserGroupIndex(index);
    }

    public static void getFriends(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method getFriends");
        if (m.getSession().getFacebookSession().getCurrentUser() == null) {
            System.out.println("FATAL ERROR FATAL ERROR CURRENT USER CANNOT BE NULL!");
            throw new RuntimeException("FATAL ERROR FATAL ERROR CURRENT USER CANNOT BE NULL!");
        }
        Connection<User> users =
                FacebookConfig.userFacebookClient.fetchConnection(m.getSession().getFacebookSession().getCurrentUserId() + "/friends", User.class,
                        Parameter.with("fields", "name,id"));
        System.out.println("Friends amount of " + m.getSession().getFacebookSession().getCurrentUser().getName() + " " +
                "= " + users.getData().size());
        m.getSession().getFacebookSession().setCurrentUserFriends(users);
        if (users.getData().isEmpty()) m.getSession().getFacebookSession().setCurrentUserFriendIndex(null);
        else {
            m.getSession().getFacebookSession().setCurrentUserFriendIndex(0);
            String profilePicUrl = "https://graph.facebook.com/v7.0/" + m.getSession().getFacebookSession().getCurrentUserFriends().getData().get(0).getId() + "/picture?fields=url&width=1000&height=1000&access_token=" + FacebookConfig.ACCESS_TOKEN;
            m.getSession().getFacebookSession().setCurrentUserFriendIndexPictureUrl(profilePicUrl);
        }
    }

    public static void seePrevFriend(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method seePrevFriend");

        Connection<User> users = m.getSession().getFacebookSession().getCurrentUserFriends();
        Integer index = m.getSession().getFacebookSession().getCurrentUserFriendIndex();

        if (index == 0) { //if first in list
            index = users.getData().size() - 1;
        } else {
            index--;
        }
        String profilePicUrl = "https://graph.facebook.com/v7.0/" + users.getData().get(index).getId() + "/picture?fields=url&width=1000&height=1000&access_token=" + FacebookConfig.ACCESS_TOKEN;
        m.getSession().getFacebookSession().setCurrentUserFriendIndexPictureUrl(profilePicUrl);
        m.getSession().getFacebookSession().setCurrentUserFriendIndex(index);
    }

    public static void seeNextFriend(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method seeNextFriend");

        Connection<User> users = m.getSession().getFacebookSession().getCurrentUserFriends();
        Integer index = m.getSession().getFacebookSession().getCurrentUserFriendIndex();

        if (index == users.getData().size() - 1) { //if last in list
            index = 0;
        } else {
            index++;
        }
        String profilePicUrl = "https://graph.facebook.com/v7.0/" + users.getData().get(index).getId() + "/picture?fields=url&width=1000&height=1000&access_token=" + FacebookConfig.ACCESS_TOKEN;
        m.getSession().getFacebookSession().setCurrentUserFriendIndexPictureUrl(profilePicUrl);
        m.getSession().getFacebookSession().setCurrentUserFriendIndex(index);
    }

    public static void openProfile(Message m, ApplicationContext ctx) {
        log.info("Call ProfileActions method openProfile");

        Connection<User> friends = m.getSession().getFacebookSession().getCurrentUserFriends();
        Integer index = m.getSession().getFacebookSession().getCurrentUserFriendIndex();
        User friend = friends.getData().get(index);
        String profilePicUrl = m.getSession().getFacebookSession().getCurrentUserFriendIndexPictureUrl();

        User currentUser = FacebookConfig.userFacebookClient.fetchObject(friend.getId(), User.class,
                Parameter.with("fields", "email,name,birthday,location,address,languages,work,id,link,hometown"));
        System.out.println(currentUser.getName());
        System.out.println(currentUser.getBirthday());
        System.out.println(currentUser.getEmail());
        m.getSession().getFacebookSession().setCurrentUser(currentUser);
        m.getSession().getFacebookSession().setCurrentUserPictureUrl(profilePicUrl);
        m.getSession().getFacebookSession().setCurrentUserId(currentUser.getId());
        System.out.println("Facebook session current user picture url = " + m.getSession().getFacebookSession().getCurrentUserPictureUrl());
    }

}
