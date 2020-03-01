package io.crypto.beer.telegram.bot.business.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KeyboardPath {

    BASE_PATH("src/main/resources/bot/message/config/"),

    LOGIN_MAIN_PAGE("instagram/login/main.json"),
    LOGIN_VERIFICATION("instagram/login/confirmation.json"),
    LOGIN_ERROR("instagram/login/error.json"),
    LOGIN_SUCCESS("instagram/profile/main.json"),

    FIND_USER_INPUT("instagram/profile/input/find-user-input.json"),
    FIND_POST_INPUT("instagram/profile/input/find-post-input.json"),
    FIND_USER_VIEW("instagram/profile/view/find-user-view.json"),
    FIND_POST_VIEW("instagram/profile/view/find-post-view.json"),

    SEE_POSTS_VIEW("instagram/profile/view/see-posts-view.json");

    private String path;
}
