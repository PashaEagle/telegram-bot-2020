package io.crypto.beer.telegram.bot.business.text.message.instagram.login;

import io.crypto.beer.telegram.bot.engine.entity.Session;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoginArgGenerator {

    public static String ERROR_MESSAGE = "No error :\\";

    public static Object[] getArgs(Session session) {
        log.info("Call LoginArgGenerator method getArgs");

        String accountName = session.getInstagramSession().getAccountName();
        String password = session.getInstagramSession().getPassword();
        String hidenPassword = hidePassword(password);
        return new Object[]{

                accountName,
                hidenPassword
        };
    }

    public static String hidePassword(String password){

        StringBuilder hidenPassword = new StringBuilder(password);
        for (int i = 1; i < password.length()-1; ++i){
            hidenPassword.setCharAt(i, '*');
        }

        return hidenPassword.toString();
    }

    public static Object[] getErrorMessage(Session session) {
        log.info("Call LoginArgGenerator method getErrorMessage");
        return new Object[]{ERROR_MESSAGE};
    }
}