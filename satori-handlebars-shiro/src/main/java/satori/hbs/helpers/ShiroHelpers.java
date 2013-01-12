package satori.hbs.helpers;

import java.io.IOException;
import java.util.Arrays;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

/**
 * See http://shiro.apache.org/web.html
 * 
 */
public class ShiroHelpers {

    private static abstract class Conditional implements Helper<Object> {

        @Override
        public CharSequence apply(Object ctx, Options options) throws IOException {

            if (condition(SecurityUtils.getSubject(), ctx, options)) {
                return options.fn();
            } else {
                return options.inverse();
            }

        }

        abstract protected boolean condition(Subject subject, Object ctx, Options options);

    }

    public static void register(final Handlebars handlebars) {

        handlebars.registerHelper("guest", guest());
        handlebars.registerHelper("user", user());
        handlebars.registerHelper("authenticated", authenticated());
        handlebars.registerHelper("notAuthenticated", notAuthenticated());
        handlebars.registerHelper("principal", principal());
        handlebars.registerHelper("isRemembered", isRemembered());
        handlebars.registerHelper("hasRole", hasRole());
        handlebars.registerHelper("lacksRole", lacksRole());
        handlebars.registerHelper("hasAnyRole", hasAnyRole());
        handlebars.registerHelper("hasAllRoles", hasAllRoles());
        handlebars.registerHelper("hasPermission", hasPermission());
        handlebars.registerHelper("lacksPermission", lacksPermission());

    }

    private static Helper<Object> authenticated() {
        return new Conditional() {

            @Override
            protected boolean condition(Subject subject, Object ctx, Options options) {
                return subject != null && subject.isAuthenticated();
            }

        };
    }

    private static Helper<Object> guest() {
        return new Conditional() {

            @Override
            protected boolean condition(Subject subject, Object ctx, Options options) {
                return subject == null || subject.getPrincipal() == null;
            }

        };
    }

    private static Helper<Object> hasAllRoles() {
        return new Conditional() {

            @Override
            protected boolean condition(Subject subject, Object ctx, Options options) {
                return subject != null && subject.hasAllRoles(Arrays.asList(((String) ctx).split(",")));
            }

        };
    }

    private static Helper<Object> hasAnyRole() {
        return new Conditional() {

            @Override
            protected boolean condition(Subject subject, Object ctx, Options options) {
                return subject != null && hasAnyRole(subject, ((String) ctx).split(","));
            }

            private boolean hasAnyRole(Subject subject, String[] roles) {
                for (String role : roles) {
                    if (subject.hasRole(role))
                        return true;
                }
                return false;
            }

        };
    }

    private static Helper<Object> hasPermission() {
        return new Conditional() {

            @Override
            protected boolean condition(Subject subject, Object ctx, Options options) {
                return subject != null && subject.isPermitted((String) ctx);
            }

        };
    }

    private static Helper<Object> hasRole() {
        return new Conditional() {

            @Override
            protected boolean condition(Subject subject, Object ctx, Options options) {
                return subject != null && subject.hasRole((String) ctx);
            }

        };
    }

    private static Helper<Object> isRemembered() {
        return new Conditional() {

            @Override
            protected boolean condition(Subject subject, Object ctx, Options options) {
                return subject != null && subject.isRemembered();
            }

        };
    }

    private static Helper<Object> lacksPermission() {
        return new Conditional() {

            @Override
            protected boolean condition(Subject subject, Object ctx, Options options) {
                return subject != null && !subject.isPermitted((String) ctx);
            }

        };
    }

    private static Helper<Object> lacksRole() {
        return new Conditional() {

            @Override
            protected boolean condition(Subject subject, Object ctx, Options options) {
                return subject != null && !subject.hasRole((String) ctx);
            }

        };
    }

    private static Helper<Object> notAuthenticated() {
        return new Conditional() {

            @Override
            protected boolean condition(Subject subject, Object ctx, Options options) {
                return subject == null || !subject.isAuthenticated();
            }

        };
    }

    private static Helper<Object> principal() {
        return new Helper<Object>() {

            @Override
            public CharSequence apply(Object ctx, Options options) throws IOException {
                String type = options.hash("type");
                Subject subject = SecurityUtils.getSubject();
                try {
                    return (subject == null) ? null : (type == null) ? subject.getPrincipal().toString() : subject
                            .getPrincipals().oneByType(Class.forName(type)).toString();
                } catch (ClassNotFoundException e) {
                    return null;
                }
            }

        };
    }

    /**
     * The user tag will display its wrapped content only if the current Subject
     * is considered a 'user'. A 'user' in this context is defined as a Subject
     * with a known identity, either from a successful authentication or from
     * 'RememberMe' services. Note that this tag is semantically different from
     * the authenticated tag, which is more restrictive than this tag.
     */
    private static Helper<Object> user() {
        return new Conditional() {

            @Override
            protected boolean condition(Subject subject, Object ctx, Options options) {
                return subject != null && subject.getPrincipal() != null;
            }

        };
    }

}
