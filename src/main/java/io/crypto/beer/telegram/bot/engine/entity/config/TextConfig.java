package io.crypto.beer.telegram.bot.engine.entity.config;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TextConfig {

    @NotNull
    private String key;
    private String argGenerationMethodPath;
}
