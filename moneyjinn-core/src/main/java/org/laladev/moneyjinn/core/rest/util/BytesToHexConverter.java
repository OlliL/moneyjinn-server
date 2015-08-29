package org.laladev.moneyjinn.core.rest.util;

public class BytesToHexConverter {
	private static final char[] hexArray = "0123456789abcdef".toCharArray();

	public static String convert(final byte[] bytes) {
		final int l = bytes.length;
		final char[] out = new char[l << 1];
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = hexArray[(0xF0 & bytes[i]) >>> 4];
			out[j++] = hexArray[0x0F & bytes[i]];
		}
		return new String(out);
	}

}
