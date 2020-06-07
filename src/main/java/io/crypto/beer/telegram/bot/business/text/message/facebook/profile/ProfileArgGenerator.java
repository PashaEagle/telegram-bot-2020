package io.crypto.beer.telegram.bot.business.text.message.facebook.profile;

import com.google.inject.internal.cglib.core.internal.$CustomizerRegistry;
import com.restfb.types.Photo;
import io.crypto.beer.telegram.bot.engine.entity.Session;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileArgGenerator {

    public static Object[] getMainPageArgs(Session session) {
        log.info("Call ProfileArgGenerator method getMainPageArgs");
        return new Object[]{
                session.getFacebookSession().getCurrentUser().getName(),
                session.getFacebookSession().getCurrentUserPictureUrl()
        };
    }

    public static Object[] getDetails(Session session) {
        log.info("Call ProfileArgGenerator method getDetails");
        return new Object[]{
                session.getFacebookSession().getCurrentUser().getName(),
                session.getFacebookSession().getCurrentUser().getEmail(),
                session.getFacebookSession().getCurrentUser().getLocation().getName(),
                session.getFacebookSession().getCurrentUser().getHometownName(),
                session.getFacebookSession().getCurrentUser().getBirthday(),
                session.getFacebookSession().getCurrentUser().getLink()
        };
    }

    public static Object[] getPhotos(Session session) {
        log.info("Call ProfileArgGenerator method getMainPageArgs");
        Integer index = session.getFacebookSession().getCurrentUserPhotoIndex();
        if (index == null) System.out.println("CURRENT USER DONT HAVE PHOTOS");
        Photo currentPhoto = session.getFacebookSession().getCurrentUserPhotos().getData().get(index);
        return new Object[]{
                currentPhoto.getImages().get(0).getSource(),
                currentPhoto.getFrom().getName(),
                currentPhoto.getAlbum().getName(),
                currentPhoto.getPlace().getName(),
                currentPhoto.getCreatedTime(),
                currentPhoto.getLink()
        };
    }

}
