package satori.i18n;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Locale;

import org.junit.Test;

import satori.i18n.TextFlow;
import satori.i18n.locale.LocaleSource;

public class TextFlowTest {

    @Test
    public void test() {
        LocaleSource.update(Locale.ENGLISH);
        assertEquals(get("/html/t1.html.en.expect"), TextFlow.emit(get("/html/t1.html")));
        LocaleSource.update(new Locale("es"));
        assertEquals(get("/html/t1.html.es.expect"), TextFlow.emit(get("/html/t1.html")));
    }

    private String get(String file) {
        InputStream is = getClass().getResourceAsStream(file);
        return new java.util.Scanner(is).useDelimiter("\\A").next();
    }

}
