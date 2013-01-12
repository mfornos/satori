package satori.hbs.helpers;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.ws.rs.core.UriInfo;

import satori.i18n.Translation;
import satori.utils.Html;
import satori.utils.PathUtils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public class NavigationHelpers {

    public static void register(final Handlebars handlebars, ServletContext context) {

        handlebars.registerHelper("navlink", navItem(context.getContextPath()));

    }

    private static Helper<String> navItem(final String contextPath) {
        return new Helper<String>() {

            @Override
            public CharSequence apply(String basePath, Options options) throws IOException {
                String regexp = Util.rem(options, "regexp");
                String label = Util.rem(options, "label");
                String icon = Util.rem(options, "icon");
                String path = Util.buildPath(basePath, options);

                UriInfo uriInfo = (UriInfo) options.context.get("uriInfo");

                String css = uriInfo != null && matches(PathUtils.absolutize(uriInfo.getPath()), path, regexp) ? "active"
                        : null;

                Html html = Html.builder().begin("li").attr("class", css);
                html.begin("a").attrs("href",
                        Util.withContextPath(contextPath, path, false).toString());

                Util.addInformalOptions(html, options);

                if (icon != null) {
                    html.begin("i").attr("class", icon).close().end();
                    html.text(" ");
                }

                html.text(Translation.translate(label));
                html.endAll();

                return new Handlebars.SafeString(html.emit());

            }

            private boolean matches(String currentPath, String path, String regexp) {
                return currentPath.equalsIgnoreCase(path) || (regexp != null && currentPath.matches(regexp));
            }
        };
    }

}
