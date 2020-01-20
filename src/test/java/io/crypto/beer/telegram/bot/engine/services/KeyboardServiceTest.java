package io.crypto.beer.telegram.bot.engine.services;

import static io.crypto.beer.telegram.bot.engine.services.ResourceHandlerService.fillMessageConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.crypto.beer.telegram.bot.engine.EngineTestUtils;
import io.crypto.beer.telegram.bot.engine.entity.Button;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.entity.Session;
import io.crypto.beer.telegram.bot.engine.entity.config.ButtonConfig;
import io.crypto.beer.telegram.bot.engine.entity.config.LabelConfig;
import io.crypto.beer.telegram.bot.engine.utils.LocalizationService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ LocalizationService.class })
@PowerMockIgnore({ "com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*" })
public class KeyboardServiceTest {

    private static final String DYNAMIC_VALIDATION_METHOD_PATH = "dynamic.validation.method.path";

    private static final String DYNAMIC_BUTTON_LABEL = "Dynamic button label";

    private static final String DYNAMIC_BUTTON_ID = "DYNAMIC_BUTTON_ID";

    private static final String JSON_PATH_SUCCESS = "src/test/resources/bot/message/config/success.json";

    private static final String JSON_PATH_LABEL_CONFIG_TEST = "src/test/resources/bot/message/config/labelConfigTest.json";

    @Mock
    private PaginationService paginationService;
    @Mock
    private MethodCallService methodCallService;
    private KeyboardService keyboardService;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(LocalizationService.class);
        MockitoAnnotations.initMocks(this);
        keyboardService = new KeyboardService(methodCallService, paginationService);
    }

    @Test
    public void generateKeyboardSuccess() {
        Session session = EngineTestUtils.buildDefaultSession();
        fillMessageConfig(session, JSON_PATH_SUCCESS);

        Message message = Message.builder().session(session).build();

        keyboardService.generateKeyboard(message);
        assertThat(message.getCallbacks()).isNotEmpty();
    }

    @Test(expected = NullPointerException.class)
    public void generateKeyboardNullParam() {
        keyboardService.generateKeyboard(null);
    }

    @Test
    public void shouldNotAddDynamicButtonToKeyboardWhenConditionIsFalse() throws Exception {
        Message message = EngineTestUtils.buildDefaultMessage();
        fillMessageConfig(message.getSession(), JSON_PATH_SUCCESS);

        ButtonConfig dynamicButton = ButtonConfig.builder().build();
        dynamicButton.setId(DYNAMIC_BUTTON_ID);
        dynamicButton.setDynamicValidationMethodPath(DYNAMIC_VALIDATION_METHOD_PATH);

        List<ButtonConfig> buttonsRow = new ArrayList<>();
        buttonsRow.add(dynamicButton);
        message.getSession().getMessageConfig().getCallbacks().add(buttonsRow);

        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        when(methodCallService.invokeMethod(any(), any(), any(), any(), any())).thenReturn(false);
        keyboardService.generateKeyboard(message);

        verify(methodCallService, times(1)).invokeMethod(any(), stringCaptor.capture(), any(), any());
        assertThat(stringCaptor.getValue().contains(DYNAMIC_VALIDATION_METHOD_PATH));

        assertThat(message.getCallbacks()
            .stream()
            .flatMap(List::stream)
            .collect(Collectors.toList())
            .stream()
            .anyMatch(button -> button.getId().equals(DYNAMIC_BUTTON_ID))).isEqualTo(false);

        assertThat(message.getSession()
            .getCurrentButtons()
            .stream()
            .anyMatch(button -> button.getId().equals(DYNAMIC_BUTTON_ID))).isEqualTo(false);
    }

    @Test
    public void shouldAddDynamicButtonToKeyboardWhenConditionIsTrue() throws Exception {
        Message message = EngineTestUtils.buildDefaultMessage();
        fillMessageConfig(message.getSession(), JSON_PATH_SUCCESS);

        ButtonConfig dynamicButton = ButtonConfig.builder().build();
        dynamicButton.setId(DYNAMIC_BUTTON_ID);
        dynamicButton.setDynamicValidationMethodPath(DYNAMIC_VALIDATION_METHOD_PATH);
        dynamicButton.setLabel(LabelConfig.builder().key("LABLE_KEY").build());

        List<ButtonConfig> buttonsRow = new ArrayList<>();
        buttonsRow.add(dynamicButton);
        message.getSession().getMessageConfig().getCallbacks().add(0, buttonsRow);

        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        when(LocalizationService.getMessage("LABLE_KEY", Locale.ENGLISH)).thenReturn(DYNAMIC_BUTTON_LABEL);

        when(methodCallService.invokeMethod(any(), any(), any(), any())).thenReturn(true);
        keyboardService.generateKeyboard(message);

        verify(methodCallService, times(1)).invokeMethod(any(), stringCaptor.capture(), any(), any());
        assertThat(stringCaptor.getValue().contains(DYNAMIC_VALIDATION_METHOD_PATH));

        Button button = message.getCallbacks().get(0).get(0);
        assertThat(button.getId()).isEqualTo(DYNAMIC_BUTTON_ID);
        assertThat(button.getKey()).isEqualTo(DYNAMIC_BUTTON_LABEL);

        button = message.getSession()
            .getCurrentButtons()
            .stream()
            .filter(b -> b.getId().equals(DYNAMIC_BUTTON_ID))
            .findFirst()
            .orElse(null);
        assertThat(button.getKey()).isEqualTo(DYNAMIC_BUTTON_LABEL);
    }

    @Test
    public void shouldSetProperTextFromLabelConfig() throws Exception {
        Message message = EngineTestUtils.buildDefaultMessage();
        ResourceHandlerService.fillMessageConfig(message.getSession(), JSON_PATH_LABEL_CONFIG_TEST);

        List<ButtonConfig> buttonsConfigRow = message.getSession().getMessageConfig().getCallbacks().get(0);

        when(LocalizationService.getMessage(buttonsConfigRow.get(0).getLabel().getKey(),
                                            message.getSession().getLocale())).thenReturn("Value from messages");

        when(methodCallService.invokeMethod(any(), any(), any(), any())).thenReturn("Button text");

        keyboardService.generateKeyboard(message);

        List<Button> buttonsRow = message.getCallbacks().get(0);

        assertThat(buttonsRow.get(0).getKey()).isEqualTo("Value from messages");
        assertThat(buttonsRow.get(1).getKey()).isEqualTo(buttonsConfigRow.get(1).getLabel().getText());
        assertThat(buttonsRow.get(2).getKey()).isEqualTo("Button text");
    }
}