package io.crypto.beer.telegram.bot.engine.entity.enums;

public enum ValidationKey {

        NAME_EDITED("message.name.edited"),
        PHONE_EDITED("message.phone.edited"),
        INSTAGRAM_NAME_EDITED("message.instagram.login.name.edited"),
        INSTAGRAM_PASSWORD_EDITED("message.instagram.login.password.edited"),
        INSTAGRAM_USERNAME_FOR_SEARCH_EDITED("message.instagram.profile.find-user-input.edited");

    private String key;

    private ValidationKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
