package io.crypto.beer.telegram.bot.engine.generic.deserializer;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.crypto.beer.telegram.bot.engine.entity.config.MessageConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageConfigDeserializer {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static MessageConfig mapJsonToMessageConfig(String jsonPath) {
        try {
            File file = new File(jsonPath);
            return mapper.readValue(file, MessageConfig.class);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}