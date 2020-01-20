package io.crypto.beer.telegram.bot.engine.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TelegramProfile {
    private Long telegramId;
    private String fullName;
    private String userName;
    private String phone;
    private String languageCode;
    private boolean bot;
}