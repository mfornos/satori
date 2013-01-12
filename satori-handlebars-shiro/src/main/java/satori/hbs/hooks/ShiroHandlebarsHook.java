package satori.hbs.hooks;

import satori.hbs.helpers.ShiroHelpers;

import com.github.jknack.handlebars.Handlebars;

public class ShiroHandlebarsHook implements HandlebarsHook {

    @Override
    public void onHandlebarsCreated(Handlebars h) {
        
        ShiroHelpers.register(h);
        
    }

}
