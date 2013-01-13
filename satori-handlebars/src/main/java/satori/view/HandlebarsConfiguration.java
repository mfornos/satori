package satori.view;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.google.common.cache.CacheBuilderSpec;

import satori.config.Configuration;
import satori.i18n.Translation;
import satori.i18n.locale.DefaultResolver;
import satori.i18n.locale.LocaleResolverFactory;

public class HandlebarsConfiguration {

    public static final String TEMPLATES_PATH_PARAM = "handlebars.templates.path";

    public static final String ENVIRONMENT_PARAM = "environment";

    public static final String DELIMITER_START_PARAM = "handlebars.delimiter.start";

    public static final String DELIMITER_END_PARAM = "handlebars.delimiter.end";

    public static final String EXPOSE_PSEUDO_VARS_PARAM = "handlebars.expose.pseudo.vars";

    public static final String DEFAULT_BUNDLE_BASE_PARAM = "handlebars.default.bundle.base";

    public static final String CACHE_BUILDER_SPEC_PARAM = "handlebars.cache.spec";

    private static final String DEFAULT_CACHE_SPEC = "expireAfterWrite=1s";

    protected final String templatesPath;

    protected final String environment;

    protected final boolean hasDelimiters;

    protected final String startDelimiter;

    protected final String endDelimiter;

    protected final boolean isExposePseudoVariables;

    protected final String defaultBundleBase;

    protected final String localeResolverClassName;

    protected final CacheBuilderSpec cacheBuilderSpec;

    public HandlebarsConfiguration(Configuration config) {

        this.environment = config.get(ENVIRONMENT_PARAM, "development");
        Configuration envCfg = config.has(environment) ? config.child(environment) : config;

        this.templatesPath = getTemplatesPath((String) envCfg.get(TEMPLATES_PATH_PARAM));
        this.startDelimiter = envCfg.get(DELIMITER_START_PARAM);
        this.endDelimiter = envCfg.get(DELIMITER_END_PARAM);
        this.hasDelimiters = StringUtils.isNotBlank(startDelimiter) && StringUtils.isNotBlank(endDelimiter);
        this.isExposePseudoVariables = envCfg.get(EXPOSE_PSEUDO_VARS_PARAM, true);
        this.defaultBundleBase = envCfg.get(DEFAULT_BUNDLE_BASE_PARAM, Translation.DEFAULT_BUNDLE_BASE_NAME);
        this.localeResolverClassName = envCfg.get(LocaleResolverFactory.LOCALE_RESOLVER_PARAM,
                DefaultResolver.class.getName());
        this.cacheBuilderSpec = CacheBuilderSpec.parse(envCfg.get(CACHE_BUILDER_SPEC_PARAM, DEFAULT_CACHE_SPEC));

    }

    public void logStatus(Logger log) {

        log.info("Environment: {}", environment);
        log.info("Cache spec: '{}'", cacheBuilderSpec);
        log.info("Views path: '{}'", templatesPath);
        if (hasDelimiters)
            log.info("Delimiters: '{}' '{}'", startDelimiter, endDelimiter);
        log.info("Expose pseudo vars: '{}'", isExposePseudoVariables);
        log.info("Default base bundle: '{}'", defaultBundleBase);
        log.info("Locale Resolver: {}", localeResolverClassName);

    }

    private String getTemplatesPath(String path) {

        return StringUtils.isBlank(path) ? "/views" : path.replaceAll("/$", "");

    }

}
