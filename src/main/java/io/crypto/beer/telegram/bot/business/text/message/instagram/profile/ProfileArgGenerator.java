package io.crypto.beer.telegram.bot.business.text.message.instagram.profile;

import io.crypto.beer.telegram.bot.engine.entity.Session;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brunocvcunha.instagram4j.requests.payload.ImageVersions;
import org.brunocvcunha.instagram4j.requests.payload.InstagramComment;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileArgGenerator {

    public static Object[] getMainPageArgs(Session session) {
        log.info("Call ProfileArgGenerator method getMainPageArgs");
        return new Object[]{
                session.getInstagramSession().getInstagramLoginResult().getLogged_in_user().getFull_name(),
                session.getInstagramSession().getInstagramLoginResult().getLogged_in_user().getProfile_pic_url()
        };
    }

    public static Object[] getLastEnteredInput(Session session) {
        log.info("Call ProfileArgGenerator method getLastEnteredInput");
        return new Object[]{
                session.getInputData()
        };
    }

    public static Object[] getFoundUserArgs(Session session) {
        log.info("Call ProfileArgGenerator method getFoundUserArgs");

        InstagramUser user = session.getInstagramSession().getInstagramUser();

        return new Object[]{
                user.getUsername(),
                user.getFull_name(),
                user.getMedia_count(),
                user.getFollower_count(),
                user.getFollowing_count(),
                user.getPublic_email(),
                user.getCity_name(),
                user.getPublic_phone_number(),
                user.getProfile_pic_url(),
                user.getExternal_url(),
                "https://www.instagram.com/" + user.getUsername()
        };
    }

    public static Object[] getFoundPostArgs(Session session) {
        log.info("Call ProfileArgGenerator method getFoundPostArgs");

        InstagramFeedItem post = session.getInstagramSession().getCurrentPost();
        ImageVersions image = post.getImage_versions2();
        if (image == null) {
            image = post.getCarousel_media().get(0).getImage_versions2();
        }

        return new Object[]{

                post.getUser().getUsername(),
                image.getCandidates().get(0).getUrl(),
                post.getComment_count(),
                post.getCaption().getText()
        };
    }

    public static Object[] getFollowerArgs(Session session) {
        log.info("Call ProfileArgGenerator method getFoundPostArgs");
        Integer index = session.getInstagramSession().getCurrentIndexInUserList();
        InstagramUserSummary user = session.getInstagramSession().getUserList().get(index);
        return new Object[]{

                user.getUsername(),
                user.getFull_name(),
                user.getProfile_pic_url()
        };
    }

    public static Object[] getPostArgs(Session session) {
        log.info("Call ProfileArgGenerator method getPostArgs");

        Integer index = session.getInstagramSession().getCurrentIndexInPostList();
        InstagramFeedItem post = session.getInstagramSession().getPostList().get(index);

        ImageVersions image = post.getImage_versions2();
        if (image == null) {
            image = post.getCarousel_media().get(0).getImage_versions2();
        }

        String text;
        if (post.getCaption() == null) {
            text = "No text";
        } else {
            text = post.getCaption().getText();
        }
        return new Object[]{

                post.getUser().getUsername(),
                text,
                image.getCandidates().get(0).getUrl()
        };
    }

    public static Object[] getUserPostComments(Session session) {
        log.info("Call ProfileArgGenerator method getUserPostComments");

        InstagramFeedItem post = session.getInstagramSession().getCurrentPost();

        ImageVersions image = post.getImage_versions2();
        if (image == null) {
            image = post.getCarousel_media().get(0).getImage_versions2();
        }

        if (post.isComments_disabled())
            return new Object[]{

                    image.getCandidates().get(0).getUrl(),
                    "Commenting disabled for this post."
            };

        if (post.getPreview_comments().size() == 0)
            return new Object[]{

                    image.getCandidates().get(0).getUrl(),
                    "There are no comments under this post yet."
            };

        StringBuilder comments = new StringBuilder();
        for (InstagramComment comment : post.getPreview_comments()) {
            comments.append("<b>").append(comment.getUser().getUsername()).append(":</b>\n");
            comments.append("<i>").append(comment.getText()).append("</i>\n");
            comments.append("\n");
        }

        return new Object[]{

                image.getCandidates().get(0).getUrl(),
                comments.toString()
        };
    }
}
