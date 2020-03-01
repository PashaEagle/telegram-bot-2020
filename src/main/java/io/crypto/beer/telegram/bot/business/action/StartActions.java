package io.crypto.beer.telegram.bot.business.action;

import io.crypto.beer.telegram.bot.business.db.model.AccountModel;
import io.crypto.beer.telegram.bot.business.db.repository.AccountRepository;
import io.crypto.beer.telegram.bot.business.instagram.entity.InstagramSession;
import org.brunocvcunha.instagram4j.Instagram4j;
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

        if (m.getSession().getInstagramSession() == null) {
            m.getSession().setInstagramSession(InstagramSession.builder().instagram4j(Instagram4j.builder().build()).build());
            log.info("New instagram session object set");
        }
    }

    public static void settingsAction(Message m, ApplicationContext ctx) {
        log.info("Call StartActions method settingsAction");
    }
}
