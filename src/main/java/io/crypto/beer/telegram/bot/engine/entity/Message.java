package io.crypto.beer.telegram.bot.engine.entity;

import java.util.List;

import io.crypto.beer.telegram.bot.engine.entity.enums.Modifier;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {

    private Session session;
    private Integer messageId;
    private Modifier modifier;

    private String text;
    private List<List<Button>> callbacks;
}
