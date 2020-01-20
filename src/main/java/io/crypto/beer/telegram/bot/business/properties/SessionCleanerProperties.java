package io.crypto.beer.telegram.bot.business.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "session.cleaner")
@Component
public class SessionCleanerProperties {

    private Long expirationTime;
    private Integer pageSize;
}
