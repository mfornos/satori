package satori.jersey.spi.inject;

import java.lang.reflect.ParameterizedType;

import javax.ws.rs.core.Context;

import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

public abstract class AbstractInjectableProvider<E> implements InjectableProvider<Context, Parameter>, Injectable<E> {

    private Class<E> type;

    @SuppressWarnings("unchecked")
    public AbstractInjectableProvider() {
        this.type = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public AbstractInjectableProvider(Class<E> type) {
        this.type = type;
    }

    @Override
    public Injectable<E> getInjectable(ComponentContext ic, Context a, Parameter c) {
        Class<?> parameterClass = c.getParameterClass();
        if (parameterClass.equals(type)) {
            return this;
        }
        return null;
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

}
