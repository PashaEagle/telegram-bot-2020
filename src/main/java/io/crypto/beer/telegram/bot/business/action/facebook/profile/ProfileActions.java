package io.crypto.beer.telegram.bot.business.action.facebook.profile;

import com.restfb.Connection;
import com.restfb.Parameter;
import com.restfb.types.Photo;
import com.restfb.types.User;
import io.crypto.beer.telegram.bot.business.action.facebook.configuration.FacebookConfig;
import io.crypto.beer.telegram.bot.business.instagram.entity.FacebookSession;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;
import org.springframework.context.ApplicationContext;

import java.util.List;

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
        Connection<Photo> photos = FacebookConfig.userFacebookClient.fetchConnection(m.getSession().getFacebookSession().getCurrentUserId() + "/photos/uploaded", Photo.class,
                Parameter.with("fields", "id,created_time,from,link,images,icon,album,place,comments{message,from,created_time}"));
        System.out.println("Received response on get user photos");
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

}
