package org.laladev.moneyjinn.core.mapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.error.MoneyjinnException;

public abstract class AbstractMapperSupport {
	private static final Logger LOG = Logger.getLogger(AbstractMapperSupport.class.getName());

	private static final String MAPPER_UNDEFINED = "Mapper undefined!";

	// -----returnTyp---------parameter- Method of Object to execute
	Map<Class<?>, Map<Class<?>, Method>> mapperMethods = new HashMap<Class<?>, Map<Class<?>, Method>>();
	// -----returnTyp---------parameter- Object to execute
	Map<Class<?>, Map<Class<?>, IMapper<?, ?>>> mapperClasses = new HashMap<Class<?>, Map<Class<?>, IMapper<?, ?>>>();

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
					final Class<?> parameter = mapperMethod.getParameterTypes()[0];

					this.putToMapperMethods(mapperMethod, returnType, parameter);
					this.putToMapperClasses(mapper, returnType, parameter);
				}
			}
		}
	}

	private void putToMapperClasses(final IMapper<?, ?> mapper, final Class<?> returnType, final Class<?> parameter) {
		Map<Class<?>, IMapper<?, ?>> classMap = this.mapperClasses.get(returnType);
		if (classMap == null) {
			classMap = new HashMap<Class<?>, IMapper<?, ?>>();
		}
		classMap.put(parameter, mapper);
		this.mapperClasses.put(returnType, classMap);
	}

	private void putToMapperMethods(final Method m, final Class<?> returnType, final Class<?> parameter) {
		Map<Class<?>, Method> methodMap = this.mapperMethods.get(returnType);
		if (methodMap == null) {
			methodMap = new HashMap<Class<?>, Method>();
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
				LOG.log(Level.SEVERE, e.toString() + "on mapping " + args + " to " + clazz);
				throw new MoneyjinnException(MAPPER_UNDEFINED, ErrorCode.MAPPER_UNDEFINED);
			}
			if (method == null) {
				throw new MoneyjinnException(MAPPER_UNDEFINED, ErrorCode.MAPPER_UNDEFINED);
			}
			try {
				final Object result = method.invoke(obj, args);
				if (clazz.isInstance(result)) {
					return clazz.cast(result);
				}
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
				LOG.log(Level.SEVERE, e.toString());
				throw new MoneyjinnException(MAPPER_UNDEFINED, ErrorCode.MAPPER_UNDEFINED);
			} catch (final IllegalArgumentException e) {
				e.printStackTrace();
				LOG.log(Level.SEVERE, e.toString());
				throw new MoneyjinnException(MAPPER_UNDEFINED, ErrorCode.MAPPER_UNDEFINED);
			} catch (final InvocationTargetException e) {
				e.printStackTrace();
				LOG.log(Level.SEVERE, e.toString());
				throw new MoneyjinnException(MAPPER_UNDEFINED, ErrorCode.MAPPER_UNDEFINED);
			}
		}
		return null;
	}

	protected <T> List<T> mapList(final List<?> args, final Class<T> clazz) {
		final List<T> resultList = new ArrayList<T>();
		if (args != null) {
			for (final Object object : args) {
				resultList.add(this.map(object, clazz));
			}
		}
		return resultList;
	}
}
