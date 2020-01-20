package io.crypto.beer.telegram.bot.engine.entity.config;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MessageConfig {

    @NotNull
    private TextConfig text;
    @NotEmpty
    private List<List<ButtonConfig>> callbacks;
}