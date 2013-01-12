package satori.hbs.helpers;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import satori.hbs.forms.Selectable;
import satori.hbs.forms.SimpleSelectable;

import com.github.jknack.handlebars.Options;

public class FormHelpersTest extends HelpersTest {

    @Before
    public void init() {

        FormHelpers.register(handlebars, sc);

    }

    @Test
    public void testInput() throws IOException {

        assertEquals("<input type=\"text\" name=\"\" id=\"\" />", apply("input", null, OptionsMock.empty()));
        assertEquals("<input type=\"text\" name=\"user\" id=\"userId\" />",
                apply("input", null, OptionsMock.options(map("name", "user", "id", "userId"))));
        assertEquals("<input type=\"text\" name=\"user\" id=\"user\" onclick=\"blabla\" />",
                apply("input", null, OptionsMock.options(map("name", "user", "onclick", "blabla"))));
        assertEquals("<input type=\"text\" name=\"user\" id=\"user\" value=\"true\" />",
                apply("input", null, OptionsMock.options(map("name", "user", "value", true))));
        assertEquals("<input type=\"password\" name=\"user\" id=\"user\" value=\"default\" />",
                apply("input", null, OptionsMock.options(map("type", "password", "name", "user", "value", "default"))));
        assertEquals(
                "<input type=\"password\" name=\"user\" id=\"user\" value=\"default\" /><span class=\"help-inline\">here</span>",
                apply("input", null, OptionsMock.options(map("help", "here", "type", "password", "name", "user",
                        "value", "default"))));

    }

    @Test
    public void testControlGroup() throws IOException {

        Options opts = OptionsMock.options(map("label", "12"));
        opts = spy(opts);
        when(opts.fn()).thenReturn("Lorem ipsum");

        assertEquals("<div class=\"control-group\"><label for=\"12\" class=\"control-label\">12</label>"
                + "<div class=\"controls\">Lorem ipsum</div></div>", apply("group", null, opts));

        opts = OptionsMock.options(map("label", "12", "error", this));
        opts = spy(opts);
        when(opts.fn()).thenReturn("Lorem ipsum");

        assertEquals("<div class=\"control-group error\"><label for=\"12\" class=\"control-label\">12</label>"
                + "<div class=\"controls\">Lorem ipsum</div></div>", apply("group", null, opts));

    }

    @Test
    public void testSelect() throws IOException {

        assertEquals("<select name=\"\" id=\"\" />", apply("select", null, OptionsMock.empty()));

        Options opts = OptionsMock.options(map("name", "my", "value", 1, "options", "0..2"));
        assertEquals("<select name=\"my\" id=\"my\"><option value=\"0\">0</option><option value=\"1\""
                + " selected=\"selected\">1</option><option value=\"2\">2</option></select>",
                apply("select", null, opts));

        opts = OptionsMock.options(map("name", "my", "options", "0..2"));
        assertEquals("<select name=\"my\" id=\"my\"><option value=\"0\">0</option><option value=\"1\">"
                + "1</option><option value=\"2\">2</option></select>", apply("select", null, opts));

        Collection<Selectable> coll = new ArrayList<Selectable>();
        coll.add(new SimpleSelectable("one", 1));
        coll.add(new SimpleSelectable("two", 2));

        opts = OptionsMock.options(map("name", "my", "value", 2, "options", coll));
        assertEquals("<select name=\"my\" id=\"my\"><option value=\"1\">one</option><option value=\"2\""
                + " selected=\"selected\">two</option></select>", apply("select", null, opts));

        ArrayList<Object> objs = new ArrayList<Object>();
        objs.add(1);
        objs.add(true);

        opts = OptionsMock.options(map("name", "my", "value", true, "options", objs));
        assertEquals("<select name=\"my\" id=\"my\"><option value=\"1\">1</option><option value=\"true\""
                + " selected=\"selected\">true</option></select>", apply("select", null, opts));

    }

}
