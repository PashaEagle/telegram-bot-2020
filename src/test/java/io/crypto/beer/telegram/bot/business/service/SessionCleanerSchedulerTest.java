package io.crypto.beer.telegram.bot.business.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyList;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import io.crypto.beer.telegram.bot.business.db.model.SessionModel;
import io.crypto.beer.telegram.bot.business.db.repository.SessionModelRepository;
import io.crypto.beer.telegram.bot.business.properties.SessionCleanerProperties;
import io.crypto.beer.telegram.bot.business.service.scheduler.SessionCleanerScheduler;
import io.crypto.beer.telegram.bot.engine.services.SessionService;

public class SessionCleanerSchedulerTest {

    @Mock
    private SessionService sessionService;
    @Mock
    private Clock clock;
    @Mock
    private SessionModelRepository sessionModelRepository;
    @Mock
    private SessionCleanerProperties sessionCleanerProperties;

    private SessionCleanerScheduler objectUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(sessionCleanerProperties.getPageSize()).thenReturn(5);
        when(sessionCleanerProperties.getExpirationTime()).thenReturn(600000L);
        objectUnderTest = new SessionCleanerScheduler(clock, sessionService, sessionModelRepository,
                sessionCleanerProperties);
    }

    @Test
    public void shouldCleanSessionsWhenExpiredSessionsExistThenRemove() {
        List<SessionModel> sessionsToRemove = new ArrayList<>();
        sessionsToRemove.add(SessionModel.builder().chatId(1L).build());
        sessionsToRemove.add(SessionModel.builder().chatId(2L).build());
        sessionsToRemove.add(SessionModel.builder().chatId(3L).build());

        when(clock.millis()).thenReturn(700000L);
        when(sessionModelRepository.findByLastModifiedDateLessThan(100000L, PageRequest.of(0, 5)))
            .thenReturn(sessionsToRemove);
        objectUnderTest.clean();
        verify(sessionService, times(1))
            .remove(sessionsToRemove.stream().map(SessionModel::getChatId).collect(Collectors.toList()));
    }

    @Test
    public void shouldDoNothingWhenExpiredSessionListIsEmpty() {
        List<SessionModel> sessionsToRemove = new ArrayList<>();

        when(clock.millis()).thenReturn(700000L);
        when(sessionModelRepository.findByLastModifiedDateLessThan(100000L, PageRequest.of(0, 5)))
            .thenReturn(sessionsToRemove);
        objectUnderTest.clean();
        verify(sessionService, times(0)).remove(anyList());
    }
}
