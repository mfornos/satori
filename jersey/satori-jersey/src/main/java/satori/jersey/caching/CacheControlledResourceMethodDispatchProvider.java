package satori.jersey.caching;

import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.spi.container.ResourceMethodDispatchProvider;
import com.sun.jersey.spi.dispatch.RequestDispatcher;

import java.util.concurrent.TimeUnit;

public class CacheControlledResourceMethodDispatchProvider implements ResourceMethodDispatchProvider {
    private static final int ONE_YEAR_IN_SECONDS = (int) TimeUnit.DAYS.toSeconds(365);

    private final ResourceMethodDispatchProvider provider;

    public CacheControlledResourceMethodDispatchProvider(ResourceMethodDispatchProvider provider) {
        this.provider = provider;
    }

    @Override
    public RequestDispatcher create(AbstractResourceMethod abstractResourceMethod) {
        final RequestDispatcher dispatcher = provider.create(abstractResourceMethod);
        final CacheControl control = abstractResourceMethod.getAnnotation(CacheControl.class);
        if (control != null) {
            final javax.ws.rs.core.CacheControl cacheControl = new javax.ws.rs.core.CacheControl();
            cacheControl.setPrivate(control.isPrivate());
            cacheControl.setNoCache(control.noCache());
            cacheControl.setNoStore(control.noStore());
            cacheControl.setNoTransform(control.noTransform());
            cacheControl.setMustRevalidate(control.mustRevalidate());
            cacheControl.setProxyRevalidate(control.proxyRevalidate());
            cacheControl.setMaxAge((int) control.maxAgeUnit().toSeconds(control.maxAge()));
            cacheControl.setSMaxAge((int) control.sharedMaxAgeUnit().toSeconds(control.sharedMaxAge()));
            if (control.immutable()) {
                cacheControl.setMaxAge(ONE_YEAR_IN_SECONDS);
            }
            return new CacheControlledRequestDispatcher(dispatcher, cacheControl);
        }
        return dispatcher;
    }
}
