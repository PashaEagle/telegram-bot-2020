package io.crypto.beer.telegram.bot.engine.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import io.crypto.beer.telegram.bot.engine.EngineTestUtils;
import io.crypto.beer.telegram.bot.engine.entity.Button;
import io.crypto.beer.telegram.bot.engine.entity.TelegramProfile;
import io.crypto.beer.telegram.bot.engine.entity.Session;

@RunWith(MockitoJUnitRunner.class)
public class BotOrchestrationTest {

    @Mock
    private TextService textService;
    @Mock
    private KeyboardService keyboardService;
    @Mock
    private ActionHandlerService actionHandlerService;
    @Mock
    private MessageHandlerService messageHandlerService;
    private BotOrchestration botOrchestration;

    @Before
    public void setup() {
        botOrchestration = new BotOrchestration(textService, keyboardService, actionHandlerService,
                messageHandlerService);
        ReflectionTestUtils.setField(botOrchestration, "messageConfigPath", "src/test/resources/bot/message/config/");
    }

    @Test
    public void adaptCallbackSuccess() {
        Set<Button> buttons = new HashSet<>();
        buttons.add(Button.builder().id("success_keyboard_id").next("success.json").build());

        Session session = Session.builder()
            .callbackData("success_keyboard_id")
            .currentButtons(buttons)
            .telegramProfile(TelegramProfile.builder().telegramId(1L).build())
            .lastMessageId(1)
            .build();

        Update update = EngineTestUtils.buildCallbackUpdateDefault();
        List<io.crypto.beer.telegram.bot.engine.entity.Message> messages = new ArrayList<>();
        when(actionHandlerService.generateCallbackMessage(update, session)).thenReturn(messages);

        botOrchestration.adaptCallback(update, session);
        verify(actionHandlerService).generateCallbackMessage(update, session);
        verify(messageHandlerService).sendMessage(messages);
    }

    @Test
    public void shouldSendExpiredMessageWhenOldMessageIsUsed() {
        Update update = EngineTestUtils.buildCallbackUpdateDefault();
        Session session = Session.builder()
            .locale(Locale.ENGLISH)
            .telegramProfile(TelegramProfile.builder().telegramId(1L).build())
            .lastMessageId(100)
            .build();

        botOrchestration.adaptCallback(update, session);

        verify(messageHandlerService, times(1))
            .sendExpiredMessage(session.getLocale(), session.getTelegramProfile().getTelegramId(),
                                update.getCallbackQuery().getMessage().getMessageId());

        verify(textService, times(0)).generateMessageText(any(io.crypto.beer.telegram.bot.engine.entity.Message.class));
        verify(keyboardService, times(0))
            .generateKeyboard(any(io.crypto.beer.telegram.bot.engine.entity.Message.class));
        verify(messageHandlerService, times(0))
            .sendMessage(ArgumentMatchers.<io.crypto.beer.telegram.bot.engine.entity.Message>anyList());
    }

    @Test
    public void shouldSetActiveButtonToNullWhenStartCommandIsEntered() {
        Update update = EngineTestUtils.buildUpdate(1, "/start");
        Session session = Session.builder()
            .telegramProfile(TelegramProfile.builder().build())
            .inputData("/start")
            .activeButton(Button.builder().build())
            .build();

        botOrchestration.adaptMessage(update, session);

        assertThat(session.getActiveButton()).isNull();
    }

    @Test(expected = NullPointerException.class)
    public void getKeyboardNameNull() {
        Session session = Session.builder()
            .callbackData(null)
            .currentButtons(new HashSet<>())
            .telegramProfile(TelegramProfile.builder().telegramId((long) 1).build())
            .build();

        Update update = EngineTestUtils.buildUpdateDefault();

        botOrchestration.adaptCallback(update, session);
    }

    @Test
    public void adaptMessageBreakIf() {
        Session session = Session.builder()
            .inputData("don't start")
            .currentButtons(new HashSet<>())
            .telegramProfile(TelegramProfile.builder().telegramId((long) 1).build())
            .build();

        Update update = EngineTestUtils.buildUpdateDefault();
        when(actionHandlerService.generateMessage(update, session))
            .thenReturn(new ArrayList<io.crypto.beer.telegram.bot.engine.entity.Message>());

        botOrchestration.adaptMessage(update, session);
        assertThat(session.getMessageConfig()).isNull();
    }
}