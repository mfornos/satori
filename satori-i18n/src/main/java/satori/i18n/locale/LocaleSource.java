package satori.i18n.locale;

import java.util.Locale;
import java.util.concurrent.Callable;

public class LocaleSource {

    private final static ThreadLocal<Locale> locale = new ThreadLocal<Locale>();

    public static Locale getCurrentLocale() {
        Locale cl = locale.get();
        return (cl == null) ? Locale.getDefault() : cl;
    }

    public static void update(Callable<Locale> callable) {
        try {
            locale.set(callable.call());
        } catch (Exception e) {
            locale.set(Locale.getDefault());
        }
    }

    public static void update(Locale loc) {
        locale.set(loc);
    }

}
