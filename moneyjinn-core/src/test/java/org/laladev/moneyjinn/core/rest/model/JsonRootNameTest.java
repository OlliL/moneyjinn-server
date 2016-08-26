package org.laladev.moneyjinn.core.rest.model;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.reflections.Reflections;

import com.fasterxml.jackson.annotation.JsonRootName;

public class JsonRootNameTest {

	/**
	 * This test makes sure all REST-Model classes which are annotated with {@link JsonRootName}
	 * have JsonRootName matching to their class name. One exception is {@code ErrorResponse} which
	 * is intended to be named "error" in the REST Response.<br>
	 * <br>
	 * This test does not validate that an annotation was specified at all!
	 */
	@Test
	public void testAnnotation() {
		final Reflections reflections = new Reflections("org.laladev.moneyjinn.core.rest");
		final Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(JsonRootName.class);

		for (final Class<?> model : annotated) {
			final JsonRootName jsonRootName = model.getAnnotation(JsonRootName.class);
			if (jsonRootName != null) {
				final String annotatedName = jsonRootName.value();
				if (!model.getSimpleName().equals("ErrorResponse")) {
					Assert.assertEquals(model.getSimpleName(),
							Character.toUpperCase(annotatedName.charAt(0)) + annotatedName.substring(1));
				}
			}
		}
	}
}
