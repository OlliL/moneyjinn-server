
package org.laladev.moneyjinn.server.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MyObjectMapperTest {
  @Test
  public void test_nullAttribute_notInJson() throws JsonProcessingException {
    final MyObjectMapper mapper = new MyObjectMapper();
    final RestObject restObject = new RestObject();
    restObject.setAttribute1("l");
    final String json = mapper.writeValueAsString(restObject);
    final boolean attribute2NotExistentInJson = json.contains("attribute2");
    Assertions.assertFalse(attribute2NotExistentInJson);
  }
}
