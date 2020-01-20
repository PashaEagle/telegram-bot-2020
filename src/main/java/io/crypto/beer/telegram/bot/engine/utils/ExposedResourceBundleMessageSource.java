package io.crypto.beer.telegram.bot.engine.utils;

import static java.util.Locale.ENGLISH;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.context.support.ResourceBundleMessageSource;

public class ExposedResourceBundleMessageSource extends ResourceBundleMessageSource {

    public static final String LOCALE_MESSAGES = "locale/messages";

    public Set<String> getKey(Locale locale) {
        ResourceBundle bundle = getResourceBundle(LOCALE_MESSAGES, Optional.ofNullable(locale).orElse(ENGLISH));
        return bundle.keySet();
    }
}
