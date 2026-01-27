package org.laladev.moneyjinn.server.config;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MyObjectMapperTest extends AbstractTest {
    @Inject
    private ObjectMapper mapper;

    @Test
    void test_nullAttribute_notInJson() {
        final RestObject restObject = new RestObject();
        restObject.setAttribute1("l");
        final String json = this.mapper.writeValueAsString(restObject);
        final boolean attribute2NotExistentInJson = json.contains("attribute2");
        assertFalse(attribute2NotExistentInJson);
    }

    @Test
    void test_dateAttribute_asISOStringInJson() {
        final RestObject restObject = new RestObject();
        restObject.setAttribute3(OffsetDateTime.of(LocalDateTime.of(2020, 1, 2, 3, 4, 5), ZoneOffset.UTC));
        final String json = this.mapper.writeValueAsString(restObject);
        assertEquals("{\"attribute3\":\"2020-01-02T03:04:05Z\"}", json);
    }
}
