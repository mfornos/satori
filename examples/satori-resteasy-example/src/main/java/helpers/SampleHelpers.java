package helpers;

import java.io.IOException;

import models.Person;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public class SampleHelpers {

    public static void register(final Handlebars h) {

        h.registerHelper("following", following());

        h.registerHelper("gravatar", new Helper<String>() {
            @Override
            public CharSequence apply(String email, Options options) throws IOException {
                Integer size = options.hash("size", 60);
                return new Handlebars.SafeString(String.format("<img src=\"%s\" />", Gravatar.getUrl(email, size)));
            }
        });

    }

    private static Helper<Object> following() {
        return new Helper<Object>() {
            @Override
            public CharSequence apply(Object ctx, Options options) throws IOException {

                Person a = (Person) options.context.get("person");
                if (a.follows.contains(new Person(ctx))) {
                    return options.fn();
                } else {
                    return options.inverse();
                }

            }
        };
    }
}
