package org.laladev.moneyjinn.hbci.batch.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.laladev.moneyjinn.hbci.batch.entity.AccountData;

public class MoneyYouHandler {

	public AccountData handle(final String iban, final String username, final String password)
			throws ScriptException, IOException, NoSuchMethodException {
		final ScriptEngineManager manager = new ScriptEngineManager();
		final ScriptEngine engine = manager.getEngineByName("JavaScript");

		engine.eval(new BufferedReader(
				new InputStreamReader(this.getClass().getResourceAsStream("/" + "hibiscus.script.moneyou.js"))));

		final Invocable inv = (Invocable) engine;

		final Object object = inv.invokeFunction("HibiscusScripting_MoneYou_kontoSync", iban, username, password);
		if (object instanceof AccountData) {
			return ((AccountData) object);
		}
		return null;
	}

}
