package io.crypto.beer.telegram.bot.business.db.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
