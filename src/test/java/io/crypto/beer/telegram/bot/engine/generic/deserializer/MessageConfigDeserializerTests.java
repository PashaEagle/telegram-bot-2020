package io.crypto.beer.telegram.bot.engine.generic.deserializer;

import static io.crypto.beer.telegram.bot.engine.generic.deserializer.MessageConfigDeserializer.mapJsonToMessageConfig;
import static javax.validation.Validation.buildDefaultValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Test;

import io.crypto.beer.telegram.bot.engine.entity.config.ButtonConfig;
import io.crypto.beer.telegram.bot.engine.entity.config.MessageConfig;
import io.crypto.beer.telegram.bot.engine.entity.config.TextConfig;

public class MessageConfigDeserializerTests {

    private static final String JSON_SUCCESS_PATH = "src/test/resources/bot/message/config/success.json";
    private static final String JSON_FAIL_PATH = "src/test/resources/bot/message/config/fail.json";

    @Test
    public void checkIfMessageValid() {
        MessageConfig message = mapJsonToMessageConfig(JSON_SUCCESS_PATH);
        assertThat(message.getText()).isNotNull();
        assertThat(message.getCallbacks()).isNotNull();

        TextConfig text = message.getText();
        assertThat(text.getKey()).isNotNull();
        assertThat(text.getKey()).matches("^[a-zA-Z._]{3,}$");

        List<List<ButtonConfig>> keyboard = message.getCallbacks();
        keyboard.stream().forEach(row -> row.stream().forEach(button -> assertThat(button.getId()).isNotNull()));
    }

    @Test(expected = NullPointerException.class)
    public void callMapperWithNullParametr() {
        mapJsonToMessageConfig(null);
    }

    @Test(expected = RuntimeException.class)
    public void callMapperWithWrongPath() {
        mapJsonToMessageConfig("fkoeif");
    }

    @Test
    public void messageTextIsMissing() {
        MessageConfig message = mapJsonToMessageConfig(JSON_FAIL_PATH);
        TextConfig text = message.getText();
        assertThat(text.getKey()).isNull();

        ValidatorFactory factory = buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<TextConfig>> violations = validator.validate(text);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void buttonNameIsMissing() {
        MessageConfig message = mapJsonToMessageConfig(JSON_FAIL_PATH);
        ValidatorFactory factory = buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ButtonConfig>> violations = new HashSet<>();

        List<List<ButtonConfig>> keyboard = message.getCallbacks();
        keyboard.stream().forEach(row -> row.stream().forEach(button -> violations.addAll(validator.validate(button))));
        assertThat(violations).isNotEmpty();
    }
}
