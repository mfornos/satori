package helpers;

import humanize.Humanize;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import models.events.Reservation;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import satori.i18n.Formats;
import satori.i18n.Translation;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public class SampleHelpers {

    private static Handlebars renderer;

    public static String render(String template) {
        try {
            return renderer.compile(template).apply(Context.newContext(null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void register(Handlebars h) {

        renderer = h;

        h.registerHelper("plural", new Helper<String>() {
            @Override
            public CharSequence apply(String tl, Options options) throws IOException {
                return Humanize.pluralize(tl).format(options.params);
            }
        });

        h.registerHelper("isoDate", new Helper<Date>() {
            @Override
            public CharSequence apply(Date date, Options options) throws IOException {
                return Translation.getUniversalFormat(Formats.ISO8601_DATE).format(date);
            }
        });

        h.registerHelper("gravatar", new Helper<String>() {
            @Override
            public CharSequence apply(String email, Options options) throws IOException {
                Integer size = options.hash("size", 60);
                return Gravatar.getUrl(email, size);
            }
        });

        h.registerHelper("canEdit", new Helper<Reservation>() {
            @Override
            public CharSequence apply(Reservation reserv, Options options) throws IOException {
                Subject subject = SecurityUtils.getSubject();
                if (reserv.isOwner(subject.getPrincipal()) || subject.hasRole("admin")) {
                    return options.fn();
                } else {
                    return options.inverse();
                }
            }
        });

        h.registerHelper("sameDay", new Helper<Date>() {
            @Override
            public CharSequence apply(Date one, Options options) throws IOException {
                Date two = options.param(0);
                if (DateUtils.isSameDay(one, two)) {
                    return options.fn();
                } else {
                    return options.inverse();
                }
            }
        });

        h.registerHelper("expand", new Helper<String>() {
            @Override
            public CharSequence apply(String text, Options options) throws IOException {
                return new Handlebars.SafeString(options.handlebars.compile(text).apply(options.context));
            }
        });

        // XXX pending patch in hbars
        h.registerHelper("for", new Helper<Object>() {

            @Override
            public CharSequence apply(final Object context, final Options options) throws IOException {
                StringBuilder buffer = new StringBuilder();
                @SuppressWarnings("unchecked")
                Iterable<Object> elements = (Iterable<Object>) context;
                if (options.isFalsy(elements)) {
                    buffer.append(options.inverse());
                } else {
                    Iterator<Object> iterator = elements.iterator();
                    int index = 0;
                    while (iterator.hasNext()) {
                        buffer.append(options.fn(next(iterator, index++, options)));
                    }
                }
                return buffer.toString();
            }

            protected Object next(final Iterator<Object> iterator, final int index, Options options) {
                Object element = iterator.next();
                boolean first = index == 0;
                boolean even = index % 2 == 0;
                boolean last = !iterator.hasNext();
                return Context.newBuilder(options.context, element).combine("@index", index)
                        .combine("@first", first ? "first" : "").combine("@last", last ? "last" : "")
                        .combine("@odd", even ? "" : "odd").combine("@even", even ? "even" : "").build();
            }
        });
    }

}
