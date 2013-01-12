package satori.i18n;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import satori.i18n.standard.ISO8601Format;

public class Formats {

    private static abstract class FormatLoader {
        private String key;

        public FormatLoader(String key) {
            this.key = key;
        }

        abstract protected Format defaultFormat();

        protected Format newFormat(String format) {
            return defaultFormat();
        }
    }

    public static final String DATE = "date";

    public static final String DATE_TIME = "date.time";

    public static final String TIME = "time";

    public static final String DECIMAL = "decimal";

    public static final String PERCENT = "percent";

    public static final String CURRENCY = "currency";

    public static final String ISO8601_DATE = "iso8601.date";

    private static final Map<Locale, Map<String, Format>> cache = new HashMap<Locale, Map<String, Format>>();

    private static final ThreadLocal<Map<Locale, Map<String, Format>>> localCache = new ThreadLocal<Map<Locale, Map<String, Format>>>();

    public Formats() {

    }

    public Format get(Locale locale, String name) {

        Map<Locale, Map<String, Format>> lc = localCache.get();
        if (lc == null) {
            localCache.set(new HashMap<Locale, Map<String, Format>>());
            lc = localCache.get();
        }

        if (lc.containsKey(locale)) {
            Map<String, Format> tmp = lc.get(locale);
            return tmp.containsKey(name) ? tmp.get(name) : localFromCached(locale, name, lc);
        } else {
            lc.put(locale, new HashMap<String, Format>());
            return localFromCached(locale, name, lc);
        }

    }

    public boolean lacksLocale(Locale locale) {
        return !cache.containsKey(locale);
    }

    public void loadFormats(final Locale locale, final ResourceBundle bundle) {

        load(locale, bundle, new FormatLoader(DATE) {
            protected Format defaultFormat() {
                return newFormat("MM/dd/yyyy");
            }

            protected Format newFormat(String format) {
                return new SimpleDateFormat(format);
            }
        });

        load(locale, bundle, new FormatLoader(DATE_TIME) {
            protected Format defaultFormat() {
                return newFormat("MM/dd/yyyy kk:mm");
            }

            protected Format newFormat(String format) {
                return new SimpleDateFormat(format);
            }
        });

        load(locale, bundle, new FormatLoader(TIME) {
            protected Format defaultFormat() {
                return newFormat("kk:mm");
            }

            protected Format newFormat(String format) {
                return new SimpleDateFormat(format);
            }
        });

        load(locale, bundle, new FormatLoader(DECIMAL) {
            protected Format defaultFormat() {
                return DecimalFormat.getInstance(locale);
            }

            protected Format newFormat(String format) {
                return new DecimalFormat(format);
            }
        });

        load(locale, bundle, new FormatLoader(PERCENT) {
            protected Format defaultFormat() {
                return DecimalFormat.getPercentInstance(locale);
            }

            protected Format newFormat(String format) {
                return new DecimalFormat(format);
            }
        });

        load(locale, bundle, new FormatLoader(CURRENCY) {
            protected Format defaultFormat() {
                return DecimalFormat.getCurrencyInstance(locale);
            }

            protected Format newFormat(String format) {
                return new DecimalFormat(format);
            }
        });

        // Universal formats

        load(Translation.UNIVERSAL_LOCALE, bundle, new FormatLoader(ISO8601_DATE) {
            protected Format defaultFormat() {
                return new ISO8601Format();
            }
        });

    }

    public void put(Locale locale, String key, Format format) {
        synchronized (cache) {
            if (!cache.containsKey(locale)) {
                cache.put(locale, new HashMap<String, Format>());
            }
            cache.get(locale).put(key, format);
        }
    }

    public void reset() {

        cache.clear();

    }

    private Format getCached(Locale locale, String name) {
        if (cache.containsKey(locale)) {
            return cache.get(locale).get(name);
        }
        return null;
    }

    private void load(Locale locale, ResourceBundle bundle, FormatLoader loader) {
        if (bundle != null && bundle.containsKey(loader.key)) {
            put(locale, loader.key, loader.newFormat(bundle.getString(loader.key)));
        } else {
            put(locale, loader.key, loader.defaultFormat());
        }
    }

    private Format localFromCached(Locale locale, String name, Map<Locale, Map<String, Format>> lc) {
        Format tmp = getCached(locale, name);
        if (tmp != null) {
            Format f = (Format) tmp.clone();
            lc.get(locale).put(name, f);
            return f;
        }
        return null;
    }

}
