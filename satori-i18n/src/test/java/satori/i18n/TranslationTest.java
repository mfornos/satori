package satori.i18n;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ListResourceBundle;
import java.util.Locale;

import org.testng.annotations.Test;

import satori.i18n.Translation;
import satori.i18n.locale.LocaleSource;

public class TranslationTest {

    @Test(threadPoolSize = 10, invocationCount = 10)
    public void testMessages() {

        LocaleSource.update(Locale.ENGLISH);
        Translation.setResourceBundle(LocaleSource.getCurrentLocale(),
                Translation.loadBundle(LocaleSource.getCurrentLocale()));
        assertEquals("hi!", Translation.translate("hello.world"));
        assertEquals("unresolved symbol", Translation.translate("unresolved symbol"));

        LocaleSource.update(new Locale("es"));
        assertEquals("lo!", Translation.translate("hello.world"));
        assertEquals("unresolved symbol", Translation.translate("unresolved symbol"));

        LocaleSource.update(Locale.ENGLISH);
        Translation.setResourceBundle(Locale.ENGLISH, "i18n.secondary");
        assertEquals("hi ho!", Translation.translate("i18n.secondary", "hello.world"));
        assertEquals("unresolved symbol", Translation.translate("i18n.secondary", "unresolved symbol"));

        Translation.setResourceBundle(Locale.ENGLISH, new ListResourceBundle() {
            private Object[][] contents = { { "hello.world", "hello 2!" }, { "bye", "good bye!" } };

            @Override
            protected Object[][] getContents() {
                return contents;
            }
        });
        assertEquals("hello 2!", Translation.translate("hello.world"));
        assertEquals("unresolved symbol", Translation.translate("unresolved symbol"));
        assertEquals("good bye!", Translation.translate("bye"));

    }

    @Test(threadPoolSize = 10, invocationCount = 10)
    public void testFormats() {

        LocaleSource.update(Locale.UK);
        Date date = new Date();
        assertEquals(new SimpleDateFormat("MM/dd/yyyy kk:mm").format(date),
                Translation.getFormat("date.time").format(date));
        assertEquals(new SimpleDateFormat("MM/dd/yyyy").format(date), Translation.getFormat("date").format(date));
        assertEquals(new SimpleDateFormat("kk:mm").format(date), Translation.getFormat("time").format(date));
        assertEquals(DecimalFormat.getInstance(LocaleSource.getCurrentLocale()).format(10.99990), Translation
                .getFormat("decimal").format(10.99990));
        assertEquals(DecimalFormat.getPercentInstance(LocaleSource.getCurrentLocale()).format(0.5), Translation
                .getFormat("percent").format(0.5));
        assertEquals(DecimalFormat.getCurrencyInstance(LocaleSource.getCurrentLocale()).format(10.99990), Translation
                .getFormat("currency").format(10.99990));
        assertEquals(DecimalFormat.getCurrencyInstance(LocaleSource.getCurrentLocale()).getCurrency().getSymbol(),
                ((NumberFormat) Translation.getFormat("currency")).getCurrency().getSymbol());

        LocaleSource.update(new Locale("es", "ES"));
        assertEquals(new SimpleDateFormat("MM/dd/yyyy kk:mm").format(date),
                Translation.getFormat("date.time").format(date));
        assertEquals(new SimpleDateFormat("MM/dd/yyyy").format(date), Translation.getFormat("date").format(date));
        assertEquals(new SimpleDateFormat("kk:mm").format(date), Translation.getFormat("time").format(date));
        assertEquals(DecimalFormat.getInstance(LocaleSource.getCurrentLocale()).format(10.99990), Translation
                .getFormat("decimal").format(10.99990));
        assertEquals(DecimalFormat.getPercentInstance(LocaleSource.getCurrentLocale()).format(0.5), Translation
                .getFormat("percent").format(0.5));
        assertEquals(DecimalFormat.getCurrencyInstance(LocaleSource.getCurrentLocale()).format(10.99990), Translation
                .getFormat("currency").format(10.99990));

    }

    @Test(threadPoolSize = 10, invocationCount = 10)
    public void testFormatsExt() {

        LocaleSource.update(Locale.UK);
        if (Translation.getFormat("custom") == null)
            Translation.loadFormat(LocaleSource.getCurrentLocale(), "custom", new DecimalFormat("00.#"));

        assertEquals(new DecimalFormat("00.#").format(1.2), Translation.getFormat("custom").format(1.2));

        LocaleSource.update(new Locale("es"));
        assertNull(Translation.getFormat("custom"));

    }

    @Test(threadPoolSize = 10, invocationCount = 10)
    public void testFormatsUniversal() throws ParseException {

        Date zeroDate = new Date(0);
        assertEquals("1970-01-01T01:00:00+01:00", Translation.getUniversalFormat("iso8601.date").format(zeroDate));
        assertEquals(zeroDate, Translation.getUniversalFormat("iso8601.date").parseObject("1970-01-01T01:00:00+01:00"));

        Date aDate = new Date(1000000);
        assertEquals("1970-01-01T01:16:40+01:00", Translation.getUniversalFormat("iso8601.date").format(aDate));
        assertEquals(aDate, Translation.getUniversalFormat("iso8601.date").parseObject("1970-01-01T01:16:40+01:00"));

    }

}
