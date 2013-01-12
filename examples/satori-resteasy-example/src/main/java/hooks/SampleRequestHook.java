package hooks;

import org.apache.shiro.SecurityUtils;

import com.github.jknack.handlebars.Context.Builder;

import satori.hbs.hooks.RequestHook;
import db.GraphDB;

public class SampleRequestHook implements RequestHook {

    @Override
    public void onModel(Builder builder) {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal != null) {
            builder.combine("person", GraphDB.byName(principal.toString()));
        }
    }

}
