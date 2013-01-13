package satori.jersey.spi.filter;

import java.util.Collections;
import java.util.List;

import satori.security.CsrfGuard;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

public class CsrfFilterFactory implements ResourceFilterFactory {

    @Override
    public List<ResourceFilter> create(AbstractMethod am) {

        if (am.getAnnotation(CsrfGuard.class) != null) {
            return Collections.<ResourceFilter> singletonList(new CsrfFilter());
        }

        return null;
        
    }

}
