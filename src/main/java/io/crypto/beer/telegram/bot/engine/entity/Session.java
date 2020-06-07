package io.crypto.beer.telegram.bot.engine.entity;

import io.crypto.beer.telegram.bot.business.instagram.entity.FacebookSession;
import io.crypto.beer.telegram.bot.business.instagram.entity.InstagramSession;
import io.crypto.beer.telegram.bot.engine.entity.config.MessageConfig;
import io.crypto.beer.telegram.bot.engine.entity.pagination.PaginationSession;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Data
@Builder
public class Session {

    private Locale locale;
    private TelegramProfile telegramProfile;
    private String inputData;
    private String callbackData;
    private Integer lastMessageId;
    private MessageConfig messageConfig;
    private PaginationSession paginationSession;

    private Button activeButton;
    @Builder.Default
    private Set<Button> currentButtons = new HashSet<>();

    private InstagramSession instagramSession;
    private FacebookSession facebookSession;
}