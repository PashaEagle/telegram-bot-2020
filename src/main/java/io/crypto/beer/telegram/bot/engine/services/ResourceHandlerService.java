package io.crypto.beer.telegram.bot.engine.services;

import static io.crypto.beer.telegram.bot.engine.generic.deserializer.MessageConfigDeserializer.mapJsonToMessageConfig;

import io.crypto.beer.telegram.bot.engine.entity.Session;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResourceHandlerService {

    public static void fillMessageConfig(Session session, String jsonPath) {
        try {
            session.setMessageConfig(mapJsonToMessageConfig(jsonPath));
            log.info("Json parse success. Session is configured for user with chat id {}",
                     session.getTelegramProfile().getTelegramId());
        } catch (Exception e) {
            log.error("Error during filling message config for user with chat id {}:{}",
                      session.getTelegramProfile().getTelegramId(), e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
