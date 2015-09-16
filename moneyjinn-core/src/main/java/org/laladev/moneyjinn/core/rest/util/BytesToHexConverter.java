package org.laladev.moneyjinn.core.rest.util;

public class BytesToHexConverter {
	private static final char[] HEY_ARRAY = "0123456789abcdef".toCharArray();

	private BytesToHexConverter() {

	}

	public static String convert(final byte[] bytes) {
		final int l = bytes.length;
		final char[] out = new char[l << 1];
		for (int i = 0, j = 0; i < l; i++) {
			final byte byteToWorkOn = bytes[i];
			out[j++] = HEY_ARRAY[(0xF0 & byteToWorkOn) >>> 4];
			out[j++] = HEY_ARRAY[0x0F & byteToWorkOn];
		}
		return new String(out);
	}

}
