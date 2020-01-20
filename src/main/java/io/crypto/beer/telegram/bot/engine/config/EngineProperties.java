package io.crypto.beer.telegram.bot.engine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Component
@ConfigurationProperties("bot")
public class EngineProperties {
    
    private String token;
    private String username;
    
    private Creator creator;

    @Setter
    @Getter
    @NoArgsConstructor
    @ConfigurationProperties("creator")
    public static class Creator {
        private Integer id;
    }
}
