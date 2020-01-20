package io.crypto.beer.telegram.bot.engine.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Button {

    private String key;
    private String url;
    private String action;
    /*
     * Telegram API limitation 1-64 bytes
     */
    private String id;

    private String next;
    private String inputDataValidator;

}
