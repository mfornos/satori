package satori.hbs.helpers;

import static satori.utils.PathUtils.relativize;

import javax.ws.rs.core.UriBuilder;

import org.apache.commons.beanutils.ConvertUtils;

import satori.utils.Html;

import com.github.jknack.handlebars.Options;

public class Util {

    public static Html addInformalOptions(Html html, Options options) {

        for (String name : options.hash.keySet()) {
            // if (excludes.length > 0 && Arrays.binarySearch(excludes, name) >
            // -1) {
            // continue;
            // }
            html.attr(name, ConvertUtils.convert(options.hash(name)));
        }

        return html;

    }

    public static String buildPath(String path, Options options) {
        if (options.params != null && options.params.length > 0) {
            return UriBuilder.fromPath(path).build(options.params).toString();
        } else {
            return path;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T rem(Options options, String name) {
        return (T) options.hash.remove(name);
    }

    public static <T> T rem(Options options, String name, T defaultValue) {
        T v = rem(options, name);
        return (v == null) ? defaultValue : v;
    }

    public static CharSequence withContextPath(final String contextPath, String path, boolean wrap) {
        return String.format((wrap) ? "\"%s/%s\"" : "%s/%s", contextPath, relativize(path));
    }

    public static CharSequence withContextPath(final String contextPath, String path, Options options, boolean wrap) {
        return withContextPath(contextPath, Util.buildPath(path, options), wrap);
    }

}
