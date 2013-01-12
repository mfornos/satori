package hooks;

import helpers.SampleHelpers;
import satori.hbs.hooks.HandlebarsHook;

import com.github.jknack.handlebars.Handlebars;

public class SampleHandlebarsHook implements HandlebarsHook {

    @Override
    public void onHandlebarsCreated(Handlebars h) {
        SampleHelpers.register(h);
    }

}
