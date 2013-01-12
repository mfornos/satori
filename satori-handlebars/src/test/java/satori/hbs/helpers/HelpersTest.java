package satori.hbs.helpers;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public abstract class HelpersTest {

    @Mock Handlebars handlebars;
    @Mock ServletContext sc;
    Map<String, Helper<Object>> helpers;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {

        helpers = new HashMap<String, Helper<Object>>();

        MockitoAnnotations.initMocks(this);

        when(handlebars.registerHelper(anyString(), (Helper<Object>) any())).thenAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                helpers.put((String) args[0], (Helper<Object>) args[1]);
                return null;
            }
        });

        when(sc.getContextPath()).thenReturn("test");

    }

    HashMap<String, Object> map(Object... objects) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < objects.length; i++) {
            map.put((String) objects[i], objects[++i]);
        }
        return map;
    }

    String apply(String helper, Object model, Options options) throws IOException {
        assertTrue("Helper '" + helper + "' not registerd!", helpers.containsKey(helper));
        return helpers.get(helper).apply(model, options).toString();
    }

}
