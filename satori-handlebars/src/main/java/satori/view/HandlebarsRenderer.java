package satori.view;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.Map;
import java.util.ServiceLoader;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import satori.config.Configuration;
import satori.hbs.helpers.FormHelpers;
import satori.hbs.helpers.NavigationHelpers;
import satori.hbs.helpers.UriHelpers;
import satori.hbs.hooks.HandlebarsHook;
import satori.hbs.hooks.RequestHook;
import satori.i18n.Translation;
import satori.i18n.locale.LocaleSource;
import satori.security.CsrfHelper;
import satori.utils.PathUtils;
import satori.view.providers.ViewContext;
import satori.view.providers.ViewRenderer;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Context.Builder;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.TemplateLoader;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class HandlebarsRenderer implements ViewRenderer {

    private static final Logger log = LoggerFactory.getLogger(HandlebarsRenderer.class);

    private static final ServiceLoader<HandlebarsHook> handlebarsHooks = ServiceLoader.load(HandlebarsHook.class);

    private static final ServiceLoader<RequestHook> requestHooks = ServiceLoader.load(RequestHook.class);

    private final LoadingCache<String, Template> templates;

    private final Handlebars handlebars;

    private final TemplateLoader templateLoader;

    private final HandlebarsConfiguration config;

    public HandlebarsRenderer(Configuration cfg, ServletContext context) {

        this.config = new HandlebarsConfiguration(cfg);

        logStatus();

        this.templates = buildCache();
        this.templateLoader = new ClassPathTemplateLoader(config.templatesPath);
        this.handlebars = new Handlebars(templateLoader);

        Translation.setDefaultBaseName(config.defaultBundleBase);

        UriHelpers.register(handlebars, context);
        FormHelpers.register(handlebars, context);
        NavigationHelpers.register(handlebars, context);

        onInstance();

    }

    @Override
    public boolean accepts(SatoriView view) {

        String resolved = PathUtils.absolutize(view.getTemplateName());
        return isTemplateAvailable(resolved);

    }

    @Override
    public void render(SatoriView view, ViewContext context, OutputStream out) throws IOException {

        final OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");

        try {

            final Template template = templates.get(view.getTemplateName());

            Builder model = prepareModel(view, context);

            onModel(model);

            template.apply(model.build(), writer);

        } catch (Exception ex) {

            log.error(ex.getMessage(), ex);
            error(ex, writer);

        } finally {
            writer.flush();
        }

    }

    private LoadingCache<String, Template> buildCache() {

        CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.from(config.cacheBuilderSpec);

        return cacheBuilder.build(new CacheLoader<String, Template>() {

            public Template load(String path) {
                try {
                    // On load compiles the template
                    return (config.hasDelimiters) ? handlebars.compile(URI.create(path), config.startDelimiter,
                            config.endDelimiter) : handlebars.compile(URI.create(path));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });

    }

    private void error(Exception ex, OutputStreamWriter writer) throws IOException {

        StringBuilder html = new StringBuilder();
        html.append("<html><head>")
                .append("<title>Error</title>")
                .append("<style>body{background-color: #B0ADAE;margin: 0;padding: 0;font: normal 14px/20px helvetica,arial,sans-serif;}")
                .append("h1{background-color: #F3F3E9;margin: 0;padding: 20px 10px;border-bottom: 1px solid #777} ")
                .append("pre{white-space: pre-wrap;padding: 30px 15px;background-color: white;margin: 0;}</style>")
                .append("</head><body><h1>Handlebars Processor Error</h1><pre>").append(ex.getMessage())
                .append("</pre></body></html>");
        writer.append(html.toString());

    }

    private boolean isTemplateAvailable(String path) {

        return (templates.getIfPresent(path) != null || HandlebarsRenderer.class.getResourceAsStream(templateLoader
                .resolve(path)) != null);

    }

    private void logStatus() {

        log.info(HandlebarsRenderer.class.getSimpleName());
        log.info(StringUtils.repeat('=', HandlebarsRenderer.class.getSimpleName().length()));
        config.logStatus(log);
        log.info("~");

    }

    private void onInstance() {

        for (HandlebarsHook hh : handlebarsHooks) {
            hh.onHandlebarsCreated(handlebars);
        }

    }

    private void onModel(Builder model) {

        for (RequestHook rh : requestHooks) {
            rh.onModel(model);
        }

    }

    @SuppressWarnings("unchecked")
    private Builder prepareModel(SatoriView view, ViewContext context) {

        Object m = view.getModel();

        if (m == null) {
            return Context.newBuilder(null);
        }

        Map<String, Object> map = null;
        Object vm;

        if (Map.class.isAssignableFrom(m.getClass())) {
            map = (Map<String, Object>) m;
            vm = map.remove("model");
        } else {
            vm = m;
        }

        Builder model = vm != null && Builder.class.isAssignableFrom(vm.getClass()) ? (Builder) vm : Context.newBuilder(vm);
        
        model.combine("locale", LocaleSource.getCurrentLocale())
             .combine("securityContext", context.securityContext)
             .combine("uriInfo", context.uriInfo)
             .combine(CsrfHelper.CSRF_TOKEN_PARAM, view.getCsrfToken())
             .combine("nil", null);

        if (!(map == null || map.isEmpty())) {
            model.combine(map);
        }

        return model;

    }

}
