package satori.hbs.hooks;

import com.github.jknack.handlebars.Context.Builder;


public interface RequestHook {

    void onModel(Builder builder);

}
