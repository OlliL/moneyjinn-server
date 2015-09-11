//
// Copyright (c) 2014-2015 Oliver Lehmann <oliver@laladev.org>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//
// $Id: UnixTimestampSerializer.java,v 1.4 2015/08/28 17:46:02 olivleh1 Exp $
//
package org.laladev.moneyjinn.core.rest.util;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class UnixTimestampSerializer extends JsonSerializer<Date> {
	TimeZone tzGMT = TimeZone.getTimeZone("GMT");
	int systemTzOffset = TimeZone.getDefault().getRawOffset();

	@Override
	public void serialize(final Date value, final JsonGenerator jgen, final SerializerProvider provider)
			throws IOException, JsonProcessingException {
		// does not work for dates before 1582 (Change from Julian to Gregorian calendar)
		// jgen.writeNumber((value.getTime() - value.getTimezoneOffset() * 60 * 1000) / 1000);

		// Java 8:
		// jgen.writeString(String.valueOf(value.toLocalDate().atStartOfDay(ZoneId.of("GMT")).toEpochSecond()));

		// does work for dates before 1582 but is slower than the both above
		final GregorianCalendar c = new GregorianCalendar(this.tzGMT);
		c.setGregorianChange(new Date(Long.MIN_VALUE));
		c.set(value.getYear() + 1900, value.getMonth(), value.getDate(), 0, 0, 0);
		c.clear(Calendar.MILLISECOND);

		jgen.writeString(String.valueOf(c.getTimeInMillis() / 1000));

	}
}