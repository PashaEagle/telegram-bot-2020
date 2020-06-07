package io.crypto.beer.telegram.bot.business.action.facebook.profile;

import com.restfb.types.User;
import io.crypto.beer.telegram.bot.business.instagram.entity.FacebookSession;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.springframework.context.ApplicationContext;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileActions {

    static Instagram4j instagram4j;

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
    }
}
