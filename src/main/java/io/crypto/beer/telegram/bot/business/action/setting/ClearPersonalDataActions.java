package io.crypto.beer.telegram.bot.business.action.setting;

import io.crypto.beer.telegram.bot.business.db.model.AccountModel;
import io.crypto.beer.telegram.bot.business.db.repository.AccountRepository;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClearPersonalDataActions {

    public static void clear(Message m, ApplicationContext ctx) {
        log.info("Call ClearPersonalDataActions method clear");

        AccountRepository accountRepository = ctx.getBean(AccountRepository.class);
        AccountModel account = accountRepository.getByChatId(m.getSession().getTelegramProfile().getTelegramId());
        if (account != null) accountRepository.delete(account);

        m.getSession().getInstagramSession().setPersonalDataCleared(true);
    }
}
