package hooks;

import helpers.SampleHelpers;
import satori.hbs.hooks.HandlebarsHook;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.StringHelpers;

public class RegisterHook implements HandlebarsHook {

    @Override
    public void onHandlebarsCreated(Handlebars h) {
        StringHelpers.register(h);
        SampleHelpers.register(h);
    }

}
