package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.AbstractEntityID;
import org.mapstruct.TargetType;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public interface EntityIdMapper<A extends AbstractEntityID<B>, B extends Serializable> {

    default B mapAToB(final A a) {
        return a == null ? null : a.getId();
    }

    default A mapBToA(final B b, @TargetType final Class<A> entityClass)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        A a = null;
        if (b != null) {
            a = entityClass.getConstructor(b.getClass()).newInstance(b);
        }
        return a;
    }
}
