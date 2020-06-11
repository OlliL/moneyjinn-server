package org.laladev.moneyjinn.server.config;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

public class MyObjectMapperTest {

	@Test
	public void test_nullAttribute_notInJSON() throws JsonProcessingException {
		final MyObjectMapper mapper = new MyObjectMapper();
		final RestObject restObject = new RestObject();
		restObject.setAttribute1("l");

		final String json = mapper.writeValueAsString(restObject);

		final boolean attribute2NotExistentInJSON = json.contains("attribute2");

		Assert.assertFalse(attribute2NotExistentInJSON);
	}

	@Test
	public void test_objectWrapping() throws JsonProcessingException {
		final MyObjectMapper mapper = new MyObjectMapper();
		final RestObject restObject = new RestObject();

		final String json = mapper.writeValueAsString(restObject);

		Assert.assertEquals("{\"RestObject\":{}}", json);

	}

	@Test
	public void test_objectWrappingRoundTrip() throws IOException {
		final MyObjectMapper mapper = new MyObjectMapper();
		final RestObject restObject = new RestObject();
		restObject.setAttribute1("String");
		restObject.setAttribute2(1L);

		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
		calendar.clear();
		calendar.set(2015, Calendar.JANUARY, 1);

		restObject.setAttribute3(new Date(calendar.getTimeInMillis()));
		restObject.setAttribute4(new Timestamp(System.currentTimeMillis()));

		final String json = mapper.writeValueAsString(restObject);

		final RestObject restObjectActual = mapper.readValue(json, RestObject.class);

		Assert.assertEquals(restObject, restObjectActual);
	}

	@Test
	public void test_DateHandling() throws JsonProcessingException {
		final MyObjectMapper mapper = new MyObjectMapper();
		final RestObject restObject = new RestObject();
		final String date = "2015-01-01";
		final Timestamp timestampJava = Timestamp.valueOf("2015-01-01 01:02:03.004");

		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.GERMANY);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		// Should be changed back to X in the Formatter instead of hardcoding +00:00 with Jackson 3
		// see https://github.com/FasterXML/jackson-databind/issues/2643
		final String timestampJSON = dateFormat.format(timestampJava) + "+00:00";

		restObject.setAttribute3(Date.valueOf(date));
		restObject.setAttribute4(timestampJava);

		final String json = mapper.writeValueAsString(restObject);

		final boolean dateContained = json.contains("\"attribute3\":\"" + date + "\"");
		final boolean timestampContained = json.contains("\"attribute4\":\"" + timestampJSON + "\"");

		Assert.assertTrue(dateContained);
		Assert.assertTrue(timestampContained);
	}
}
