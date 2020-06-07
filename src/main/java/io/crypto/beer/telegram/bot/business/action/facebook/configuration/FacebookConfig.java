package io.crypto.beer.telegram.bot.business.action.facebook.configuration;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.scope.FacebookPermissions;
import com.restfb.scope.ScopeBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FacebookConfig {

    public static String APP_ID;
    public static String APP_SECRET;
    public static String REDIRECT_URI;
    public static String ACCESS_TOKEN;
    public static FacebookClient userFacebookClient;
    public static FacebookClient appFacebookClient;

    @Value("${facebook.app.id}")
    public void setAppId(String appId) {
        FacebookConfig.APP_ID = appId;
    }

    @Value("${facebook.app.secret}")
    public void setAppSecret(String appSecret) {
        FacebookConfig.APP_SECRET = appSecret;
    }

    @Value("${facebook.redirect.uri}")
    public void setRedirectUri(String redirectUri) {
        FacebookConfig.REDIRECT_URI = redirectUri;
    }

    @Bean
    public ScopeBuilder defaultScopeBuilder() {
        ScopeBuilder scopeBuilder = new ScopeBuilder();
        scopeBuilder.addPermission(FacebookPermissions.EMAIL);
        scopeBuilder.addPermission(FacebookPermissions.ADS_MANAGEMENT);
        scopeBuilder.addPermission(FacebookPermissions.ADS_READ);
        scopeBuilder.addPermission(FacebookPermissions.BUSINESS_MANAGEMENT);
        scopeBuilder.addPermission(FacebookPermissions.CATALOG_MANAGEMENT);
        scopeBuilder.addPermission(FacebookPermissions.GROUPS_ACCESS_MEMBER_INFO);
        scopeBuilder.addPermission(FacebookPermissions.LEADS_RETRIEVAL);
        scopeBuilder.addPermission(FacebookPermissions.PAGES_MANAGE_ADS);
        scopeBuilder.addPermission(FacebookPermissions.PAGES_MANAGE_CTA);
        scopeBuilder.addPermission(FacebookPermissions.PAGES_MANAGE_ENGAGEMENT);
        scopeBuilder.addPermission(FacebookPermissions.PAGES_MANAGE_INSTANT_ARTICLES);
        scopeBuilder.addPermission(FacebookPermissions.PAGES_MANAGE_METADATA);
        scopeBuilder.addPermission(FacebookPermissions.PAGES_MANAGE_POSTS);
        scopeBuilder.addPermission(FacebookPermissions.PAGES_MESSAGING);
        scopeBuilder.addPermission(FacebookPermissions.PAGES_MESSAGING_PHONE_NUMBER);
        scopeBuilder.addPermission(FacebookPermissions.PAGES_MESSAGING_SUBSCRPTIONS);
        scopeBuilder.addPermission(FacebookPermissions.PAGES_READ_ENGAGEMENT);
        scopeBuilder.addPermission(FacebookPermissions.PAGES_READ_USER_CONTENT);
        scopeBuilder.addPermission(FacebookPermissions.PAGES_SHOW_LIST);
        scopeBuilder.addPermission(FacebookPermissions.PUBLIC_PROFILE);
        scopeBuilder.addPermission(FacebookPermissions.PUBLISH_TO_GROUPS);
        scopeBuilder.addPermission(FacebookPermissions.PUBLISH_VIDEO);
        scopeBuilder.addPermission(FacebookPermissions.READ_INSIGHTS);
        scopeBuilder.addPermission(FacebookPermissions.READ_PAGE_MAILBOXES);
        scopeBuilder.addPermission(FacebookPermissions.USER_AGE_RANGE);
        scopeBuilder.addPermission(FacebookPermissions.USER_BIRTHDAY);
        scopeBuilder.addPermission(FacebookPermissions.USER_EVENTS);
        scopeBuilder.addPermission(FacebookPermissions.USER_FRIENDS);
        scopeBuilder.addPermission(FacebookPermissions.USER_GENDER);
        scopeBuilder.addPermission(FacebookPermissions.USER_HOMETOWN);
        scopeBuilder.addPermission(FacebookPermissions.USER_LIKES);
        scopeBuilder.addPermission(FacebookPermissions.USER_LINK);
        scopeBuilder.addPermission(FacebookPermissions.USER_LOCATION);
        scopeBuilder.addPermission(FacebookPermissions.USER_PHOTOS);
        scopeBuilder.addPermission(FacebookPermissions.USER_POSTS);
        scopeBuilder.addPermission(FacebookPermissions.USER_TAGGED_PLACES);
        scopeBuilder.addPermission(FacebookPermissions.USER_VIDEOS);
        return scopeBuilder;
    }

    @Bean
    public FacebookClient defaultFacebookClient() {
        return new DefaultFacebookClient(Version.LATEST);
    }

    @Bean
    public static FacebookClient userFacebookClient() {
        return userFacebookClient;
    }

    @Bean
    public static FacebookClient appFacebookClient() {
        return appFacebookClient;
    }
}
