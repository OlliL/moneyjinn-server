package org.laladev.hbci.entity.mapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.kapott.hbci.GV_Result.GVRKUms.UmsLine;
import org.kapott.hbci.structures.Konto;
import org.kapott.hbci.structures.Saldo;
import org.kapott.hbci.structures.Value;

class ObjectBuilder {

	public static Konto getKonto() {
		final Konto account = new Konto("10001000", "30000000");
		account.iban = "DE13300000000010001000";
		account.bic = "MARKDEF1300";

		return account;
	}

	public static Value getValue() {
		final Value value = new Value(BigDecimal.TEN, "EUR");
		return value;
	}

	public static Saldo getSaldo() {
		final Saldo saldo = new Saldo();
		saldo.timestamp = new Date(System.currentTimeMillis());
		saldo.value = getValue();
		return saldo;
	}

	public static UmsLine getUmsLine(final Calendar currentDate) {
		final UmsLine entry = new UmsLine();
		entry.usage = Arrays.asList("");
		entry.gvcode = "5";
		entry.bdate = currentDate.getTime();
		entry.valuta = currentDate.getTime();
		entry.value = getValue();
		entry.saldo = getSaldo();
		return entry;
	}
}
