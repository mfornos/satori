package satori.i18n;

import java.text.Format;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import satori.i18n.locale.LocaleSource;
import satori.i18n.standard.UTF8Control;

public class Translation {

    private static final Logger log = LoggerFactory.getLogger(Translation.class);

    public static final String DEFAULT_BUNDLE_BASE_NAME = "i18n.messages";

    public static final String DEFAULT_FORMATS_BUNDLE_BASE_NAME = "i18n.formats";

    private static String defaultBaseName = DEFAULT_BUNDLE_BASE_NAME;

    private static final Map<Locale, Translation> instances = new HashMap<Locale, Translation>();

    private static final ThreadLocal<Map<Locale, Translation>> localInstances = new ThreadLocal<Map<Locale, Translation>>();

    public static String getDefaultBaseName() {

        return defaultBaseName;

    }

    public static Format getFormat(String name) {

        return getInstance().getFormat(LocaleSource.getCurrentLocale(), name);

    }

    public static Translation getInstance() {

        return getInstance(LocaleSource.getCurrentLocale(), defaultBaseName);

    }

    public static Translation getInstance(Locale locale, String baseName) {

        Map<Locale, Translation> lc = getLocalCache();

        if (lc.containsKey(locale) && lc.get(locale).hasBundle(baseName)) {

            return lc.get(locale);

        } else {

            synchronized (instances) {
                if (lacksLocale(locale)) {
                    instances.put(locale, new Translation(locale, baseName));
                } else if (!instances.get(locale).hasBundle(baseName)) {
                    Translation.setResourceBundle(locale, baseName);
                }
            }

            Translation instance = instances.get(locale);
            lc.put(locale, instance);
            return instance;

        }

    }

    public static boolean hasLocale(Locale locale) {

        return instances.containsKey(locale);

    }

    public static boolean lacksLocale(Locale locale) {

        return !hasLocale(locale);

    }

    public static ResourceBundle loadBundle(Locale locale) {

        return loadBundle(locale, defaultBaseName);

    }

    public static ResourceBundle loadBundle(Locale locale, String baseName) {

        try {
            return ResourceBundle.getBundle(baseName, locale, new UTF8Control());
        } catch (Exception ex) {
            log.warn(ex.getMessage());
            return null;
        }

    }

    public static void loadFormat(Locale locale, String name, Format format) {

        Translation.formats.put(locale, name, format);

    }

    public static void loadFormats(Locale locale, ResourceBundle resourceBundle) {

        Translation.formats.loadFormats(locale, resourceBundle);

    }

    public static void reload() {

        ResourceBundle.clearCache();

    }

    public static void resetFormats() {

        Translation.formats.reset();

    }

    public static Translation setResourceBundle(final Locale locale, final String baseName) {

        return setResourceBundle(locale, baseName, loadBundle(locale, baseName));

    }

    public static Translation setResourceBundle(final Locale locale, final String baseName, final ResourceBundle bundle) {
        synchronized (instances) {
            Translation instance = new Translation(locale, baseName, bundle);
            instances.put(locale, instance);
            getLocalCache().put(locale, instance);
            return instances.get(locale);
        }
    }

    public static String translate(final Locale locale, final String baseText) {

        return translate(locale, defaultBaseName, baseText);

    }

    public static String translate(final Locale locale, final String baseName, final String baseText) {

        if (StringUtils.isBlank(baseText)) {
            return baseText;
        }

        Translation translationUtils = getInstance(locale, baseName);
        if (translationUtils == null || translationUtils.resourceBundles.isEmpty()) {
            return baseText;
        }

        try {
            ResourceBundle resourceBundle = translationUtils.resourceBundles.get(baseName);
            return (resourceBundle == null) ? baseText : resourceBundle.getString(baseText);
        } catch (MissingResourceException e) {
            return baseText;
        }

    }

    public static String translate(final String baseText) {

        return translate(LocaleSource.getCurrentLocale(), defaultBaseName, baseText);

    }

    public static String translate(String baseName, String baseText) {

        return translate(LocaleSource.getCurrentLocale(), baseName, baseText);

    }

    private static Map<Locale, Translation> getLocalCache() {
        if (localInstances.get() == null) {
            localInstances.set(new HashMap<Locale, Translation>());
        }

        return localInstances.get();
    }

    private final Map<String, ResourceBundle> resourceBundles;

    private static final Formats formats = new Formats();

    public static final Locale UNIVERSAL_LOCALE = new Locale("", "");

    public static Format getUniversalFormat(String name) {
        return getInstance().getFormat(UNIVERSAL_LOCALE, name);
    }

    public static void setDefaultBaseName(String baseName) {

        defaultBaseName = baseName;

    }

    public static Translation setResourceBundle(final Locale locale, final ResourceBundle bundle) {

        return setResourceBundle(locale, defaultBaseName, bundle);

    }

    private final Locale locale;

    public Translation(Locale locale, String baseName) {

        this(locale, baseName, loadBundle(locale, baseName));

    }

    protected Translation() {

        this(Locale.getDefault(), defaultBaseName, loadBundle(Locale.getDefault()));

    }

    protected Translation(Locale locale) {

        this(locale, defaultBaseName, loadBundle(locale));

    }

    protected Translation(final Locale locale, final String baseName, final ResourceBundle bundle) {

        this.locale = locale;
        this.resourceBundles = new HashMap<String, ResourceBundle>();
        resourceBundles.put(baseName, bundle);
        if (formats.lacksLocale(locale)) {
            loadFormats(locale, loadBundle(locale, DEFAULT_FORMATS_BUNDLE_BASE_NAME));
        }

    }

    public Format getFormat(Locale locale, String name) {

        return formats.get(locale, name);

    }

    public Locale getLocale() {

        return locale;

    }

    private boolean hasBundle(String baseName) {

        return resourceBundles.containsKey(baseName);

    }

}
