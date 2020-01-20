package io.crypto.beer.telegram.bot.engine.services;

import static io.crypto.beer.telegram.bot.engine.services.ResourceHandlerService.fillMessageConfig;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.crypto.beer.telegram.bot.engine.entity.Session;
import io.crypto.beer.telegram.bot.engine.entity.TelegramProfile;

public class ResourceHandlerServiceTest {

    private static final String JSON_PATH_SUCCESS = "src/test/resources/bot/message/config/success.json";

    @Test
    public void shouldFillMessageConfigWhenJsonPathIsCorrectThenFill() {
        Session session = Session.builder().telegramProfile(TelegramProfile.builder().telegramId(1L).build()).build();
        fillMessageConfig(session, JSON_PATH_SUCCESS);
        assertThat(session.getMessageConfig().getText().getKey()).as("Key").isEqualTo("message.startingKeyboard");
        assertThat(session.getMessageConfig().getText().getArgGenerationMethodPath()).as("PATH")
            .isEqualTo("StartArgGenerator.getArgs");
        assertThat(session.getMessageConfig().getCallbacks().get(0).get(0).getLabel().getKey()).as("Label")
            .isEqualTo("keyboard.settings");
        assertThat(session.getMessageConfig().getCallbacks().get(0).get(0).getId()).as("Data").isEqualTo("settings");
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionWhenGenerateMessageTextWithNullParameters() {
        fillMessageConfig(null, null);
    }
}
