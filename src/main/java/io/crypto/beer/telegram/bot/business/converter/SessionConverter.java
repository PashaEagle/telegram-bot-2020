package io.crypto.beer.telegram.bot.business.converter;

import java.util.Locale;

import io.crypto.beer.telegram.bot.business.db.model.SessionModel;
import io.crypto.beer.telegram.bot.engine.entity.Session;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SessionConverter {

    public static Session convert(SessionModel sessionModel) {
        return Session.builder()
            .locale(new Locale(sessionModel.getLocale()))
            .telegramProfile(sessionModel.getTelegramProfile())
            .inputData(sessionModel.getInputData())
            .callbackData(sessionModel.getCallbackData())
            .lastMessageId(sessionModel.getLastMessageId())
            .messageConfig(sessionModel.getMessageConfig())
            .activeButton(sessionModel.getActiveButton())
            .currentButtons(sessionModel.getCurrentButtons())
            .paginationSession(sessionModel.getPaginationSession())
            .build();
    }

    public static SessionModel convert(Session session, Long chatId) {
        return convert(session, chatId, null, null, null);
    }

    public static SessionModel convert(Session session, Long chatId, Long createDate) {
        return convert(session, chatId, createDate, createDate, null);
    }

    public static SessionModel convert(Session session, Long chatId, Long createDate, Long lastModifiedDate) {
        return convert(session, chatId, createDate, lastModifiedDate, null);
    }

    public static SessionModel convert(Session session, Long chatId, Long createDate, Long lastModifiedDate,
            String id) {
        return SessionModel.builder()
            .id(id)
            .chatId(chatId)
            .locale(session.getLocale().getLanguage())
            .telegramProfile(session.getTelegramProfile())
            .inputData(session.getInputData())
            .callbackData(session.getCallbackData())
            .lastMessageId(session.getLastMessageId())
            .messageConfig(session.getMessageConfig())
            .activeButton(session.getActiveButton())
            .currentButtons(session.getCurrentButtons())
            .paginationSession(session.getPaginationSession())
            .createdDate(createDate)
            .lastModifiedDate(lastModifiedDate)
            .build();
    }

}
