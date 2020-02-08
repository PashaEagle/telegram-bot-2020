package io.crypto.beer.telegram.bot.business.action.instagram.login;

import io.crypto.beer.telegram.bot.business.instagram.entity.InstagramSession;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramGetChallengeRequest;
import org.brunocvcunha.instagram4j.requests.InstagramResetChallengeRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSelectVerifyMethodRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSendSecurityCodeRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetChallengeResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramLoginResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSelectVerifyMethodResult;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import static io.crypto.beer.telegram.bot.engine.entity.enums.ValidationKey.INSTAGRAM_NAME_EDITED;
import static io.crypto.beer.telegram.bot.engine.entity.enums.ValidationKey.INSTAGRAM_PASSWORD_EDITED;
import static io.crypto.beer.telegram.bot.engine.validation.ValidateConsumers.AFTER_ACTION_SUCCESS;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoginActions {

    static Instagram4j instagram4j;

    public static void editName(Message m, ApplicationContext ctx) {
        log.info("Call LoginActions method editName");
        m.getSession().getInstagramSession().setAccountName(m.getSession().getInputData());
        AFTER_ACTION_SUCCESS.accept(m, INSTAGRAM_NAME_EDITED.getKey());
        log.info("Validation success -> name changed: {}", m.getSession().getInstagramSession().getAccountName());
    }

    public static void editPassword(Message m, ApplicationContext ctx) {
        log.info("Call LoginActions method editPassword");
        m.getSession().getInstagramSession().setPassword(m.getSession().getInputData());
        AFTER_ACTION_SUCCESS.accept(m, INSTAGRAM_PASSWORD_EDITED.getKey());
        log.info("Validation success -> password changed: {}", m.getSession().getInstagramSession().getPassword());
    }

    public static void startLoginAction(Message m, ApplicationContext ctx) {
        log.info("Call LoginActions method startLoginAction");

        if (m.getSession().getInstagramSession() == null) {
            m.getSession().setInstagramSession(InstagramSession.builder().build());
            log.info("New instagram session object set");
        }
    }

    public static void check(Message m, ApplicationContext ctx) {
        log.info("Call LoginActions method check");

        InstagramSearchUsernameResult userResult = null;
        try {
            userResult = instagram4j.sendRequest(new InstagramSearchUsernameRequest("github"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ID for @github is " + userResult.getUser().getPk());
        System.out.println("Number of followers: " + userResult.getUser().getFollower_count());
    }

    public static void authorize(Message m, ApplicationContext ctx) {
        log.info("Call LoginActions method authorize");

        instagram4j =
                Instagram4j.builder()
                        .username(m.getSession().getInstagramSession().getAccountName())
                        .password(m.getSession().getInstagramSession().getPassword())
                        .build();
        m.getSession().getInstagramSession().setInstagram4j(instagram4j);
        instagram4j.setup();
// Get login response
        InstagramLoginResult instagramLoginResult = null;
        try {
            instagramLoginResult = instagram4j.login();
            checkInstagramLoginResult(instagram4j, instagramLoginResult, true);
        } catch (IOException e) {
            log.error("Error when try login");
            e.printStackTrace();
        } catch (Exception e) {
            log.error("Error when try check login result");
            e.printStackTrace();
        }
    }

    /**
     * Check login response.
     *
     * @param instagram4j
     * @param instagramLoginResult
     * @param doReAuthentication
     * @throws Exception
     */
    public static void checkInstagramLoginResult(Instagram4j instagram4j, InstagramLoginResult instagramLoginResult,
                                                 boolean doReAuthentication) throws Exception {
        if (Objects.equals(instagramLoginResult.getStatus(), "ok")
                && instagramLoginResult.getLogged_in_user() != null) {
            // Login success
            System.out.println("■Login success.");
            System.out.println(instagramLoginResult.getLogged_in_user());
        } else if (Objects.equals(instagramLoginResult.getStatus(), "ok")) {
            // Logged in user not exists
            System.out.println("■Logged in user not exists.");

            // TODO
        } else if (Objects.equals(instagramLoginResult.getStatus(), "fail")) {
            // Login failed

            // Check error type
            if (Objects.equals(instagramLoginResult.getError_type(), "checkpoint_challenge_required")) {
                System.out.println("■Challenge URL : " + instagramLoginResult.getChallenge().getUrl());

                // If do re-authentication
                if (doReAuthentication) {
                    // Get challenge URL
                    String challengeUrl = instagramLoginResult.getChallenge().getApi_path().substring(1);

                    // Reset
                    String resetUrl = challengeUrl.replace("challenge", "challenge/reset");
                    InstagramGetChallengeResult getChallengeResult = instagram4j
                            .sendRequest(new InstagramResetChallengeRequest(resetUrl));
                    System.out.println("■Reset result : " + getChallengeResult);

                    // if "close"
                    if (Objects.equals(getChallengeResult.getAction(), "close")) {
                        // Get challenge response
                        getChallengeResult = instagram4j
                                .sendRequest(new InstagramGetChallengeRequest(challengeUrl));
                        System.out.println("■Challenge response : " + getChallengeResult);
                    }

                    // Check step name
                    if (Objects.equals(getChallengeResult.getStep_name(), "select_verify_method")) {
                        // Select verify method

                        // Get select verify method result
                        InstagramSelectVerifyMethodResult postChallengeResult = instagram4j
                                .sendRequest(new InstagramSelectVerifyMethodRequest(challengeUrl,
                                        getChallengeResult.getStep_data().getChoice()));

                        // If "close"
                        if (Objects.equals(postChallengeResult.getAction(), "close")) {
                            // Challenge was closed
                            System.out.println("■Challenge was closed : " + postChallengeResult);

                            // End
                            return;
                        }

                        // Security code has been sent
                        System.out.println("■Security code has been sent : " + postChallengeResult);

                        // Please input Security code
                        System.out.println("Please input Security code");
                        String securityCode = null;
                        try (Scanner scanner = new Scanner(System.in)) {
                            securityCode = scanner.nextLine();
                        }

                        // Send security code
                        InstagramLoginResult securityCodeInstagramLoginResult = instagram4j
                                .sendRequest(new InstagramSendSecurityCodeRequest(challengeUrl, securityCode));

                        // Check login response
                        checkInstagramLoginResult(instagram4j, securityCodeInstagramLoginResult, false);
                    } else if (Objects.equals(getChallengeResult.getStep_name(), "verify_email")) {
                        // Security code has been sent to E-mail
                        System.out.println("■Security code has been sent to E-mail");

                        // TODO
                    } else if (Objects.equals(getChallengeResult.getStep_name(), "verify_code")) {
                        // Security code has been sent to phone
                        System.out.println("■Security code has been sent to phone");

                        // TODO
                    } else if (Objects.equals(getChallengeResult.getStep_name(), "submit_phone")) {
                        // Unknown
                        System.out.println("■Unknown.");

                        // TODO
                    } else if (Objects.equals(getChallengeResult.getStep_name(), "delta_login_review")) {
                        // Maybe showing security confirmation view?
                        System.out.println("■Maybe showing security confirmation view?");

                        // FIXME Send request with choice
                        InstagramSelectVerifyMethodResult instagramSelectVerifyMethodResult = instagram4j
                                .sendRequest(new InstagramSelectVerifyMethodRequest(challengeUrl,
                                        getChallengeResult.getStep_data().getChoice()));
                        System.out.println(instagramSelectVerifyMethodResult);

                        // TODO
                    } else if (Objects.equals(getChallengeResult.getStep_name(), "change_password")) {
                        // Change password needed
                        System.out.println("■Change password needed.");
                    } else if (Objects.equals(getChallengeResult.getAction(), "close")) {
                        // Maybe already challenge closed at other device
                        System.out.println("■Maybe already challenge closed at other device.");

                        // TODO
                    } else {
                        // TODO Other
                        System.out.println("■Other.");
                    }
                }
            } else if (Objects.equals(instagramLoginResult.getError_type(), "bad_password")) {
                System.out.println("■Bad password.");
                System.out.println(instagramLoginResult.getMessage());
            } else if (Objects.equals(instagramLoginResult.getError_type(), "rate_limit_error")) {
                System.out.println("■Too many request.");
                System.out.println(instagramLoginResult.getMessage());
            } else if (Objects.equals(instagramLoginResult.getError_type(), "invalid_parameters")) {
                System.out.println("■Invalid parameter.");
                System.out.println(instagramLoginResult.getMessage());
            } else if (Objects.equals(instagramLoginResult.getError_type(), "needs_upgrade")) {
                System.out.println("■App upgrade needed.");
                System.out.println(instagramLoginResult.getMessage());
            } else if (Objects.equals(instagramLoginResult.getError_type(), "sentry_block")) {
                System.out.println("■Sentry block.");
                System.out.println(instagramLoginResult.getMessage());
            } else if (Objects.equals(instagramLoginResult.getError_type(), "inactive user")) {
                System.out.println("■Inactive user.");
                System.out.println(instagramLoginResult.getMessage());
            } else if (Objects.equals(instagramLoginResult.getError_type(), "unusable_password")) {
                System.out.println("■Unusable password.");
                System.out.println(instagramLoginResult.getMessage());
            } else if (instagramLoginResult.getTwo_factor_info() != null) {
                System.out.println("■Two factor authentication needed.");
                System.out.println(instagramLoginResult.getMessage());

                // If do re-authentication
//                if (doReAuthentication) {
//                    // Two factor authentication
//                    InstagramLoginResult twoFactorInstagramLoginResult = instagram4j.login(VERIFICATION_CODE);
//
//                    // Check login result
//                    checkInstagramLoginResult(instagram4j, twoFactorInstagramLoginResult, false);
//                }
            } else if (Objects.equals(instagramLoginResult.getMessage(),
                    "Please check the code we sent you and try again.")) {
                System.out.println("■Invalid security code.");
                System.out.println(instagramLoginResult.getMessage());
            } else if (Objects.equals(instagramLoginResult.getMessage(),
                    "This code has expired. Go back to request a new one.")) {
                System.out.println("■Security code has expired.");
                System.out.println(instagramLoginResult.getMessage());
            } else if (instagramLoginResult.getChallenge() != null) {
                System.out.println("■Challenge : " + instagramLoginResult.getChallenge());
                System.out.println(instagramLoginResult.getMessage());
                if (instagramLoginResult.getChallenge().getLock() != null
                        && instagramLoginResult.getChallenge().getLock()) {
                    // Login locked
                    System.out.println("■Login locked.");
                } else {
                    // Logged in user exists, or maybe showing security code
                    // view on other device
                    System.out.println("■Logged in user exists, or maybe showing security code view on other device.");
                }
            } else {
                System.out.println("■Unknown error : " + instagramLoginResult.getError_type());
                System.out.println(instagramLoginResult.getMessage());
            }
        } else {
            System.out.println("■Unknown status : " + instagramLoginResult.getStatus());
            System.out.println(instagramLoginResult.getMessage());
        }
    }

//        System.out.println(instagram4j.isLoggedIn());
//        try {
//            InstagramLoginResult result = instagram4j.login();
//            String confirmationUrl = result.getChallenge().getUrl();
//            log.info("Login data is ok, confirmation url: {}", confirmationUrl);
//            m.getSession().getInstagramSession().setConfirmationUrl(confirmationUrl);
//            System.out.println(instagram4j.isLoggedIn());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        InstagramSearchUsernameResult userResult = null;
//        try {
//            userResult = instagram4j.sendRequest(new InstagramSearchUsernameRequest("github"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("ID for @github is " + userResult.getUser().getPk());
//        System.out.println("Number of followers: " + userResult.getUser().getFollower_count());

//        AFTER_ACTION_SUCCESS.accept(m, INSTAGRAM_NAME_EDITED.getKey());

}
