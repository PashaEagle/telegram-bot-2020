package io.crypto.beer.telegram.bot.engine.services;

import static io.crypto.beer.telegram.bot.engine.entity.enums.Modifier.CREATE;
import static io.crypto.beer.telegram.bot.engine.utils.Transformer.TO_SEND_MESSAGE_WITH_INLINE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import io.crypto.beer.telegram.bot.engine.Bot;
import io.crypto.beer.telegram.bot.engine.entity.Button;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.entity.Session;
import io.crypto.beer.telegram.bot.engine.entity.TelegramProfile;
import io.crypto.beer.telegram.bot.engine.entity.enums.Modifier;

@RunWith(MockitoJUnitRunner.class)
public class MessageHandlerServiceTest {

    @Mock
    private Bot bot;

    private MessageHandlerService messageHandlerService;

    @Before
    public void setup() {
        messageHandlerService = new MessageHandlerService(bot);
    }

    @Test
    public void sendMessageCreate() throws TelegramApiException, NoSuchFieldException, IllegalAccessException {
        List<Message> messages = buildMessages(CREATE);

        org.telegram.telegrambots.meta.api.objects.Message telegramMessage = new org.telegram.telegrambots.meta.api.objects.Message();
        Field messageId = org.telegram.telegrambots.meta.api.objects.Message.class.getDeclaredField("messageId");
        messageId.setAccessible(true);
        messageId.set(telegramMessage, 1);

        when(bot.execute(TO_SEND_MESSAGE_WITH_INLINE.apply(messages.get(0)))).thenReturn(telegramMessage);
        messageHandlerService.sendMessage(messages);

        verify(bot).execute(TO_SEND_MESSAGE_WITH_INLINE.apply(messages.get(0)));
        assertThat(messages.get(0).getSession().getLastMessageId()).isNotNull().isNotZero();
    }

    @Test(expected = NullPointerException.class)
    public void sendMessageEdit() {
        messageHandlerService.sendMessage(null);
    }

    @Test
    public void shouldSendProperExpiredMessage() throws TelegramApiException {
        ArgumentCaptor<EditMessageText> editMessageTextCaptor = ArgumentCaptor.forClass(EditMessageText.class);

        messageHandlerService.sendExpiredMessage(Locale.ENGLISH, 1L, 5);
        verify(bot, times(1)).execute(editMessageTextCaptor.capture());

        EditMessageText editMessageText = editMessageTextCaptor.getValue();

        assertThat(editMessageText.getChatId()).isEqualTo("1");
        assertThat(editMessageText.getMessageId()).isEqualTo(5);
        assertThat(editMessageText.getText()).isNotBlank();
        assertThat(editMessageText.getReplyMarkup()).isNull();
    }

    private List<Message> buildMessages(Modifier modifier) {
        List<List<Button>> keyboard = new ArrayList<>();
        Session session = Session.builder()
            .lastMessageId(1)
            .telegramProfile(TelegramProfile.builder().telegramId((long) 1).build())
            .build();
        Message message = Message.builder()
            .text("a")
            .messageId(1)
            .callbacks(keyboard)
            .messageId(1)
            .modifier(modifier)
            .session(session)
            .build();
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        return messages;
    }

}
