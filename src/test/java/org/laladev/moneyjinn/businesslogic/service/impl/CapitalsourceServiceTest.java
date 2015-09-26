package org.laladev.moneyjinn.businesslogic.service.impl;

import javax.inject.Inject;

import org.junit.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.service.api.ICapitalsourceService;

public class CapitalsourceServiceTest extends AbstractTest {
	@Inject
	private ICapitalsourceService capitalsourceService;

	@Test(expected = IllegalArgumentException.class)
	public void test_validateNullUser_raisesException() {
		final Capitalsource capitalsource = new Capitalsource();

		this.capitalsourceService.validateCapitalsource(capitalsource);
	}

	@Test(expected = BusinessException.class)
	public void test_createWithInvalidEntity_raisesException() {
		final Capitalsource capitalsource = new Capitalsource();
		capitalsource.setUser(new User(new UserID(1l)));

		this.capitalsourceService.createCapitalsource(capitalsource);
	}

	@Test(expected = BusinessException.class)
	public void test_updateWithInvalidEntity_raisesException() {
		final Capitalsource capitalsource = new Capitalsource();
		capitalsource.setUser(new User(new UserID(1l)));

		this.capitalsourceService.updateCapitalsource(capitalsource);
	}
}
