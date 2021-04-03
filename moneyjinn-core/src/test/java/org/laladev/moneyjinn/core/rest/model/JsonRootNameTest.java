package org.laladev.moneyjinn.core.rest.model;

import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

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
		final Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(XmlRootElement.class);

		for (final Class<?> model : annotated) {
			final XmlRootElement jsonRootName = model.getAnnotation(XmlRootElement.class);
			if (jsonRootName != null) {
				final String annotatedName = jsonRootName.name();
				if (!model.getSimpleName().equals("ErrorResponse")) {
					Assertions.assertEquals(model.getSimpleName(),
							Character.toUpperCase(annotatedName.charAt(0)) + annotatedName.substring(1));
				}
			}
		}
	}
}
