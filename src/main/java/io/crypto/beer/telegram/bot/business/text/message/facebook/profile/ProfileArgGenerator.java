package io.crypto.beer.telegram.bot.business.text.message.facebook.profile;

import com.google.inject.internal.cglib.core.internal.$CustomizerRegistry;
import com.restfb.types.Page;
import com.restfb.types.Photo;
import com.restfb.types.Post;
import com.restfb.types.User;
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
        log.info("Call ProfileArgGenerator method getPhotos");
        Integer index = session.getFacebookSession().getCurrentUserPhotoIndex();
        if (index == null) System.out.println("CURRENT USER DONT HAVE PHOTOS");
        Photo currentPhoto = session.getFacebookSession().getCurrentUserPhotos().getData().get(index);
        return new Object[]{
                currentPhoto.getImages().get(0).getSource(),
                currentPhoto.getFrom() != null ? currentPhoto.getFrom().getName() : null,
                currentPhoto.getAlbum() != null ? currentPhoto.getAlbum().getName() : null,
                currentPhoto.getPlace() != null ? currentPhoto.getPlace().getName() : null,
                currentPhoto.getCreatedTime(),
                currentPhoto.getLink()
        };
    }

    public static Object[] getFeed(Session session) {
        log.info("Call ProfileArgGenerator method getFeed");
        Integer index = session.getFacebookSession().getCurrentUserFeedIndex();
        if (index == null) System.out.println("CURRENT USER DONT HAVE POSTS IN FEED");
        Post currentPost = session.getFacebookSession().getCurrentUserFeed().getData().get(index);
        return new Object[]{
                currentPost.getFullPicture(),
                currentPost.getFrom() != null ? currentPost.getFrom().getName() : null,
                currentPost.getMessage(),
                currentPost.getCaption(),
                currentPost.getCreatedTime(),
                currentPost.getCreatedTime(),
                currentPost.getPlace() == null ? null : currentPost.getPlace().getName(),
                currentPost.getAttachments() == null || currentPost.getAttachments().getData().isEmpty() ? null : currentPost.getAttachments().getData().get(0).getTitle(),
                currentPost.getAttachments() == null || currentPost.getAttachments().getData().isEmpty() ? null : currentPost.getAttachments().getData().get(0).getDescription(),
                currentPost.getAttachments() == null || currentPost.getAttachments().getData().isEmpty() ? null : currentPost.getAttachments().getData().get(0).getUrl()
        };
    }

    public static Object[] getGroups(Session session) {
        log.info("Call ProfileArgGenerator method getGroups");
        Integer index = session.getFacebookSession().getCurrentUserGroupIndex();
        if (index == null) System.out.println("CURRENT USER DONT HAVE GROUPS");
        Page page = session.getFacebookSession().getCurrentUserGroups().getData().get(index);
        return new Object[]{
                page.getName(),
                page.getCover() != null ? page.getCover().getSource() : null,
                page.getFanCount(),
                page.getLocation() != null ? page.getLocation().getName() : null,
                page.getCategory(),
                page.getCheckins(),
                page.getGenre(),
                page.getWebsite(),
                page.getEmails().isEmpty() ? null : page.getEmails().get(0),
                page.getAbout(),
                page.getDescription(),
                page.getFounded(),
                page.getGeneralInfo(),
                page.getCompanyOverview(),
                page.getBio(),
                page.getLink()
        };
    }

    public static Object[] getFriends(Session session) {
        log.info("Call ProfileArgGenerator method getFriends");
        Integer index = session.getFacebookSession().getCurrentUserFriendIndex();
        if (index == null) System.out.println("CURRENT USER DONT HAVE FRIENDS THAT ARE USING THIS BOT");
        User user = session.getFacebookSession().getCurrentUserFriends().getData().get(index);
        return new Object[]{
                user.getName(),
                session.getFacebookSession().getCurrentUserFriendIndexPictureUrl()
        };
    }

}
