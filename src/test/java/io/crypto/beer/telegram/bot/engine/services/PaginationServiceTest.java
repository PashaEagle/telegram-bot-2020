package io.crypto.beer.telegram.bot.engine.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.crypto.beer.telegram.bot.engine.EngineTestUtils;
import io.crypto.beer.telegram.bot.engine.entity.Button;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.entity.config.ButtonConfig;
import io.crypto.beer.telegram.bot.engine.entity.pagination.IPaginationEntity;
import io.crypto.beer.telegram.bot.engine.entity.pagination.PaginationPageDto;
import io.crypto.beer.telegram.bot.engine.entity.pagination.PaginationSession;
import io.crypto.beer.telegram.bot.engine.generic.deserializer.MessageConfigDeserializer;
import io.crypto.beer.telegram.bot.engine.services.utils.TestPaginationService;

public class PaginationServiceTest {

    private static final String JSON_KEYBOARD_WITHOUT_PAGINATION_PATH = "src/test/resources/bot/message/config/start.json";
    private static final String JSON_KEYBOARD_WITH_PAGINATION_PATH = "src/test/resources/bot/message/config/paginationTest.json";

    @Mock
    private MethodCallService methodCallService;
    @Mock
    private ApplicationContext applicationContext;

    private TestPaginationService paginationServiceExample;
    private PaginationService objectUnderTest;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        paginationServiceExample = new TestPaginationService();
        paginationServiceExample.init();
        objectUnderTest = new PaginationService(methodCallService);
        ReflectionTestUtils.setField(objectUnderTest, "PAGE_SIZE_DEFAULT", 5);
        ReflectionTestUtils.setField(objectUnderTest, "ctx", applicationContext);
    }

    @Test
    public void shouldReturnKeyboardConfigSameAsGiven() throws JsonProcessingException {
        Message messageWithProcessedButtonConfigs = EngineTestUtils.buildDefaultMessage();
        ResourceHandlerService.fillMessageConfig(messageWithProcessedButtonConfigs.getSession(),
                                                 JSON_KEYBOARD_WITHOUT_PAGINATION_PATH);
        objectUnderTest.preparePagination(messageWithProcessedButtonConfigs);

        Message messageWithNotProcessedButtonConfigs = EngineTestUtils.buildDefaultMessage();
        ResourceHandlerService.fillMessageConfig(messageWithNotProcessedButtonConfigs.getSession(),
                                                 JSON_KEYBOARD_WITHOUT_PAGINATION_PATH);

        assertThat(mapper
            .writeValueAsString(messageWithProcessedButtonConfigs.getSession().getMessageConfig().getCallbacks()))
                .isEqualTo(mapper.writeValueAsString(messageWithNotProcessedButtonConfigs.getSession()
                    .getMessageConfig()
                    .getCallbacks()));
    }

    @Test
    public void shouldProperProcessPaginationPlaceholderForFirstPage() throws Exception {
        Message message = EngineTestUtils.buildDefaultMessage();
        message.getSession().getPaginationSession().changePaginationService(paginationServiceExample.getName());
        message.getSession().setActiveButton(Button.builder().next(JSON_KEYBOARD_WITH_PAGINATION_PATH).build());

        ResourceHandlerService.fillMessageConfig(message.getSession(), JSON_KEYBOARD_WITH_PAGINATION_PATH);

        when(applicationContext.getBean(paginationServiceExample.getName())).thenReturn(paginationServiceExample);
        when(methodCallService.invokeMethod(any(), any(), any(), any())).thenReturn("Button text");

        objectUnderTest.preparePagination(message);

        ButtonConfig originalPaginationButtonConfig = MessageConfigDeserializer
            .mapJsonToMessageConfig(JSON_KEYBOARD_WITH_PAGINATION_PATH)
            .getCallbacks()
            .get(0)
            .get(0);

        PaginationSession paginationSession = message.getSession().getPaginationSession();
        List<IPaginationEntity> paginationValues = paginationSession.getPaginationValues();

        PaginationPageDto expectedPaginationPageDto = paginationServiceExample
            .getByPagination(paginationSession.getCurrentPage(), 5, paginationSession.getRequestParameters());

        assertThat(paginationSession.getCurrentPage()).isEqualTo(0);

        assertThat(mapper.writeValueAsString(paginationValues))
            .isEqualTo(mapper.writeValueAsString(expectedPaginationPageDto.getContent()));

        assertThat(paginationSession.getTotalPages()).isEqualTo(expectedPaginationPageDto.getTotalPages());

        List<List<ButtonConfig>> processedButtonConfig = message.getSession().getMessageConfig().getCallbacks();

        assertThat(processedButtonConfig.size()).isEqualTo(paginationValues.size() + 1);

        for (int i = 0; i < paginationValues.size(); i++) {
            ButtonConfig buttonConfig = processedButtonConfig.get(i).get(0);
            assertThat(buttonConfig.getId()).isEqualTo(String.format("PAGINATION_VALUE_%d", i));
            assertThat(buttonConfig.getAction()).isEqualTo(originalPaginationButtonConfig.getAction());
            assertThat(buttonConfig.getNext()).isEqualTo(originalPaginationButtonConfig.getNext());
            assertThat(buttonConfig.getLabel().getText()).isEqualTo("Button text");
        }

        List<ButtonConfig> pageSelectionRow = processedButtonConfig.get(paginationValues.size());

        ButtonConfig previousPageButton = pageSelectionRow.get(0);

        assertThat(previousPageButton.getId()).isEqualTo("PAGINATION_PREVIOUS_PAGE");
        assertThat(previousPageButton.getNext()).isEqualTo(message.getSession().getActiveButton().getNext());
        assertThat(previousPageButton.getAction()).isEqualTo("util.PaginationDefaultActions.previousPageAction");
        assertThat(previousPageButton.getLabel().getKey()).isEqualTo("keyboard.pagination.page.previous");
        assertThat(previousPageButton.getDynamicValidationMethodPath())
            .isEqualTo("pagination.PaginationDynamicValidations.validatePreviousPageButton");

        ButtonConfig nextPageButton = pageSelectionRow.get(1);

        assertThat(nextPageButton.getId()).isEqualTo("PAGINATION_NEXT_PAGE");
        assertThat(nextPageButton.getNext()).isEqualTo(message.getSession().getActiveButton().getNext());
        assertThat(nextPageButton.getAction()).isEqualTo("util.PaginationDefaultActions.nextPageAction");
        assertThat(nextPageButton.getLabel().getKey()).isEqualTo("keyboard.pagination.page.next");
        assertThat(nextPageButton.getDynamicValidationMethodPath())
            .isEqualTo("pagination.PaginationDynamicValidations.validateNextPageButton");

    }
}
