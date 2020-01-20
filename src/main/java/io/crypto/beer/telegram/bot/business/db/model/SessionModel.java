package io.crypto.beer.telegram.bot.business.db.model;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import io.crypto.beer.telegram.bot.engine.entity.Button;
import io.crypto.beer.telegram.bot.engine.entity.TelegramProfile;
import io.crypto.beer.telegram.bot.engine.entity.config.MessageConfig;
import io.crypto.beer.telegram.bot.engine.entity.pagination.PaginationSession;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document("session")
public class SessionModel {

    @Id
    private String id;
    @Indexed(unique = true)
    private Long chatId;
    private String locale;
    private TelegramProfile telegramProfile;
    private String inputData;
    private String callbackData;
    private Integer lastMessageId;
    private MessageConfig messageConfig;
    private PaginationSession paginationSession;
    private boolean authorized;
    private Button activeButton;
    private Set<Button> currentButtons;

    private Long createdDate;
    private Long lastModifiedDate;
}
