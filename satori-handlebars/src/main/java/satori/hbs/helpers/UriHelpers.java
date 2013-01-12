package satori.hbs.helpers;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.commons.beanutils.ConvertUtils;

import satori.i18n.TextFlow;
import satori.i18n.Translation;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public final class UriHelpers {

    public static void register(final Handlebars handlebars, final ServletContext context) {

        Helper<String> contextPathHelper = contextPath(context.getContextPath());
        handlebars.registerHelper("@link", contextPathHelper);
        handlebars.registerHelper("@", contextPathHelper);
        handlebars.registerHelper("flow", new Helper<Object>() {

            @Override
            public CharSequence apply(Object ctx, Options options) throws IOException {

                String base = options.hash("base");

                return new Handlebars.SafeString((base == null) ? TextFlow.emit(options.fn()) : TextFlow.emit(base,
                        options.fn()));

            }

        });
        handlebars.registerHelper("msg", new Helper<Object>() {

            @Override
            public CharSequence apply(Object key, Options options) throws IOException {

                String base = options.hash("base");

                return (base == null) ? Translation.translate(ConvertUtils.convert(key)) : Translation.translate(base,
                        ConvertUtils.convert(key));

            }

        });

    }

    private static Helper<String> contextPath(final String contextPath) {
        return new Helper<String>() {

            @Override
            public CharSequence apply(String path, Options options) throws IOException {

                Boolean wrap = (Boolean) ConvertUtils.convert(options.hash("wrap", "true"), Boolean.class);
                return new Handlebars.SafeString(Util.withContextPath(contextPath, path, options, wrap));

            }

        };
    }
}
