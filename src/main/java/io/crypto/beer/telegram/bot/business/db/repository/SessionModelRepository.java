package io.crypto.beer.telegram.bot.business.db.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import io.crypto.beer.telegram.bot.business.db.model.SessionModel;

@Repository
public interface SessionModelRepository extends MongoRepository<SessionModel, String> {

    Optional<SessionModel> findByChatId(Long chatId);

    List<SessionModel> findByLastModifiedDateLessThan(Long lowestAllowedModifiedDate, Pageable page);

    void deleteByChatIdIn(List<Long> chatIdList);
}
