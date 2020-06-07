package io.crypto.beer.telegram.bot.business.action.facebook.login;

import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.scope.ScopeBuilder;
import com.restfb.types.User;
import io.crypto.beer.telegram.bot.business.action.facebook.configuration.FacebookConfig;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoginActions {

    public static void startLoginAction(Message m, ApplicationContext ctx) {
        log.info("Call LoginActions method startLoginAction");
        FacebookClient defaultFacebookClient = (FacebookClient) ctx.getBean("defaultFacebookClient");
        ScopeBuilder defaultScopeBuilder = (ScopeBuilder) ctx.getBean("defaultScopeBuilder");
        String loginDialogUrlString = defaultFacebookClient.getLoginDialogUrl(
                FacebookConfig.APP_ID,
                FacebookConfig.REDIRECT_URI,
                defaultScopeBuilder);
        System.out.println("url: " + loginDialogUrlString);
        m.getSession().getFacebookSession().setLoginUrl(loginDialogUrlString);
    }

    public static void authorize(Message m, ApplicationContext ctx) {
        log.info("Call LoginActions method authorize");
        FacebookClient userFacebookClient = (FacebookClient) ctx.getBean("defaultFacebookClient");
        System.out.println("CLIENT=" + (userFacebookClient != null));
        User loggedUser = FacebookConfig.userFacebookClient.fetchObject("me", User.class,
                Parameter.with("fields", "email,name,birthday,location,address,languages,work,id,link,hometown"));
        System.out.println(loggedUser.getName());
        System.out.println(loggedUser.getBirthday());
        System.out.println(loggedUser.getEmail());
        String profilePicUrl = "https://graph.facebook.com/v7.0/" + loggedUser.getId() + "/picture?fields=url&width=1000&height=1000&access_token=" + FacebookConfig.ACCESS_TOKEN;
        m.getSession().getFacebookSession().setLoggedUser(loggedUser);
        m.getSession().getFacebookSession().setLoggedUserPictureUrl(profilePicUrl);
        m.getSession().getFacebookSession().setLoggedUserId(loggedUser.getId());
        m.getSession().getFacebookSession().setCurrentUser(loggedUser);
        m.getSession().getFacebookSession().setCurrentUserPictureUrl(profilePicUrl);
        m.getSession().getFacebookSession().setCurrentUserId(loggedUser.getId());
        System.out.println("Facebook session logged user picture url = " + m.getSession().getFacebookSession().getLoggedUserPictureUrl());
    }

}
