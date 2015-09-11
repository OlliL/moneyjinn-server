package org.laladev.moneyjinn.core.rest.util;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(MockitoJUnitRunner.class)
public class UnixTimestampSerializerTest {

	@Mock
	private JsonGenerator gen;

	@Test
	public void test_TimestampSerialization_20100101() throws JsonProcessingException, IOException {
		final UnixTimestampSerializer serializer = new UnixTimestampSerializer();
		final Date date = Date.valueOf(LocalDate.parse("2010-01-01"));
		serializer.serialize(date, this.gen, null);
		final String expectedOutput = "1262304000";
		Mockito.verify(this.gen, Mockito.times(1)).writeString(expectedOutput);
	}

	@Test
	public void test_TimestampSerialization_00010101() throws JsonProcessingException, IOException {
		final UnixTimestampSerializer serializer = new UnixTimestampSerializer();
		final Date date = Date.valueOf(LocalDate.parse("0001-01-01"));
		serializer.serialize(date, this.gen, null);
		final String expectedOutput = "-62135596800";
		Mockito.verify(this.gen, Mockito.times(1)).writeString(expectedOutput);
	}

	@Test
	public void test_TimestampSerialization_19810524() throws JsonProcessingException, IOException {
		final UnixTimestampSerializer serializer = new UnixTimestampSerializer();
		final Date date = Date.valueOf(LocalDate.parse("1981-05-24"));
		serializer.serialize(date, this.gen, null);
		final String expectedOutput = "359510400";
		Mockito.verify(this.gen, Mockito.times(1)).writeString(expectedOutput);
	}

	@Test
	public void test_TimestampSerialization_29991231() throws JsonProcessingException, IOException {
		final UnixTimestampSerializer serializer = new UnixTimestampSerializer();
		final Date date = Date.valueOf(LocalDate.parse("2999-12-31"));
		serializer.serialize(date, this.gen, null);
		final String expectedOutput = "32503593600";
		Mockito.verify(this.gen, Mockito.times(1)).writeString(expectedOutput);
	}
}
