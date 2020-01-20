package io.crypto.beer.telegram.bot.business.service;

import java.time.Clock;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import io.crypto.beer.telegram.bot.business.converter.SessionConverter;
import io.crypto.beer.telegram.bot.business.db.model.SessionModel;
import io.crypto.beer.telegram.bot.business.db.repository.SessionModelRepository;
import io.crypto.beer.telegram.bot.engine.entity.Session;
import io.crypto.beer.telegram.bot.engine.entity.Session.SessionBuilder;
import io.crypto.beer.telegram.bot.engine.entity.TelegramProfile;
import io.crypto.beer.telegram.bot.engine.entity.pagination.PaginationSession;
import io.crypto.beer.telegram.bot.engine.services.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class SessionServiceImpl implements SessionService {
    private Map<Long, Session> sessions = new ConcurrentHashMap<>();

    private final Clock utcClock;
    private final SessionModelRepository sessionModelRepository;

    public Session fetchSession(Update update, Long chatId, String data) {
        Session session = this.sessions.computeIfAbsent(chatId,
                                                        key -> sessionModelRepository.findByChatId(key)
                                                            .map(SessionConverter::convert)
                                                            .orElseGet(() -> createSession(update, key)));

        if (update.hasCallbackQuery()) {
            session.setCallbackData(data);
        } else {
            session.setInputData(data);
        }
        return session;
    }

    private Session createSession(Update update, Long chatId) {
        User user = update.getMessage().getFrom();
        log.info("Create new session for user: {}", user.getId());

        SessionBuilder newSessionBuilder = Session.builder();

        TelegramProfile profile = TelegramProfile.builder()
            .telegramId(Long.valueOf(user.getId()))
            .fullName(String.format("%s %s", user.getFirstName(), user.getLastName()))
            .bot(user.getBot())
            .userName(user.getUserName())
            .languageCode(user.getLanguageCode())
            .build();

        Session newSession = newSessionBuilder.telegramProfile(profile)
            .locale(new Locale(user.getLanguageCode()))
            .paginationSession(PaginationSession.builder().build())
            .build();

        Long createDate = utcClock.millis();
        sessionModelRepository.save(SessionConverter.convert(newSession, chatId, createDate));
        return newSession;
    }

    public void update(Session session, Long chatId) {
        SessionModel toUpdate = sessionModelRepository.findByChatId(chatId)
            .orElseThrow(() -> new RuntimeException(String.format("Session with chat id %d not found", chatId)));
        SessionModel sessionModel = SessionConverter.convert(session, chatId, toUpdate.getCreatedDate(),
                                                             utcClock.millis(), toUpdate.getId());
        sessionModelRepository.save(sessionModel);
    }

    public void remove(List<Long> chatIdList) {
        chatIdList.forEach(chatId -> {
            sessions.remove(chatId);
            log.info("Removing session with chat id {} as expired", chatId);
        });
        sessionModelRepository.deleteByChatIdIn(chatIdList);
        log.info("All mentioned sessions were removed");
    }
}
