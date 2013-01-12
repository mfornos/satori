package satori.i18n.locale;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

public interface LocaleResolver {


    Locale resolveLocale(HttpHeaders headers, HttpServletRequest request);

}
