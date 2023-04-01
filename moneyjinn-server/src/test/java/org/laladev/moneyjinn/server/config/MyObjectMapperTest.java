
package org.laladev.moneyjinn.server.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;

class MyObjectMapperTest extends AbstractTest {
  @Inject
  private ObjectMapper mapper;

  @Test
  void test_nullAttribute_notInJson() throws JsonProcessingException {
    final RestObject restObject = new RestObject();
    restObject.setAttribute1("l");
    final String json = this.mapper.writeValueAsString(restObject);
    final boolean attribute2NotExistentInJson = json.contains("attribute2");
    Assertions.assertFalse(attribute2NotExistentInJson);
  }
}
