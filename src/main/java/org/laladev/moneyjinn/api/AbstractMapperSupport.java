package org.laladev.moneyjinn.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.exception.TechnicalException;

public abstract class AbstractMapperSupport {
	private static final Log LOG = LogFactory.getLog(AbstractMapperSupport.class);

	private static final String MAPPER_UNDEFINED = "Mapper undefined!";

	// -----returnTyp---------parameter- Method of Object to execute
	Map<Class<?>, Map<Class<?>, Method>> mapperMethods = new HashMap<>();
	// -----returnTyp---------parameter- Object to execute
	Map<Class<?>, Map<Class<?>, IMapper<?, ?>>> mapperClasses = new HashMap<>();

	protected AbstractMapperSupport() {
		this.addBeanMapper();
	}

	protected abstract void addBeanMapper();

	protected void registerBeanMapper(final IMapper<?, ?> mapper) {
		final List<Method> iMapperMethods = Arrays.asList(IMapper.class.getDeclaredMethods());

		for (final Method mapperMethod : mapper.getClass().getDeclaredMethods()) {
			for (final Method method : iMapperMethods) {
				// in mapper.getClass().getDeclaredMethods() the class-methods as well as the
				// interface-methods are returned. We are only interested in the implemented
				// methods, so filter the methods from the interface which differ in their return
				// types (Generic vs. concrete class)
				if (method.getName().equals(mapperMethod.getName())
						&& !method.getReturnType().equals(mapperMethod.getReturnType())) {
					final Class<?> returnType = mapperMethod.getReturnType();
					final Class<?> parameter = mapperMethod.getParameters()[0].getType();

					this.putToMapperMethods(mapperMethod, returnType, parameter);
					this.putToMapperClasses(mapper, returnType, parameter);
				}
			}
		}
	}

	private void putToMapperClasses(final IMapper<?, ?> mapper, final Class<?> returnType, final Class<?> parameter) {
		Map<Class<?>, IMapper<?, ?>> classMap = this.mapperClasses.get(returnType);
		if (classMap == null) {
			classMap = new HashMap<>();
		}
		classMap.put(parameter, mapper);
		this.mapperClasses.put(returnType, classMap);
	}

	private void putToMapperMethods(final Method m, final Class<?> returnType, final Class<?> parameter) {
		Map<Class<?>, Method> methodMap = this.mapperMethods.get(returnType);
		if (methodMap == null) {
			methodMap = new HashMap<>();
		}
		methodMap.put(parameter, m);
		this.mapperMethods.put(returnType, methodMap);
	}

	protected <T> T map(final Object args, final Class<T> clazz) {
		if (args != null) {
			Method method = null;
			IMapper<?, ?> obj = null;
			try {
				method = this.mapperMethods.get(clazz).get(args.getClass());
				obj = this.mapperClasses.get(clazz).get(args.getClass());
			} catch (final NullPointerException e) {
				LOG.error(e);
				throw new TechnicalException(MAPPER_UNDEFINED, ErrorCode.MAPPER_UNDEFINED);
			}
			if (method == null) {
				throw new TechnicalException(MAPPER_UNDEFINED, ErrorCode.MAPPER_UNDEFINED);
			}
			try {
				final Object result = method.invoke(obj, args);
				if (clazz.isInstance(result)) {
					return clazz.cast(result);
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				LOG.error(e);
				throw new TechnicalException(MAPPER_UNDEFINED, ErrorCode.MAPPER_UNDEFINED);
			}
		}
		return null;
	}

	protected <T> List<T> mapList(final List<?> args, final Class<T> clazz) {
		return args.stream().map(element -> this.map(element, clazz)).collect(Collectors.toCollection(ArrayList::new));
	}
}
