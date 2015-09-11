package org.laladev.moneyjinn.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.exception.TechnicalException;

public abstract class AbstractMapperSupport {
	// -----returnTyp---------parameter- Method of Object to execute
	HashMap<Class<?>, HashMap<Class<?>, Method>> mapperMethods = new HashMap<>();
	// -----returnTyp---------parameter- Object to execute
	HashMap<Class<?>, HashMap<Class<?>, IMapper<?, ?>>> mapperClasses = new HashMap<>();

	protected AbstractMapperSupport() {
		this.addBeanMapper();
	}

	protected abstract void addBeanMapper();

	protected void registerBeanMapper(final IMapper<?, ?> mapper) {
		final List<Method> iMapperMethods = Arrays.asList(IMapper.class.getDeclaredMethods());

		for (final Method m : mapper.getClass().getDeclaredMethods()) {
			for (final Method method : iMapperMethods) {
				if (method.getName().equals(m.getName()) && !method.getReturnType().equals(m.getReturnType())) {
					final Class<?> returnType = m.getReturnType();
					final Class<?> parameter = m.getParameters()[0].getType();

					HashMap<Class<?>, Method> methodMap = this.mapperMethods.get(returnType);
					if (methodMap == null) {
						methodMap = new HashMap<>();
					}
					methodMap.put(parameter, m);
					this.mapperMethods.put(returnType, methodMap);

					HashMap<Class<?>, IMapper<?, ?>> classMap = this.mapperClasses.get(returnType);
					if (classMap == null) {
						classMap = new HashMap<>();
					}
					classMap.put(parameter, mapper);
					this.mapperClasses.put(returnType, classMap);
				}
			}
		}
	}

	protected <T> T map(final Object args, final Class<T> clazz) {
		if (args != null) {
			Method method = null;
			IMapper<?, ?> obj = null;
			try {
				method = this.mapperMethods.get(clazz).get(args.getClass());
				obj = this.mapperClasses.get(clazz).get(args.getClass());
			} catch (final NullPointerException e) {
				throw new TechnicalException("Mapper undefined!", ErrorCode.MAPPER_UNDEFINED);
			}
			if (method == null) {
				throw new TechnicalException("Mapper undefined!", ErrorCode.MAPPER_UNDEFINED);
			}
			try {
				final Object result = method.invoke(obj, args);
				if (clazz.isInstance(result)) {
					return clazz.cast(result);
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				throw new TechnicalException("Mapper undefined!", ErrorCode.MAPPER_UNDEFINED);
			}
		}
		return null;
	}

	protected <T> List<T> mapList(final List<?> args, final Class<T> clazz) {
		return args.stream().map(element -> this.map(element, clazz)).collect(Collectors.toCollection(ArrayList::new));
	}

// @formatter:off
//	private Method findMapperMethod(final Object args, final Class<?> clazz) {
//		Method method = null;
//		try {
//			method = this.mapperMethods.get(clazz).get(args.getClass());
//		} catch (final NullPointerException e) {
//			throw new TechnicalException("Mapper undefined!", ErrorCode.MAPPER_UNDEFINED);
//		}
//		if (method == null) {
//			throw new TechnicalException("Mapper undefined!", ErrorCode.MAPPER_UNDEFINED);
//		}
//		return method;
//	}
//
//	private IMapper<?, ?> findMapperObj(final Object args, final Class<?> clazz) {
//		IMapper<?, ?> obj = null;
//		try {
//			obj = this.mapperClasses.get(clazz).get(args.getClass());
//		} catch (final NullPointerException e) {
//			throw new TechnicalException("Mapper undefined!", ErrorCode.MAPPER_UNDEFINED);
//		}
//		if (obj == null) {
//			throw new TechnicalException("Mapper undefined!", ErrorCode.MAPPER_UNDEFINED);
//		}
//		return obj;
//	}
//
//	private <T> T doMap(final IMapper<?, ?> obj, final Method method, final Object args, final Class<T> clazz) {
//		try {
//			final Object result = method.invoke(obj, args);
//			if (clazz.isInstance(result)) {
//				return clazz.cast(result);
//			}
//		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//			throw new TechnicalException("Mapper undefined!", ErrorCode.MAPPER_UNDEFINED);
//		}
//		return null;
//	}
//
//	protected <T> T map(final Object args, final Class<T> clazz) {
//		if (args != null) {
//			final Method method = this.findMapperMethod(args, clazz);
//			final IMapper<?, ?> obj = this.findMapperObj(args, clazz);
//			return this.doMap(obj, method, args, clazz);
//		}
//		return null;
//	}
//
//	protected <T> List<T> mapList(final List<?> args, final Class<T> clazz) {
//		if (args != null && !args.isEmpty()) {
//			final Object arg = args.iterator().next();
//			final Method method = this.findMapperMethod(arg, clazz);
//			final IMapper<?, ?> obj = this.findMapperObj(arg, clazz);
//			return args.stream().map(element -> this.doMap(obj, method, element, clazz))
//					.collect(Collectors.toCollection(ArrayList::new));
//		}
//		return null;
//	}
// @formatter:on

}
