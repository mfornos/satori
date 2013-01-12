package satori.utils;

import static satori.utils.PathUtils.absolutize;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response.Status;

import satori.view.View;

public final class ResponseUtils {

    private static final Pattern SPLIT_CAMEL = Pattern
            .compile("(?<=[A-Z])(?=[A-Z][a-z])|(?<=[^A-Z])(?=[A-Z])|(?<=[A-Za-z])(?=[^A-Za-z])");

    public static String[] IGNORED_PREFIXES = new String[] { "post", "get", "remove", "delete", "put", "save", "set" };

    public static Response notFound() {

        return Response.status(Status.NOT_FOUND).build();

    }

    public static Response ok() {

        return Response.ok().build();

    }

    public static Response ok(String templateName) {

        return ok(new View(absolutize(templateName)));

    }

    public static Response ok(View v) {

        return Response.ok(v).build();

    }

    public static String resolveName() {

        return resolveName(3);

    }

    public static String resolveName(int depth) {

        Throwable t = new Throwable();
        StackTraceElement[] st = t.getStackTrace();
        StackTraceElement e = st[depth];
        return toTemplatePath(e);

    }

    public static Response seeOther(Class<?> resource, String path, Object... objects) {

        return seeOther(UriBuilder.fromResource(resource).path(path).build(objects));

    }

    public static Response seeOther(String path, Object... objects) {

        return seeOther(UriBuilder.fromPath(path).build(objects));

    }

    public static Response seeOther(URI uri) {

        return Response.seeOther(uri).build();

    }

    public static Response seeReferer(HttpServletRequest request) throws URISyntaxException {

        return seeOther(new URI(request.getHeader("referer")));

    }

    public static Response seeReferer(HttpServletRequest request, String fallback) {

        try {
            return seeOther(new URI(request.getHeader("referer")));
        } catch (URISyntaxException e) {
            return seeOther(fallback);
        }

    }

    public static View view() {

        return new View(resolveName());

    }

    public static View view(Object model) {

        return new View(resolveName(), model);

    }

    public static View view(String templateName, Object model) {

        return new View(templateName, model);

    }

    private static String abs(String value) {

        return value.charAt(value.length() - 1) == '/' ? value : value + '/';

    }

    private static String mountPoint(StackTraceElement e) {

        try {
            Path path = Class.forName(e.getClassName()).getAnnotation(Path.class);
            return (path == null) ? "/" : abs(path.value()).toLowerCase();
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);

        }
    }

    private static String toTemplatePath(StackTraceElement e) {

        String mn = e.getMethodName();
        String mp = mountPoint(e);

        for (String p : IGNORED_PREFIXES) {
            if (mn.startsWith(p)) {
                mn = mn.substring(p.length());
                break;
            }
        }

        return mp + SPLIT_CAMEL.matcher(mn).replaceAll("/").toLowerCase();

    }

}
