package org.laladev.moneyjinn.hbci.batch.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MyObjectMapper extends ObjectMapper {

  private static final long serialVersionUID = 1L;

  public MyObjectMapper() {
    super();
    super.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    super.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    super.registerModule(new JavaTimeModule());

  }
}
