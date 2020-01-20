package io.crypto.beer.telegram.bot.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import io.crypto.beer.telegram.bot.business.db.model.SessionModel;
import io.crypto.beer.telegram.bot.business.db.repository.SessionModelRepository;
import io.crypto.beer.telegram.bot.engine.EngineTestUtils;
import io.crypto.beer.telegram.bot.engine.entity.Session;
import io.crypto.beer.telegram.bot.engine.entity.TelegramProfile;
import io.crypto.beer.telegram.bot.engine.entity.pagination.PaginationSession;
import io.crypto.beer.telegram.bot.engine.services.SessionService;

public class SessionServiceTest {

    @Mock
    private SessionModelRepository sessionModelRepository;
    @Mock
    private Clock clock;
    @Captor
    private ArgumentCaptor<SessionModel> sessionModelArgumentCaptor;

    private SessionService objectUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectUnderTest = new SessionServiceImpl(clock, sessionModelRepository);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldFetchSessionWhenUserIsNewThenNewSessionIsCreated() {
        when(sessionModelRepository.save(any(SessionModel.class))).thenReturn(SessionModel.builder().build());
        when(clock.millis()).thenReturn(EngineTestUtils.SESSION_CREATE_DATE_DEFAULT);

        Session session = objectUnderTest.fetchSession(EngineTestUtils.buildUpdateDefault(),
                                                       EngineTestUtils.TELEGRAM_PROFILE_ID_DEFAULT,
                                                       EngineTestUtils.TELEGRAM_MESSAGE_TEXT_DEFAULT);

        Map<Long, Session> sessionMap = (ConcurrentHashMap<Long, Session>) ReflectionTestUtils.getField(objectUnderTest,
                                                                                                        "sessions");
        assertThat(sessionMap.get(EngineTestUtils.TELEGRAM_PROFILE_ID_DEFAULT)).isEqualTo(session);

        verify(sessionModelRepository, times(1)).findByChatId(EngineTestUtils.TELEGRAM_PROFILE_ID_DEFAULT);
        verify(sessionModelRepository, times(1)).save(sessionModelArgumentCaptor.capture());
        SessionModel capturedValue = sessionModelArgumentCaptor.getValue();
        assertThat(capturedValue.getChatId()).isEqualTo(EngineTestUtils.TELEGRAM_PROFILE_ID_DEFAULT);
        assertThat(capturedValue.getCreatedDate()).isEqualTo(EngineTestUtils.SESSION_CREATE_DATE_DEFAULT);
        assertThat(capturedValue.getLastModifiedDate()).isEqualTo(EngineTestUtils.SESSION_CREATE_DATE_DEFAULT);

        assertThat(session.getInputData()).isEqualTo(EngineTestUtils.TELEGRAM_MESSAGE_TEXT_DEFAULT);
        assertThat(session.getLocale()).isEqualTo(new Locale(EngineTestUtils.TELEGRAM_LANGUAGE_CODE_DEFAULT));

        TelegramProfile telegramProfile = session.getTelegramProfile();
        assertThat(telegramProfile.getTelegramId()).isEqualTo(EngineTestUtils.TELEGRAM_PROFILE_ID_DEFAULT);
        assertThat(telegramProfile.getFullName()).isEqualTo(EngineTestUtils.TELEGRAM_FULL_NAME_DEFAULT);
        assertThat(telegramProfile.getUserName()).isEqualTo(EngineTestUtils.TELEGRAM_USER_NAME_DEFAULT);
        assertThat(telegramProfile.isBot()).isEqualTo(EngineTestUtils.TELEGRAM_BOT_DEFAULT);
        assertThat(session.getPaginationSession()).isEqualToComparingFieldByField(PaginationSession.builder().build());

        assertThat(session.getCurrentButtons()).isEmpty();
        assertThat(session.getLastMessageId()).isNull();
        assertThat(session.getActiveButton()).isNull();
        assertThat(session.getMessageConfig()).isNull();
        assertThat(session.getCallbackData()).isNull();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldFetchSessionWhenMapDoesNotHasSessionThenFindInRepository() {
        when(sessionModelRepository.findByChatId(EngineTestUtils.TELEGRAM_PROFILE_ID_DEFAULT))
            .thenReturn(Optional.of(EngineTestUtils.buildDefaultSessionModel()));

        Session session = objectUnderTest.fetchSession(EngineTestUtils.buildUpdateDefault(),
                                                       EngineTestUtils.TELEGRAM_PROFILE_ID_DEFAULT,
                                                       EngineTestUtils.TELEGRAM_MESSAGE_TEXT_DEFAULT);

        verify(sessionModelRepository, times(0)).save(any(SessionModel.class));
        verify(sessionModelRepository, times(1)).findByChatId(EngineTestUtils.TELEGRAM_PROFILE_ID_DEFAULT);

        Map<Long, Session> sessionMap = (ConcurrentHashMap<Long, Session>) ReflectionTestUtils.getField(objectUnderTest,
                                                                                                        "sessions");
        assertThat(sessionMap.get(EngineTestUtils.TELEGRAM_PROFILE_ID_DEFAULT)).isEqualTo(session);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldFetchSessionWhenMapHasSessionThenFetchFromMap() {

        Map<Long, Session> sessionMap = Mockito.mock(Map.class);
        ReflectionTestUtils.setField(objectUnderTest, "sessions", sessionMap);
        when(sessionMap.computeIfAbsent(any(), any())).thenReturn(Session.builder().build());

        objectUnderTest.fetchSession(EngineTestUtils.buildUpdateDefault(), EngineTestUtils.TELEGRAM_PROFILE_ID_DEFAULT,
                                     EngineTestUtils.TELEGRAM_MESSAGE_TEXT_DEFAULT);

        verify(sessionModelRepository, times(0)).save(any(SessionModel.class));
        verify(sessionModelRepository, times(0)).findByChatId(EngineTestUtils.TELEGRAM_PROFILE_ID_DEFAULT);
    }

    @Test
    public void shouldUpdateSessionWhenRepositoryHasSessionThenUpdate() {
        when(clock.millis()).thenReturn(EngineTestUtils.SESSION_LAST_MODIFIED_DATE_DEFAULT);
        when(sessionModelRepository.findByChatId(EngineTestUtils.TELEGRAM_PROFILE_ID_DEFAULT)).thenReturn(Optional
            .of(SessionModel.builder().id("Id").createdDate(EngineTestUtils.SESSION_CREATE_DATE_DEFAULT).build()));

        objectUnderTest.update(EngineTestUtils.buildDefaultSession(), EngineTestUtils.TELEGRAM_PROFILE_ID_DEFAULT);
        verify(sessionModelRepository, times(1)).findByChatId(EngineTestUtils.TELEGRAM_PROFILE_ID_DEFAULT);
        verify(sessionModelRepository, times(1)).save(sessionModelArgumentCaptor.capture());

        SessionModel captured = sessionModelArgumentCaptor.getValue();

        assertThat(captured.getCreatedDate()).isEqualTo(EngineTestUtils.SESSION_CREATE_DATE_DEFAULT);
        assertThat(captured.getLastModifiedDate()).isEqualTo(EngineTestUtils.SESSION_LAST_MODIFIED_DATE_DEFAULT);
        assertThat(captured.getId()).isEqualTo("Id");
    }

    @Test
    public void shouldThrowExceptionWhenEntityNotFoundInRepositoryThenThrow() {
        when(sessionModelRepository.findByChatId(EngineTestUtils.TELEGRAM_PROFILE_ID_DEFAULT))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> objectUnderTest.update(EngineTestUtils.buildDefaultSession(),
                                                        EngineTestUtils.TELEGRAM_PROFILE_ID_DEFAULT))
                                                            .isInstanceOf(RuntimeException.class);
        verify(sessionModelRepository, times(1)).findByChatId(EngineTestUtils.TELEGRAM_PROFILE_ID_DEFAULT);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldRemoveSessions() {

        Map<Long, Session> sessionMap = Mockito.mock(Map.class);
        ReflectionTestUtils.setField(objectUnderTest, "sessions", sessionMap);
        when(sessionMap.remove(any())).thenReturn(Session.builder().build());

        List<Long> sessionsToRemove = new ArrayList<>();
        sessionsToRemove.add(1L);
        sessionsToRemove.add(2L);
        sessionsToRemove.add(3L);
        objectUnderTest.remove(sessionsToRemove);

        verify(sessionMap, times(3)).remove(any());
        verify(sessionModelRepository, times(1)).deleteByChatIdIn(sessionsToRemove);
    }
}
