package io.crypto.beer.telegram.bot.business.action.facebook.login.controller;

import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Page;
import com.restfb.types.User;
import io.crypto.beer.telegram.bot.business.action.facebook.configuration.FacebookConfig;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LoginController {

    private final FacebookClient defaultFacebookClient;

    @PostMapping("/api/login")
    void catchFacebookLoginRedirect(@RequestBody AccessTokenResponse accessTokenResponse) {
        System.out.println("Caught facebook redirect, access token = " + accessTokenResponse.getAccess_token() + ", " +
                "token type = " + accessTokenResponse.getToken_type() + ", expiration time = " + accessTokenResponse.getExpires_in());
        String shortLivedToken = accessTokenResponse.getAccess_token();

        FacebookClient.AccessToken extendedAccessToken =
                defaultFacebookClient.obtainExtendedAccessToken(FacebookConfig.APP_ID,
                        FacebookConfig.APP_SECRET, shortLivedToken);

        FacebookConfig.ACCESS_TOKEN = extendedAccessToken.getAccessToken();

        System.out.println("My extended access token: " + extendedAccessToken);
        FacebookConfig.userFacebookClient = defaultFacebookClient.createClientWithAccessToken(extendedAccessToken.getAccessToken());
        String appAccessToken = defaultFacebookClient.obtainAppAccessToken(FacebookConfig.APP_ID, FacebookConfig.APP_SECRET).getAccessToken();
        System.out.println("App access token = " + appAccessToken);
        FacebookConfig.appFacebookClient = defaultFacebookClient.createClientWithAccessToken(appAccessToken);
        System.out.println("New extended access token set to the facebook4j bean");
        User user = FacebookConfig.userFacebookClient.fetchObject("me", User.class, Parameter.with("fields", "email,name,birthday"));
        System.out.println(user.toString());
        System.out.println(user.getName());
    }
}
