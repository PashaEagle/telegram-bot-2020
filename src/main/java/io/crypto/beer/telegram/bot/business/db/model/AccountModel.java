package io.crypto.beer.telegram.bot.business.db.model;

import io.crypto.beer.telegram.bot.engine.entity.Button;
import io.crypto.beer.telegram.bot.engine.entity.TelegramProfile;
import io.crypto.beer.telegram.bot.engine.entity.config.MessageConfig;
import io.crypto.beer.telegram.bot.engine.entity.pagination.PaginationSession;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Builder
@Document("account")
public class AccountModel {

    @Id
    private String id;
    @Indexed(unique = true)
    private Long chatId;

    private String accountName;

    private String password;

    private Long createdDate;

    private Long lastModifiedDate;
}
