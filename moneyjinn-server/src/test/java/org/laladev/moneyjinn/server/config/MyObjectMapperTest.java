package org.laladev.moneyjinn.server.config;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import tools.jackson.databind.ObjectMapper;

class MyObjectMapperTest extends AbstractTest {
    @Inject
    private ObjectMapper mapper;

    @Test
    void test_nullAttribute_notInJson() {
        final RestObject restObject = new RestObject();
        restObject.setAttribute1("l");
        final String json = this.mapper.writeValueAsString(restObject);
        final boolean attribute2NotExistentInJson = json.contains("attribute2");
        Assertions.assertFalse(attribute2NotExistentInJson);
    }
}
