package io.crypto.beer.telegram.bot.business.config;

import java.time.Clock;
import java.time.ZoneOffset;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {

    @Bean
    public Clock utcClock() {
        return Clock.system(ZoneOffset.UTC);
    }
}
