package org.laladev.moneyjinn.server.builder;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateUtil {
	public static Date getGMTDate(final String dateStr) {
		final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		final TimeZone tz = TimeZone.getTimeZone("GMT");

		formatter.setTimeZone(tz);
		try {
			return new Date(formatter.parse(dateStr).getTime());
		} catch (final ParseException e) {
		}
		return null;
	}

}
