package io.crypto.beer.telegram.bot.business.validation.dynamic.util;

import org.springframework.context.ApplicationContext;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationDynamicValidations {

    public static boolean validatePreviousPageButton(Message m, ApplicationContext ctx) {
        return !m.getSession().getPaginationSession().getCurrentPage().equals(0);
    }

    public static boolean validateNextPageButton(Message m, ApplicationContext ctx) {
        return !m.getSession()
            .getPaginationSession()
            .getCurrentPage()
            .equals(m.getSession().getPaginationSession().getTotalPages() - 1);
    }
}