package org.laladev.moneyjinn.core.rest.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class MyObjectMapper extends ObjectMapper {

	private static final long serialVersionUID = 1L;

	public MyObjectMapper() {
		super();
		super.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
		super.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		super.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		super.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		// final SimpleModule module = new SimpleModule("UnixTimestamp", new Version(0, 0, 1, null,
		// null, null));
		// module.addDeserializer(Date.class, new UnixTimestampDeserializer());
		// module.addSerializer(Date.class, new UnixTimestampSerializer());
		// super.registerModule(module);
	}
}
