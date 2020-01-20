package io.crypto.beer.telegram.bot.engine;

import java.util.HashSet;
import java.util.Locale;

import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import io.crypto.beer.telegram.bot.business.db.model.SessionModel;
import io.crypto.beer.telegram.bot.engine.entity.Button;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.entity.TelegramProfile;
import io.crypto.beer.telegram.bot.engine.entity.Session;
import io.crypto.beer.telegram.bot.engine.entity.pagination.PaginationSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EngineTestUtils {

    public static final Locale SESSION_LOCALE_DEFAULT = Locale.ENGLISH;
    public static final String SESSION_INPUT_DATA_DEFAULT = "input data";

    public static final Integer PAGINATION_SESSION_CURRENT_PAGE_DEFAULT = 2;
    public static final Integer PAGINATION_SESSION_TOTAL_PAGES_DEFAULT = 3;
    public static final String PAGINATION_SESSION_PAGINATION_SERVICE_NAME_DEFAULT = "paginationServiceName";

    public static final String TELEGRAM_MESSAGE_TEXT_DEFAULT = "default text";
    public static final String TELEGRAM_FIRST_NAME_DEFAULT = "Some";
    public static final String TELEGRAM_LAST_NAME_DEFAULT = "One";
    public static final String TELEGRAM_FULL_NAME_DEFAULT = "Some One";
    public static final String TELEGRAM_USER_NAME_DEFAULT = "@Some";
    public static final String TELEGRAM_LANGUAGE_CODE_DEFAULT = "ua";
    public static final Boolean TELEGRAM_BOT_DEFAULT = Boolean.FALSE;

    public static final Long TELEGRAM_PROFILE_ID_DEFAULT = 1L;
    public static final Integer MESSAGE_ID_DEFAULT = 1;

    public static final Long SESSION_CREATE_DATE_DEFAULT = 12L;
    public static final Long SESSION_LAST_MODIFIED_DATE_DEFAULT = 123L;

    public static Update buildUpdateDefault() {
        return buildUpdate(MESSAGE_ID_DEFAULT, TELEGRAM_MESSAGE_TEXT_DEFAULT);
    }

    public static Update buildUpdate(Integer messageId, String text) {
        Update update = new Update();
        org.telegram.telegrambots.meta.api.objects.Message m = new org.telegram.telegrambots.meta.api.objects.Message();

        ReflectionTestUtils.setField(m, "messageId", messageId);
        ReflectionTestUtils.setField(m, "text", text);
        ReflectionTestUtils.setField(m, "from", buildDefaultUser());
        ReflectionTestUtils.setField(update, "message", m);
        return update;
    }

    public static Update buildCallbackUpdateDefault() {
        return buildCallbackUpdate(MESSAGE_ID_DEFAULT, TELEGRAM_MESSAGE_TEXT_DEFAULT);
    }

    public static Update buildCallbackUpdate(Integer messageId, String data) {
        Update update = new Update();
        CallbackQuery callback = new CallbackQuery();
        org.telegram.telegrambots.meta.api.objects.Message m = new org.telegram.telegrambots.meta.api.objects.Message();

        ReflectionTestUtils.setField(m, "messageId", messageId);
        ReflectionTestUtils.setField(m, "from", buildDefaultUser());
        ReflectionTestUtils.setField(callback, "message", m);
        ReflectionTestUtils.setField(callback, "data", data);
        ReflectionTestUtils.setField(update, "callbackQuery", callback);
        return update;
    }

    public static User buildDefaultUser() {
        return new User(TELEGRAM_PROFILE_ID_DEFAULT.intValue(), TELEGRAM_FIRST_NAME_DEFAULT, TELEGRAM_BOT_DEFAULT,
                TELEGRAM_LAST_NAME_DEFAULT, TELEGRAM_USER_NAME_DEFAULT, TELEGRAM_LANGUAGE_CODE_DEFAULT);
    }

    public static Message buildDefaultMessage() {
        return Message.builder().session(buildDefaultSession()).build();
    }

    public static Session buildDefaultSession() {
        return Session.builder()
            .telegramProfile(buildDefaultProfile())
            .paginationSession(buildDefaultPaginationSession())
            .locale(SESSION_LOCALE_DEFAULT)
            .inputData(SESSION_INPUT_DATA_DEFAULT)
            .lastMessageId(MESSAGE_ID_DEFAULT)
            .build();
    }

    public static SessionModel buildDefaultSessionModel() {
        return SessionModel.builder()
            .telegramProfile(buildDefaultProfile())
            .paginationSession(buildDefaultPaginationSession())
            .locale(SESSION_LOCALE_DEFAULT.getLanguage())
            .inputData(SESSION_INPUT_DATA_DEFAULT)
            .lastMessageId(MESSAGE_ID_DEFAULT)
            .currentButtons(new HashSet<Button>())
            .build();
    }

    public static PaginationSession buildDefaultPaginationSession() {
        return PaginationSession.builder()
            .totalPages(PAGINATION_SESSION_TOTAL_PAGES_DEFAULT)
            .currentPage(PAGINATION_SESSION_CURRENT_PAGE_DEFAULT)
            .paginationServiceName(PAGINATION_SESSION_PAGINATION_SERVICE_NAME_DEFAULT)
            .build();
    }

    public static TelegramProfile buildDefaultProfile() {
        return TelegramProfile.builder()
            .telegramId(TELEGRAM_PROFILE_ID_DEFAULT)
            .fullName(TELEGRAM_FULL_NAME_DEFAULT)
            .userName(TELEGRAM_USER_NAME_DEFAULT)
            .languageCode(TELEGRAM_LANGUAGE_CODE_DEFAULT)
            .bot(TELEGRAM_BOT_DEFAULT)
            .build();
    }
}
