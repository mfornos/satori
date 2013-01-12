package satori.i18n.locale;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.HttpHeaders;

public class DefaultResolver implements LocaleResolver {

    public static final String CURRENT_LOCALE_ATTR = "satori.current.locale";

    @Override
    public Locale resolveLocale(HttpHeaders headers, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        return (session != null) ? getLocaleFromSession(headers, session) : getLocaleFromRequest(headers);

    }

    private Locale getLocaleFromRequest(HttpHeaders headers) {

        final List<Locale> languages = headers.getAcceptableLanguages();

        for (Locale locale : languages) {
            if (!locale.toString().contains("*")) { // skip wildcards
                return locale;
            }
        }

        return Locale.getDefault();

    }

    private Locale getLocaleFromSession(HttpHeaders headers, HttpSession session) {

        Locale locale = (Locale) session.getAttribute(CURRENT_LOCALE_ATTR);
        
        if (locale == null) {
            locale = getLocaleFromRequest(headers);
            session.setAttribute(CURRENT_LOCALE_ATTR, locale);
        }
        
        return locale;
        
    }

}
