package satori.i18n.locale;

import satori.config.Configuration;

public final class LocaleResolverFactory {

    public static final String LOCALE_RESOLVER_PARAM = "satori.locale.resolver";

    public static LocaleResolver newResolver(Configuration config) {

        return newResolver(config.get(LOCALE_RESOLVER_PARAM, DefaultResolver.class.getName()));

    }

    public static LocaleResolver newResolver(String className) {

        try {
            return (LocaleResolver) Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private LocaleResolverFactory() {

    }
}
