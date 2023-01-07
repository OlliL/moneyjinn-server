package org.laladev.moneyjinn.converter;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import org.laladev.moneyjinn.model.AbstractEntityID;
import org.mapstruct.TargetType;

public abstract class AbstractEntityIdMapper<A extends AbstractEntityID<B>, B extends Serializable> {

  public B mapAToB(final A a) {
    return a == null ? null : a.getId();
  }

  public A mapBToA(final B b, @TargetType final Class<A> entityClass)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException {
    A a = null;
    if (b != null) {
      a = entityClass.getConstructor(b.getClass()).newInstance(b);
    }
    return a;
  }
}
