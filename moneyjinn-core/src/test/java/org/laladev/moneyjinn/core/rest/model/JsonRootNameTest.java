package org.laladev.moneyjinn.core.rest.model;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.reflections.Reflections;

import com.fasterxml.jackson.annotation.JsonRootName;

public class JsonRootNameTest {

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
