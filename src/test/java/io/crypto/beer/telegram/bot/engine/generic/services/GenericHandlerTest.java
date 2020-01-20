package io.crypto.beer.telegram.bot.engine.generic.services;

import static io.crypto.beer.telegram.bot.engine.generic.deserializer.MessageConfigDeserializer.mapJsonToMessageConfig;
import static io.crypto.beer.telegram.bot.engine.services.TextService.buildFullTextPath;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import io.crypto.beer.telegram.bot.engine.entity.TelegramProfile;
import io.crypto.beer.telegram.bot.engine.entity.Session;
import io.crypto.beer.telegram.bot.engine.entity.config.MessageConfig;

public class GenericHandlerTest {

    private static final String JSON_PATH_SUCCESS = "src/test/resources/bot/message/config/success.json";

    private GenericHandler genericHandler = new GenericHandler();

    @Test
    public void callMethodSuccess() throws IllegalAccessException, InvocationTargetException {
        Session session = createSessionAndSetMessageConfig(JSON_PATH_SUCCESS);
        TelegramProfile profile = TelegramProfile.builder().fullName("Vadiiichka").build();
        session.setTelegramProfile(profile);

        Method method = genericHandler.extractDeclaredMethodFromString(
                                                                       buildFullTextPath(session.getMessageConfig()
                                                                           .getText()
                                                                           .getArgGenerationMethodPath()),
                                                                       new Class[] { Session.class });
        Object[] args = (Object[]) method.invoke(null, session);

        assertThat(args[0]).isEqualTo("Vadiiichka");
    }

    @Test(expected = NullPointerException.class)
    public void callMethodWithNullParametr() {
        genericHandler.extractMethodFromString(null);
    }

    @Test(expected = RuntimeException.class)
    public void callMethodWithWrongMethodPath() throws IllegalAccessException, InvocationTargetException {
        Session session = createSessionAndSetMessageConfig(JSON_PATH_SUCCESS);

        StringBuilder methodPath = new StringBuilder(session.getMessageConfig().getText().getArgGenerationMethodPath());
        methodPath.append("break it!!");
        session.getMessageConfig().getText().setArgGenerationMethodPath(methodPath.toString());

        Method method = genericHandler.extractDeclaredMethodFromString(
                                                                       buildFullTextPath(session.getMessageConfig()
                                                                           .getText()
                                                                           .getArgGenerationMethodPath()),
                                                                       new Class[] { Session.class });
        method.invoke(null, session);
    }

    private Session createSessionAndSetMessageConfig(String configJsonPath) {
        Session session = Session.builder().build();
        MessageConfig config = mapJsonToMessageConfig(configJsonPath);
        session.setMessageConfig(config);
        return session;
    }
}
