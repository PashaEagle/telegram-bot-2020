package io.crypto.beer.telegram.bot.engine.utils;

import static io.crypto.beer.telegram.bot.engine.utils.LocalizationService.getMessage;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.Test;
import org.springframework.context.NoSuchMessageException;

public class LocalizationServiceTest {

    @Test
    public void getMessageSuccess() {
        String key = "key";
        Object[] args = new Object[] { "argument" };
        Locale locale = Locale.ENGLISH;

        String message = getMessage(key, locale, args);

        assertThat(message).isEqualTo(key);
    }

    @Test(expected = NoSuchMessageException.class)
    public void getMessageWithNullParametersFail() {
        String message = getMessage(null, null);

        assertThat(message).isNull();
    }

    @Test
    public void getMessageWithNullLocaleSuccess() {
        String key = "key";
        String message = getMessage(key, null);

        assertThat(message).isEqualTo(key);
    }
}
