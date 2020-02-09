package io.crypto.beer.telegram.bot.business.action;

import io.crypto.beer.telegram.bot.business.db.model.AccountModel;
import io.crypto.beer.telegram.bot.business.db.repository.AccountRepository;
import org.springframework.context.ApplicationContext;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StartActions {

    public static void startAction(Message m, ApplicationContext ctx) {
        log.info("Call StartActions method startAction");

    }

    public static void settingsAction(Message m, ApplicationContext ctx) {
        log.info("Call StartActions method settingsAction");
    }
}
