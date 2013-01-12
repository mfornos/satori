package satori.hbs.hooks;

import com.github.jknack.handlebars.Handlebars;

public interface HandlebarsHook {
    
    void onHandlebarsCreated(Handlebars h);
    
}
