package satori.resteasy.spi.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.jboss.resteasy.annotations.interception.Precedence;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

@Provider
@ServerInterceptor
@Precedence("SECURITY")
public class ShiroInterceptor implements PreProcessInterceptor {

    private final Map<Class<? extends Annotation>, SecurityCommand<?>> commands;

    public ShiroInterceptor() {

        this.commands = new LinkedHashMap<Class<? extends Annotation>, SecurityCommand<?>>();

        this.commands.put(RequiresRoles.class, new SecurityCommand<RequiresRoles>() {

            @Override
            protected boolean checkConstraints(RequiresRoles rr, HttpRequest request, Subject subject) {
                return subject != null && subject.hasAllRoles(Arrays.asList(rr.value()));
            }

        });

        this.commands.put(RequiresPermissions.class, new SecurityCommand<RequiresPermissions>() {

            @Override
            protected boolean checkConstraints(RequiresPermissions rp, HttpRequest request, Subject subject) {
                return subject != null && subject.isPermittedAll(rp.value());
            }

        });

        this.commands.put(RequiresUser.class, new SecurityCommand<RequiresUser>() {

            @Override
            protected boolean checkConstraints(RequiresUser ru, HttpRequest request, Subject subject) {
                return subject != null && subject.getPrincipal() != null;
            }

        });

        this.commands.put(RequiresAuthentication.class, new SecurityCommand<RequiresAuthentication>() {

            @Override
            protected boolean checkConstraints(RequiresAuthentication ra, HttpRequest request, Subject subject) {
                return subject != null && subject.isAuthenticated();
            }

        });

    }

    @Override
    // TODO cached
    public ServerResponse preProcess(HttpRequest request, ResourceMethod method) throws Failure,
            WebApplicationException {

        Method m = method.getMethod();

        // Guest
        RequiresGuest rg = m.getAnnotation(RequiresGuest.class);
        if (rg != null)
            return null;

        Set<Class<? extends Annotation>> annotations = commands.keySet();
        for (Class<? extends Annotation> clz : annotations) {
            Object annotation = m.getAnnotation(clz);
            if (annotation != null) {
                try {
                    commands.get(clz).process(annotation, request);
                    break;
                } catch (WebApplicationException ex) {
                    // Avoid WARN
                    return (ServerResponse) ServerResponse.status(Status.FORBIDDEN).build();
                }
            }
        }

        return null;
    }

}
