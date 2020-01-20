package io.crypto.beer.telegram.bot.engine.services;

import static io.crypto.beer.telegram.bot.engine.services.ResourceHandlerService.fillMessageConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.crypto.beer.telegram.bot.engine.EngineTestUtils;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.entity.TelegramProfile;
import io.crypto.beer.telegram.bot.engine.entity.Session;
import io.crypto.beer.telegram.bot.engine.generic.deserializer.MessageConfigDeserializer;
import io.crypto.beer.telegram.bot.engine.generic.services.GenericHandler;

public class TextServiceTest {

    private static final String JSON_PATH_SUCCESS = "src/test/resources/bot/message/config/success.json";
    private MethodCallService methodCallService;
    private TextService textService;

    @Before
    public void setUp() {
        methodCallService = Mockito.spy(new MethodCallService(new GenericHandler()));
        textService = new TextService(methodCallService);
    }

    @Test
    public void generateMessageTextSuccessConfig()
            throws IllegalAccessException, InvocationTargetException, IOException {
        Session session = Session.builder().telegramProfile(TelegramProfile.builder().fullName("Vadiiiichka").telegramId(1L).build()).build();
        fillMessageConfig(session, JSON_PATH_SUCCESS);

        Message message = Message.builder().session(session).build();
        textService.generateMessageText(message);

        assertThat(message.getText()).isNotNull().isEqualTo("This is the starting keyboard.\nHi, Vadiiiichka");
    }

    @Test(expected = NullPointerException.class)
    public void generateMessageTextWithNullParameters() {
        textService.generateMessageText(null);
    }

    @Test
    public void shouldGetTextFromMethod() throws Exception {
        Message message = EngineTestUtils.buildDefaultMessage();
        message.getSession()
            .setMessageConfig(MessageConfigDeserializer
                .mapJsonToMessageConfig("src/test/resources/bot/message/config/textServiceTest.json"));

        doReturn("Text value").when(methodCallService).invokeMethod(any(), any(), any(), any());

        textService.generateMessageText(message);

        assertThat(message.getText()).isEqualTo("Text value");
    }
}