package io.crypto.beer.telegram.bot.business.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.crypto.beer.telegram.bot.business.db.model.SessionModel;
import io.crypto.beer.telegram.bot.engine.EngineTestUtils;
import io.crypto.beer.telegram.bot.engine.entity.Session;

public class SessionConverterTest {

    @Test
    public void shouldProperlyConvertSessionToSessionModel() {
        SessionModel expected = EngineTestUtils.buildDefaultSessionModel();
        Session given = EngineTestUtils.buildDefaultSession();
        SessionModel actual = SessionConverter.convert(given, null);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    public void shouldProperlyConvertSessionModelToSession() {
        Session expected = EngineTestUtils.buildDefaultSession();
        SessionModel given = EngineTestUtils.buildDefaultSessionModel();
        Session actual = SessionConverter.convert(given);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    public void shouldProperlySetModelFields() {
        SessionModel sessionModel = SessionConverter.convert(EngineTestUtils.buildDefaultSession(), 1L);
        assertThat(sessionModel.getId()).isNull();
        assertThat(sessionModel.getChatId()).isEqualTo(1L);
        assertThat(sessionModel.getCreatedDate()).isNull();
        assertThat(sessionModel.getLastModifiedDate()).isNull();

        sessionModel = SessionConverter.convert(EngineTestUtils.buildDefaultSession(), 2L,
                                                EngineTestUtils.SESSION_CREATE_DATE_DEFAULT);
        assertThat(sessionModel.getId()).isNull();
        assertThat(sessionModel.getChatId()).isEqualTo(2L);
        assertThat(sessionModel.getCreatedDate()).isEqualTo(EngineTestUtils.SESSION_CREATE_DATE_DEFAULT);
        assertThat(sessionModel.getLastModifiedDate()).isEqualTo(EngineTestUtils.SESSION_CREATE_DATE_DEFAULT);

        sessionModel = SessionConverter.convert(EngineTestUtils.buildDefaultSession(), 3L,
                                                EngineTestUtils.SESSION_CREATE_DATE_DEFAULT,
                                                EngineTestUtils.SESSION_LAST_MODIFIED_DATE_DEFAULT);
        assertThat(sessionModel.getId()).isNull();
        assertThat(sessionModel.getChatId()).isEqualTo(3L);
        assertThat(sessionModel.getCreatedDate()).isEqualTo(EngineTestUtils.SESSION_CREATE_DATE_DEFAULT);
        assertThat(sessionModel.getLastModifiedDate()).isEqualTo(EngineTestUtils.SESSION_LAST_MODIFIED_DATE_DEFAULT);

        sessionModel = SessionConverter.convert(EngineTestUtils.buildDefaultSession(), 3L,
                                                EngineTestUtils.SESSION_CREATE_DATE_DEFAULT,
                                                EngineTestUtils.SESSION_LAST_MODIFIED_DATE_DEFAULT, "model id");
        assertThat(sessionModel.getId()).isEqualTo("model id");
        assertThat(sessionModel.getChatId()).isEqualTo(3L);
        assertThat(sessionModel.getCreatedDate()).isEqualTo(EngineTestUtils.SESSION_CREATE_DATE_DEFAULT);
        assertThat(sessionModel.getLastModifiedDate()).isEqualTo(EngineTestUtils.SESSION_LAST_MODIFIED_DATE_DEFAULT);
    }
}
