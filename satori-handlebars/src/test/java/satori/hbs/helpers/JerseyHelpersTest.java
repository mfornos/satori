package satori.hbs.helpers;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

import com.github.jknack.handlebars.Options;

public class JerseyHelpersTest extends HelpersTest {

    @Test
    public void testLink() throws IOException {

        UriHelpers.register(handlebars, sc);

        Options options = OptionsMock.options(new Object[] { 123 }, map("wrap", false));
        assertEquals("test/hello/123", apply("@link", "hello/{id}", options));
    }

    @Test
    public void testFlow() throws IOException {
        
        UriHelpers.register(handlebars, sc);
        
        Options empty = OptionsMock.empty();
        empty = spy(empty);
        when(empty.fn()).thenReturn("Lorem ipsum <Lorem ipsum> Lorem ipsum </Lorem ipsum>");
        assertEquals("Dolorem si amet <Lorem ipsum> Dolorem si amet </Lorem ipsum>", apply("flow", null, empty));
        
        Options some = OptionsMock.options(map("base", "my.messages"));
        some = spy(some);
        when(some.fn()).thenReturn("Lorem ipsum <Lorem ipsum> Lorem ipsum </Lorem ipsum>");
        assertEquals("Et nunc <Lorem ipsum> Et nunc </Lorem ipsum>", apply("flow", null, some));
        
        assertEquals("Dolorem si amet <Lorem ipsum> Dolorem si amet </Lorem ipsum>", apply("flow", null, empty));

    }

    @Test
    public void testMsg() throws IOException {
        
        UriHelpers.register(handlebars, sc);
        
        Options empty = OptionsMock.empty();
        assertEquals("Key one", apply("msg", "key.1", empty));
        
        Options some = OptionsMock.options(map("base", "my.messages"));
        assertEquals("My key one", apply("msg", "key.1", some));
        
        assertEquals("Key one", apply("msg", "key.1", empty));
        
    }

}
