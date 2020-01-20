package io.crypto.beer.telegram.bot.business.action.util;

import org.springframework.context.ApplicationContext;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationDefaultActions {

    public static void previousPageAction(Message m, ApplicationContext ctx) {
        log.info("Call PaginationDefaultActions method previousPageAction");
        m.getSession().getPaginationSession().previousPage();
    }

    public static void nextPageAction(Message m, ApplicationContext ctx) {
        log.info("Call PaginationDefaultActions method nextPageAction");
        m.getSession().getPaginationSession().nextPage();
    }

    public static void selectPaginationValueAction(Message m, ApplicationContext ctx) {
        log.info("Call PaginationDefaultActions method selectPaginationValueAction");
        String paginationButtonId = m.getSession().getActiveButton().getId();
        Integer valuePosition = Integer.parseInt(paginationButtonId.substring(paginationButtonId.lastIndexOf('_') + 1));
        m.getSession().getPaginationSession().setSelectedValue(valuePosition);
    }
}
