package satori.hbs.helpers;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;

import javax.ws.rs.core.UriInfo;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.github.jknack.handlebars.Options;

public class NavigationHelpersTest extends HelpersTest {

    @Mock private UriInfo uriInfo;

    @Test
    public void test() throws IOException {

        Mockito.when(uriInfo.getPath()).thenReturn("hello/123");

        NavigationHelpers.register(handlebars, sc);

        Options options = OptionsMock.options(new Object[] { 123 }, map("label", "one"), map("uriInfo", uriInfo));
        assertEquals("<li class=\"active\"><a href=\"test/hello/123\">one</a></li>",
                apply("navlink", "/hello/{id}", options));

        options = OptionsMock.options(new Object[] { 500 }, map("label", "one", "regexp", "/hello/\\d+"),
                map("uriInfo", uriInfo));
        assertEquals("<li class=\"active\"><a href=\"test/hello/500\">one</a></li>",
                apply("navlink", "/hello/{id}", options));

        options = OptionsMock.options(new Object[] { 500 }, map("label", "one"), map("uriInfo", uriInfo));
        assertEquals("<li><a href=\"test/hello/500\">one</a></li>", apply("navlink", "/hello/{id}", options));

        assertEquals("<li><a href=\"test/null\" /></li>", apply("navlink", null, OptionsMock.empty()));

    }

}
