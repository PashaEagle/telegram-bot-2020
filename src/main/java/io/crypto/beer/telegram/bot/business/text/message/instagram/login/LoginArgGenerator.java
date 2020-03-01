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
        String hiddenPassword = hidePassword(password);
        return new Object[]{

                accountName,
                hiddenPassword
        };
    }

    public static String hidePassword(String password){

        if (password.equals("Not set")) return password;

        StringBuilder hiddenPassword = new StringBuilder(password);
        for (int i = 1; i < password.length()-1; ++i){
            hiddenPassword.setCharAt(i, '*');
        }

        return hiddenPassword.toString();
    }

    public static Object[] getErrorMessage(Session session) {
        log.info("Call LoginArgGenerator method getErrorMessage");
        return new Object[]{ERROR_MESSAGE};
    }
}