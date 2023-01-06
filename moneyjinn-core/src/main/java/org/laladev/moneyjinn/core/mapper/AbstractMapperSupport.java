//
// Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//

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
  private final Map<Class<?>, Map<Class<?>, Method>> mapperMethods = new HashMap<>();
  // -----returnTyp---------parameter- Object to execute
  private final Map<Class<?>, Map<Class<?>, IMapper<?, ?>>> mapperClasses = new HashMap<>();

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

  private void putToMapperClasses(final IMapper<?, ?> mapper, final Class<?> returnType,
      final Class<?> parameter) {
    Map<Class<?>, IMapper<?, ?>> classMap = this.mapperClasses.get(returnType);
    if (classMap == null) {
      classMap = new HashMap<>();
    }
    classMap.put(parameter, mapper);
    this.mapperClasses.put(returnType, classMap);
  }

  private void putToMapperMethods(final Method m, final Class<?> returnType,
      final Class<?> parameter) {
    Map<Class<?>, Method> methodMap = this.mapperMethods.get(returnType);
    if (methodMap == null) {
      methodMap = new HashMap<>();
    }
    methodMap.put(parameter, m);
    this.mapperMethods.put(returnType, methodMap);
  }

  protected <T> T map(final Object args, final Class<T> clazz) {
    if (args != null) {
      Method method;
      IMapper<?, ?> obj;
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
        if (e.getTargetException() instanceof MoneyjinnException) {
          throw (MoneyjinnException) e.getCause();
        }
        throw new MoneyjinnException(MAPPER_UNDEFINED, ErrorCode.MAPPER_UNDEFINED);
      }
    }
    return null;
  }

  protected <T> List<T> mapList(final List<?> args, final Class<T> clazz) {
    final List<T> resultList = new ArrayList<>();
    if (args != null) {
      for (final Object object : args) {
        resultList.add(this.map(object, clazz));
      }
    }
    return resultList;
  }
}
