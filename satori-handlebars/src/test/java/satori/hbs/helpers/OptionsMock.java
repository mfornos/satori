package satori.hbs.helpers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.mockito.Mockito;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;

public class OptionsMock extends Options {

    public static Options empty() {
        return options(new Object[] {}, new HashMap<String, Object>());
    }

    public static Options options(HashMap<String, Object> map) {
        return options(new Object[] {}, map);
    }

    public static Options options(Object[] objects, HashMap<String, Object> hashMap) {
        return options(objects, hashMap, new HashMap<String, Object>());
    }

    public static Options options(final Object[] params, final Map<String, Object> hash, final Map<String, Object> ctx) {
        Handlebars handlebars = Mockito.mock(Handlebars.class);
        Context context = Context.newContext(ctx);
        Template fn = Mockito.mock(Template.class);
        Template inverse = Mockito.mock(Template.class);
        return new OptionsMock(handlebars, context, fn, inverse, params, hash);
    }

    private OptionsMock(final Handlebars handlebars, final Context context, final Template fn, final Template inverse,
            final Object[] params, final Map<String, Object> hash) {
        super(handlebars, context, fn, inverse, params, hash);
    }

    @Override
    public CharSequence apply(final Template template) throws IOException {
        return null;
    }

    @Override
    public CharSequence apply(final Template template, final Object context) throws IOException {
        return null;
    }

    @Override
    public CharSequence fn() throws IOException {
        return null;
    }

    @Override
    public CharSequence fn(final Object context) throws IOException {
        return null;
    }

    @Override
    public <T> T get(final String name) {
        return null;
    }

    @Override
    public CharSequence inverse() throws IOException {
        return null;
    }

    @Override
    public CharSequence inverse(final Object context) throws IOException {
        return null;
    }

    @Override
    public Template partial(final String path) {
        return null;
    }

    @Override
    public void partial(final String path, final Template partial) {
    }

    @Override
    public <T> T get(String paramString, T paramT) {
        return null;
    }

    @Override
    public Context wrap(Object paramObject) {
        return null;
    }

    @Override
    public <T> T data(String paramString) {
        return null;
    }

    @Override
    public void data(String paramString, Object paramObject) {
        
    }

}
