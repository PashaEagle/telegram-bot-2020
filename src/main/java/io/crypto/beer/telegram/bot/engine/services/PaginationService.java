package io.crypto.beer.telegram.bot.engine.services;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.entity.config.ButtonConfig;
import io.crypto.beer.telegram.bot.engine.entity.config.LabelConfig;
import io.crypto.beer.telegram.bot.engine.entity.enums.ButtonType;
import io.crypto.beer.telegram.bot.engine.entity.pagination.IPaginationEntity;
import io.crypto.beer.telegram.bot.engine.entity.pagination.IPaginationService;
import io.crypto.beer.telegram.bot.engine.entity.pagination.PaginationPageDto;
import io.crypto.beer.telegram.bot.engine.entity.pagination.PaginationSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaginationService {

    @Getter
    private enum PageSelectionButtons {
            NEXT("keyboard.pagination.page.next", "PAGINATION_NEXT_PAGE",
                 "pagination.PaginationDynamicValidations.validateNextPageButton", "util.PaginationDefaultActions.nextPageAction"),
            PREVIOUS("keyboard.pagination.page.previous", "PAGINATION_PREVIOUS_PAGE",
                     "pagination.PaginationDynamicValidations.validatePreviousPageButton", "util.PaginationDefaultActions.previousPageAction");

        private String buttonText;
        private String buttonId;
        private String dynamicValidationMethodPath;
        private String actionPath;

        PageSelectionButtons(String buttonText, String buttonId, String dynamicValidationMethodPath,
                String actionPath) {
            this.buttonText = buttonText;
            this.buttonId = buttonId;
            this.dynamicValidationMethodPath = dynamicValidationMethodPath;
            this.actionPath = actionPath;
        }
    }

    private static final String BASE_BUTTON_TEXT_METHOD_PATH = "io.crypto.beer.telegram.bot.business.text.button.";

    @Resource
    private ApplicationContext ctx;

    @Value("${pagination.page.size.default}")
    private Integer PAGE_SIZE_DEFAULT;

    private final MethodCallService methodCallService;

    public void preparePagination(Message message) {
        List<List<ButtonConfig>> buttonsConfiguration = message.getSession().getMessageConfig().getCallbacks();

        buttonsConfiguration.stream()
            .filter(row -> ButtonType.PAGINATION == row.get(0).getType())
            .findFirst()
            .ifPresent(paginationConfigRow -> preparePaginationButtonsConfiguration(message, buttonsConfiguration,
                                                                                    paginationConfigRow.get(0),
                                                                                    buttonsConfiguration
                                                                                        .indexOf(paginationConfigRow)));
    }

    private void preparePaginationButtonsConfiguration(Message message, List<List<ButtonConfig>> buttonsConfiguration,
            ButtonConfig paginationConfig, Integer paginationConfigRowIndex) {
        PaginationSession paginationSession = message.getSession().getPaginationSession();
        fillPaginationSession(paginationSession);
        List<IPaginationEntity> paginationValues = paginationSession.getPaginationValues();

        List<List<ButtonConfig>> paginationConfigReplacement = new ArrayList<>();

        paginationValues
            .forEach(paginationValue -> paginationConfigReplacement.add(Collections.singletonList(ButtonConfig.builder()
                .url(paginationConfig.getUrl())
                .action(paginationConfig.getAction())
                .next(paginationConfig.getNext())
                .inputDataValidationMethodPath(paginationConfig.getInputDataValidationMethodPath())
                .id(String.format("PAGINATION_VALUE_%d", paginationValues.indexOf(paginationValue)))
                .label(LabelConfig.builder()
                    .text(getTextForPaginationButton(paginationConfig.getLabel().getMethodPath(), message,
                                                     paginationValue))
                    .build())
                .build())));

        paginationConfigReplacement
            .add(createPageSelectionButtonConfigs(message.getSession().getActiveButton().getNext()));

        buttonsConfiguration.remove(paginationConfigRowIndex.intValue());
        buttonsConfiguration.addAll(paginationConfigRowIndex, paginationConfigReplacement);
    }

    private void fillPaginationSession(PaginationSession paginationSession) {
        IPaginationService iPaginationService = (IPaginationService) ctx
            .getBean(paginationSession.getPaginationServiceName());
        PaginationPageDto paginationPageDto = iPaginationService
            .getByPagination(paginationSession.getCurrentPage(), PAGE_SIZE_DEFAULT,
                             paginationSession.getRequestParameters());

        paginationSession.setPaginationValues(paginationPageDto.getContent());
        paginationSession.setTotalPages(paginationPageDto.getTotalPages());
    }

    private List<ButtonConfig> createPageSelectionButtonConfigs(String nextKeyboardPath) {
        List<ButtonConfig> pageSelectionRow = new ArrayList<>();
        pageSelectionRow.add(ButtonConfig.builder()
            .id(PageSelectionButtons.PREVIOUS.getButtonId())
            .action(PageSelectionButtons.PREVIOUS.getActionPath())
            .dynamicValidationMethodPath(PageSelectionButtons.PREVIOUS.getDynamicValidationMethodPath())
            .label(LabelConfig.builder().key(PageSelectionButtons.PREVIOUS.getButtonText()).build())
            .next(nextKeyboardPath)
            .build());
        pageSelectionRow.add(ButtonConfig.builder()
            .id(PageSelectionButtons.NEXT.getButtonId())
            .action(PageSelectionButtons.NEXT.getActionPath())
            .dynamicValidationMethodPath(PageSelectionButtons.NEXT.getDynamicValidationMethodPath())
            .label(LabelConfig.builder().key(PageSelectionButtons.NEXT.getButtonText()).build())
            .next(nextKeyboardPath)
            .build());

        return pageSelectionRow;
    }

    private String getTextForPaginationButton(String methodPath, Message message, IPaginationEntity paginationValue) {
        try {
            return (String) methodCallService
                .invokeMethod(null, String.format("%s%s", BASE_BUTTON_TEXT_METHOD_PATH, methodPath),
                              new Class<?>[] { Message.class, IPaginationEntity.class }, message, paginationValue);
        } catch (InvocationTargetException e) {
            log.error("Error while fetching text for pagination value button: {}", e.getMessage());
            return "Default value";
        }
    }
}
