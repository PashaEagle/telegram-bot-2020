package io.crypto.beer.telegram.bot.business.service.scheduler;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.crypto.beer.telegram.bot.business.db.model.SessionModel;
import io.crypto.beer.telegram.bot.business.db.repository.SessionModelRepository;
import io.crypto.beer.telegram.bot.business.properties.SessionCleanerProperties;
import io.crypto.beer.telegram.bot.engine.services.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class SessionCleanerScheduler {

    private final Clock utcClock;
    private final SessionService sessionService;
    private final SessionModelRepository sessionModelRepository;
    private final SessionCleanerProperties sessionCleanerProperties;

    @Scheduled(cron = "${scheduler.session-cleaner.cron}")
    public void clean() {
        log.info("Cleaning expired sessions");
        cleanExpiredSessions();
    }

    private void cleanExpiredSessions() {
        Long lowestAllowedModifiedDate = utcClock.millis() - sessionCleanerProperties.getExpirationTime();
        List<SessionModel> sessionsToRemove = sessionModelRepository
            .findByLastModifiedDateLessThan(lowestAllowedModifiedDate,
                                            PageRequest.of(0, sessionCleanerProperties.getPageSize()));

        if (!sessionsToRemove.isEmpty()) {
            List<Long> sessionIdList = sessionsToRemove.stream()
                .map(SessionModel::getChatId)
                .collect(Collectors.toList());
            sessionService.remove(sessionIdList);
        }
    }
}
