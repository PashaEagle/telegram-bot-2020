package io.crypto.beer.telegram.bot.engine.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import io.crypto.beer.telegram.bot.engine.entity.Session;
import io.crypto.beer.telegram.bot.engine.entity.TelegramProfile;
import io.crypto.beer.telegram.bot.engine.generic.services.GenericHandler;

@RunWith(MockitoJUnitRunner.class)
public class ActionHandlerServiceTest {

    @Resource
    private ApplicationContext ctx;
    private MethodCallService methodCallService = new MethodCallService(new GenericHandler());
    @Mock
    private ActionHandlerService actionHandlerService;

    @Before
    public void setup() {
        actionHandlerService = new ActionHandlerService(methodCallService);
    }

    @Test
    public void generateMessageSuccess() {
        Session session = Session.builder()
            .telegramProfile(TelegramProfile.builder().userName("testUsername1").build())
            .build();
        Update update = buildUpdate();

        assertThat(actionHandlerService.generateMessage(update, session)).isNotEmpty();
    }

    private Update buildUpdate() {
        Update update = new Update();
        try {
            Message m = new Message();
            Field messageId;
            messageId = Message.class.getDeclaredField("messageId");
            messageId.setAccessible(true);
            messageId.set(m, 1);
            Field text = Message.class.getDeclaredField("text");
            text.setAccessible(true);
            text.set(m, "a");

            Field message = Update.class.getDeclaredField("message");
            message.setAccessible(true);
            message.set(update, m);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return update;
    }
}
