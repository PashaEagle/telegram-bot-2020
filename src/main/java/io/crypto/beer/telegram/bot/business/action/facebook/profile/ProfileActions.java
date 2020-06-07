package io.crypto.beer.telegram.bot.business.action.facebook.profile;

import com.restfb.Connection;
import com.restfb.Parameter;
import com.restfb.types.Page;
import com.restfb.types.Photo;
import com.restfb.types.Post;
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
                Parameter.with("fields", "id,created_time,from,link,images,icon,album,place,comments{message,from," +
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
                Parameter.with("fields", "description,created_time,from,full_picture,link,message,name,place,type," +
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

}
