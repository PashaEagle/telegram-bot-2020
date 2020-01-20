package io.crypto.beer.telegram.bot.engine.utils;

import static io.crypto.beer.telegram.bot.engine.utils.LocalizationService.getMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.entity.Session;
import io.crypto.beer.telegram.bot.engine.interfaces.functional.ThiFunction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Transformer {

    private static final String ERROR_OLD_MESSAGE = "error.oldMessage";

    // ------------------UPDATE------------------ //
    public static final BiFunction<Update, Session, Message> TO_MESSAGE = (u, s) -> {
        Message.MessageBuilder builder = Message.builder().session(s);
        if (u.hasCallbackQuery()) {
            builder.messageId(u.getCallbackQuery().getMessage().getMessageId())
                .text(u.getCallbackQuery().getMessage().getText());
        } else {
            builder.messageId(u.getMessage().getMessageId()).text(u.getMessage().getText());
        }
        Message message = builder.build();
        if (Objects.isNull(message.getSession().getLastMessageId())
                || message.getSession().getLastMessageId().compareTo(message.getMessageId()) < 0) {
            message.getSession().setLastMessageId(message.getMessageId());
        }
        return message;
    };
    // ------------------UPDATE------------------ //

    // ------------------MESSAGE------------------ //
    public static final Function<Message, SendMessage> TO_SEND_MESSAGE_WITH_INLINE = m -> transformSend(m,
                                                                                                        transformInlineKeyboardMarkup(m));

    public static final Function<Message, EditMessageText> TO_EDIT_MESSAGE_TEXT = m -> new EditMessageText()
        .setChatId(m.getSession().getTelegramProfile().getTelegramId())
        .setMessageId(m.getMessageId())
        .setText(m.getText())
        .enableHtml(true)
        .setReplyMarkup(transformInlineKeyboardMarkup(m));

    public static final ThiFunction<Locale, Long, Integer, EditMessageText> TO_EDIT_MESSAGE_TEXT_WITH_EXPIRED_KEYBOARD = (
            locale, chatId, messageId) -> new EditMessageText().setChatId(chatId)
                .setMessageId(messageId)
                .setText(getMessage(ERROR_OLD_MESSAGE, locale));

    public static final BiFunction<String, Long, SendMessage> TO_SEND_STORY = (text,
            chatId) -> new SendMessage().setChatId(chatId).setText(text).enableHtml(true);

    private static SendMessage transformSend(Message message, ReplyKeyboard replyKeyboard) {
        return new SendMessage().setChatId(message.getSession().getTelegramProfile().getTelegramId())
            .setText(message.getText())
            .enableHtml(true)
            .setReplyMarkup(replyKeyboard);
    }

    private static InlineKeyboardMarkup transformInlineKeyboardMarkup(Message m) {
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();
        m.getCallbacks().stream().forEachOrdered(row -> {
            List<InlineKeyboardButton> inlineRow = new ArrayList<>();
            row.stream().forEach(b -> {
                if (Objects.nonNull(b.getUrl())) {
                    inlineRow.add(new InlineKeyboardButton().setUrl(b.getUrl())
                        .setCallbackData(b.getId())
                        .setText(b.getKey()));
                } else {
                    inlineRow.add(new InlineKeyboardButton().setText(b.getKey()).setCallbackData(b.getId()));
                }
            });
            keyboardRows.add(inlineRow);
        });

        return new InlineKeyboardMarkup().setKeyboard(keyboardRows);
    }
    // ------------------MESSAGE------------------ //
}
