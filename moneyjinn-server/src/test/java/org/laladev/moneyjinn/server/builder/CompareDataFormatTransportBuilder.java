package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataFormatTransport;

public class CompareDataFormatTransportBuilder extends CompareDataFormatTransport {

	public static final Long COMPARE_DATA_FORMAT2_ID = 2l;
	public static final Long COMPARE_DATA_FORMAT3_ID = 3l;
	public static final Long COMPARE_DATA_FORMAT4_ID = 4l;
	public static final Long COMPARE_DATA_FORMAT5_ID = 5l;
	public static final Long COMPARE_DATA_FORMAT6_ID = 6l;
	public static final Long NON_EXISTING_ID = 666l;
	public static final Long NEXT_ID = 5l;

	public CompareDataFormatTransportBuilder forCompareDataFormat2() {
		super.setFormatId(COMPARE_DATA_FORMAT2_ID);
		super.setName("Sparda Bank");
		return this;
	}

	public CompareDataFormatTransportBuilder forCompareDataFormat3() {
		super.setFormatId(COMPARE_DATA_FORMAT3_ID);
		super.setName("Postbank Online");
		return this;
	}

	public CompareDataFormatTransportBuilder forCompareDataFormat4() {
		super.setFormatId(COMPARE_DATA_FORMAT4_ID);
		super.setName("XML camt.052.001.03");
		return this;
	}

	public CompareDataFormatTransportBuilder forCompareDataFormat5() {
		super.setFormatId(COMPARE_DATA_FORMAT5_ID);
		super.setName("Sparkasse");
		return this;
	}

	public CompareDataFormatTransportBuilder forCompareDataFormat6() {
		super.setFormatId(COMPARE_DATA_FORMAT6_ID);
		super.setName("Volksbank");
		return this;
	}

	public CompareDataFormatTransport build() {
		final CompareDataFormatTransport transport = new CompareDataFormatTransport();

		transport.setFormatId(super.getFormatId());
		transport.setName(super.getName());

		return transport;
	}
}
