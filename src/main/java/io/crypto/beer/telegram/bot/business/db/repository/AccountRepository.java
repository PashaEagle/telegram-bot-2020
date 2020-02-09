package io.crypto.beer.telegram.bot.business.db.repository;

import io.crypto.beer.telegram.bot.business.db.model.AccountModel;
import io.crypto.beer.telegram.bot.business.db.model.SessionModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<AccountModel, String> {

    AccountModel getByChatId(Long chatId);
}
