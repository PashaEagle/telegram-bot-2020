package io.crypto.beer.telegram.bot.business.action.facebook.configuration;

import com.restfb.FacebookClient;
import com.restfb.scope.ScopeBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@AllArgsConstructor
public class Starter {

    private final ScopeBuilder defaultScopeBuilder;
    private final FacebookClient defaultFacebookClient;

    @PostConstruct
    public void start() {

    }
}
