package io.crypto.beer.telegram.bot.engine.services;

import java.util.List;

import org.telegram.telegrambots.meta.api.objects.Update;

import io.crypto.beer.telegram.bot.engine.entity.Session;

public interface SessionService {

    Session fetchSession(Update update, Long chatId, String data);

    void update(Session session, Long chatId);

    void remove(List<Long> keyList);
}
