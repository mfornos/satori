package satori.hbs.hooks;

import org.apache.shiro.SecurityUtils;

import com.github.jknack.handlebars.Context.Builder;

public class ShiroRequestHook implements RequestHook {

    @Override
    public void onModel(Builder builder) {
        
        builder.combine("subject", SecurityUtils.getSubject());
        
    }

}
