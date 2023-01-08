package org.laladev.moneyjinn.hbci.batch.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationIntrospector;

public class MyObjectMapper extends ObjectMapper {

  private static final long serialVersionUID = 1L;

  public MyObjectMapper() {
    super();
    super.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
    super.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
    super.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    super.setAnnotationIntrospector(
        new JakartaXmlBindAnnotationIntrospector(TypeFactory.defaultInstance()));
    super.registerModule(new JavaTimeModule());

  }
}
