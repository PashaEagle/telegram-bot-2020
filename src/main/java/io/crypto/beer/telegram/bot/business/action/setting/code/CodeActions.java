package io.crypto.beer.telegram.bot.business.action.setting.code;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CodeActions {

    static final String LIKE_ALL_POST_BUTTON_CODE = "MORELIKES";

    public static void activateHiddenFeature(Message m, ApplicationContext ctx) {
        log.info("Call CodeActions method activateHiddenFeature");

        String code = m.getSession().getInputData();
        if (code.equals(LIKE_ALL_POST_BUTTON_CODE)) {
            m.getSession().getInstagramSession().setLikeAllActive(true);
            m.getSession().getMessageConfig().getText().setKey("message.instagram.secret-code.morelikes");
        } else {
            m.getSession().getMessageConfig().getText().setKey("message.instagram.secret-code-failure");
        }
    }
}
