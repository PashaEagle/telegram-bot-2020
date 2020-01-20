package io.crypto.beer.telegram.bot.business.validation.dynamic.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import io.crypto.beer.telegram.bot.business.validation.dynamic.util.PaginationDynamicValidations;
import io.crypto.beer.telegram.bot.engine.EngineTestUtils;
import io.crypto.beer.telegram.bot.engine.entity.Message;

public class PaginationDynamicValidationsTest {

    @Mock
    private ApplicationContext context;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnTrueForPreviousPageButton() {
        Message message = EngineTestUtils.buildDefaultMessage();
        message.getSession().getPaginationSession().setCurrentPage(1);
        assertThat(PaginationDynamicValidations.validatePreviousPageButton(message, context)).isEqualTo(true);
    }

    @Test
    public void shouldReturnFalseForPreviousPageButton() {
        Message message = EngineTestUtils.buildDefaultMessage();
        message.getSession().getPaginationSession().setCurrentPage(0);
        assertThat(PaginationDynamicValidations.validatePreviousPageButton(message, context)).isEqualTo(false);
    }

    @Test
    public void shouldReturnTrueForNextPageButton() {
        Message message = EngineTestUtils.buildDefaultMessage();
        message.getSession().getPaginationSession().setCurrentPage(1);
        message.getSession().getPaginationSession().setTotalPages(3);
        assertThat(PaginationDynamicValidations.validateNextPageButton(message, context)).isEqualTo(true);
    }

    @Test
    public void shouldReturnFalseForNextPageButton() {
        Message message = EngineTestUtils.buildDefaultMessage();
        message.getSession().getPaginationSession().setCurrentPage(1);
        message.getSession().getPaginationSession().setTotalPages(2);
        assertThat(PaginationDynamicValidations.validateNextPageButton(message, context)).isEqualTo(false);
    }
}
