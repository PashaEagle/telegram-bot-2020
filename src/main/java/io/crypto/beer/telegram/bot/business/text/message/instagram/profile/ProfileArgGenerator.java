package io.crypto.beer.telegram.bot.business.text.message.instagram.profile;

import io.crypto.beer.telegram.bot.engine.entity.Session;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        return new Object[]{

                session.getInstagramSession().getInstagramUser().getUsername(),
                session.getInstagramSession().getInstagramUser().getFull_name(),
                session.getInstagramSession().getInstagramUser().getMedia_count(),
                session.getInstagramSession().getInstagramUser().getFollower_count(),
                session.getInstagramSession().getInstagramUser().getFollowing_count(),
                session.getInstagramSession().getInstagramUser().getPublic_email(),
                session.getInstagramSession().getInstagramUser().getCity_name(),
                session.getInstagramSession().getInstagramUser().getPublic_phone_number(),
                session.getInstagramSession().getInstagramUser().getProfile_pic_url(),
                session.getInstagramSession().getInstagramUser().getExternal_url(),
                "https://www.instagram.com/" + session.getInstagramSession().getInstagramUser().getUsername()
        };
    }

    public static Object[] getFoundPostArgs(Session session) {
        log.info("Call ProfileArgGenerator method getFoundPostArgs");
        return new Object[]{

                session.getInstagramSession().getCurrentPost().getUser().getUsername(),
                session.getInstagramSession().getCurrentPost().getImage_versions2().getCandidates().get(0).getUrl(),
                session.getInstagramSession().getCurrentPost().getComment_count(),
                session.getInstagramSession().getCurrentPost().getCaption().getText()
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
}
