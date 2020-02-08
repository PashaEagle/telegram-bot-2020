package io.crypto.beer.telegram.bot.business.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KeyboardPath {

    BASE_PATH("src/main/resources/bot/message/config/"),

    LOGIN_VERIFICATION("instagram/login/confirmation.json"),
    LOGIN_ERROR("instagram/login/error.json"),
    LOGIN_SUCCESS("instagram/profile/main.json");

    private String path;
}
