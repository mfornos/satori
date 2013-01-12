package satori.hbs.helpers;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import java.util.HashMap;

import org.junit.Test;

import com.github.jknack.handlebars.Options;

public class UtilTest {

    @Test
    public void test() {

        assertEquals("\"test/hi\"", Util.withContextPath("test", "hi", true));
        assertEquals("test/hi", Util.withContextPath("test", "/hi", false));

        HashMap<String, Object> hash = new HashMap<String, Object>();
        hash.put("hi", "ho");
        Options opts = OptionsMock.options(new Object[] { 7, 3 }, hash);
        assertEquals("test/hi/7/3", Util.withContextPath("test", "hi/{id1}/{id2}", opts, false));
        assertEquals("hi/7/3", Util.buildPath("hi/{id1}/{id2}", opts).toString());

        assertEquals(1, opts.hash.size());
        assertEquals("ho", Util.rem(opts, "hi"));
        assertEquals("bar", Util.rem(opts, "foo", "bar"));
        assertNull(Util.rem(opts, "foo"));
        assertEquals(0, opts.hash.size());

    }

}
