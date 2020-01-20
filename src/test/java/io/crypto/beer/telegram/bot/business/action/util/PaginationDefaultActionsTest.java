package io.crypto.beer.telegram.bot.business.action.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import io.crypto.beer.telegram.bot.engine.EngineTestUtils;
import io.crypto.beer.telegram.bot.engine.entity.Button;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.entity.pagination.IPaginationEntity;
import io.crypto.beer.telegram.bot.engine.services.utils.TestPaginationEntity;

public class PaginationDefaultActionsTest {

    @Mock
    private ApplicationContext ctx;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldIncreaseCurrentPage() {
        Message message = EngineTestUtils.buildDefaultMessage();
        message.getSession().getPaginationSession().setCurrentPage(1);
        PaginationDefaultActions.nextPageAction(message, ctx);
        assertThat(message.getSession().getPaginationSession().getCurrentPage()).isEqualTo(2);
    }

    @Test
    public void shouldDecreaseCurrentPage() {
        Message message = EngineTestUtils.buildDefaultMessage();
        message.getSession().getPaginationSession().setCurrentPage(1);
        PaginationDefaultActions.previousPageAction(message, ctx);
        assertThat(message.getSession().getPaginationSession().getCurrentPage()).isEqualTo(0);
    }

    @Test
    public void shouldProperSelectPaginationValue() {
        Message message = EngineTestUtils.buildDefaultMessage();

        message.getSession().setActiveButton(Button.builder().id("PAGINATION_VALUE_2").build());
        List<IPaginationEntity> paginationValues = new ArrayList<>();
        paginationValues.add(TestPaginationEntity.builder().id(1).build());
        paginationValues.add(TestPaginationEntity.builder().id(2).build());
        paginationValues.add(TestPaginationEntity.builder().id(12).build());
        message.getSession().getPaginationSession().setPaginationValues(paginationValues);

        PaginationDefaultActions.selectPaginationValueAction(message, ctx);
        TestPaginationEntity selectedValue = (TestPaginationEntity) message.getSession()
            .getPaginationSession()
            .getSelectedPaginationValue();

        assertThat(selectedValue.getId()).isEqualTo(12);
    }
}
