package io.crypto.beer.telegram.bot.engine.validation;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidateConsumers {

    private static final String PHONE_REGEX = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
    private static final String NAME_REGEX = "[a-zA-Z0-9._ а-яА-ЯіІїЇєЄ-]{1,100}";

    public static final Predicate<String> IS_NAME = s -> Pattern.compile(NAME_REGEX).matcher(s).matches();
    public static final Predicate<String> IS_PHONE = s -> Pattern.compile(PHONE_REGEX).matcher(s).matches();

    public static final BiConsumer<Message, String> AFTER_ACTION_FAILURE = (m, key) -> {
        m.getSession().getMessageConfig().getText().setKey(key);
        m.getSession().getMessageConfig().getText().setArgGenerationMethodPath(null);
    };

    public static final BiConsumer<Message, String> AFTER_ACTION_SUCCESS = (m, key) -> {
        m.getSession().getMessageConfig().getText().setKey(key);
        m.getSession().getMessageConfig().getText().setArgGenerationMethodPath(null);
    };
}
