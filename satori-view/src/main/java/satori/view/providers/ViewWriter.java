package satori.view.providers;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import satori.config.Configuration;
import satori.i18n.locale.DefaultResolver;
import satori.i18n.locale.LocaleResolver;
import satori.i18n.locale.LocaleResolverFactory;
import satori.i18n.locale.LocaleSource;
import satori.view.SatoriView;

/**
 * JAX-RS {@link SatoriView} writer for HTML and XHTML media types.
 * 
 */
@Provider
@Produces({ MediaType.TEXT_HTML, MediaType.APPLICATION_XHTML_XML })
public class ViewWriter implements MessageBodyWriter<SatoriView> {

    private static final List<ViewRenderer> renderers = new ArrayList<ViewRenderer>();

    @Context private HttpServletRequest request;

    @Context private HttpServletResponse response;

    @Context private UriInfo uriInfo;

    @Context private SecurityContext securityContext;

    @Context private HttpHeaders headers;

    private final Configuration config;

    private final LocaleResolver resolver;

    public ViewWriter(@Context ServletContext servletContext) throws Exception {

        this.config = Configuration.satoriYaml();
        this.resolver = LocaleResolverFactory.newResolver(config.get(LocaleResolverFactory.LOCALE_RESOLVER_PARAM,
                DefaultResolver.class.getName()));

        createRenderers(servletContext);

    }

    @Override
    public long getSize(SatoriView view, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {

        return -1;

    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {

        return SatoriView.class.isAssignableFrom(type);

    }

    @Override
    public void writeTo(SatoriView view, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream out) throws IOException,
            WebApplicationException {

        LocaleSource.update(resolver.resolveLocale(headers, request));

        for (ViewRenderer renderer : renderers) {
            if (renderer.accepts(view)) {
                renderer.render(view, createViewContext(annotations), out);
                break;
            }
        }

    }

    private void createRenderers(ServletContext servletContext) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        List<String> rendererClasses = config.get("renderers");

        if (rendererClasses == null) {
            throw new RuntimeException("No renderers found in 'satori.yml'. Please, configure at least one renderer.");
        }

        for (String renderer : rendererClasses) {
            Class<?> rendererClass = Class.forName(renderer);
            Constructor<?> ctor = rendererClass.getConstructor(Configuration.class, ServletContext.class);
            renderers.add((ViewRenderer) ctor.newInstance(config, servletContext));
        }

    }

    private ViewContext createViewContext(Annotation[] annotations) {

        return new ViewContext(request, response, uriInfo, securityContext, headers, annotations);

    }

}
