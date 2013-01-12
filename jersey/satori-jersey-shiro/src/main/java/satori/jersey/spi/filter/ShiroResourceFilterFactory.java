package satori.jersey.spi.filter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

/**
 * A {@link ResourceFilterFactory} supporting the {@link RequiresUser},
 * {@link RequiresAuthentication}, {@link RequiresGuest}, {@link RequiresRoles}
 * and {@link RequiresPermissions} on resource methods sub-resource methods, and
 * sub-resource locators.
 * 
 * When an application is deployed as a Servlet or Filter this Jersey resource
 * filter can be registered using the following initialization parameter:
 * <blockquote>
 * 
 * <pre>
 *     &lt;init-param&gt;
 *         &lt;param-name&gt;com.sun.jersey.spi.container.ResourceFilters&lt;/param-name&gt;
 *         &lt;param-value&gt;satori.jersey.spi.filter.ShiroResourceFilterFactory&lt;/param-value&gt;
 *     &lt;/init-param&gt
 * </pre>
 * 
 * </blockquote>
 * 
 * @see com.sun.jersey.api.container.filter
 */
public class ShiroResourceFilterFactory implements ResourceFilterFactory {

    // TODO cached
    @Override
    public List<ResourceFilter> create(AbstractMethod am) {

        // Guest
        RequiresGuest rg = am.getAnnotation(RequiresGuest.class);
        if (rg != null)
            return null;

        // Roles
        final RequiresRoles rr = am.getAnnotation(RequiresRoles.class);
        if (rr != null)
            return Collections.<ResourceFilter> singletonList(new BaseFilter() {

                @Override
                protected boolean checkConstraints(ContainerRequest request, Subject subject) {
                    return subject != null && subject.hasAllRoles(Arrays.asList(rr.value()));
                }

            });

        // Permissions
        final RequiresPermissions rp = am.getAnnotation(RequiresPermissions.class);
        if (rp != null)
            return Collections.<ResourceFilter> singletonList(new BaseFilter() {

                @Override
                protected boolean checkConstraints(ContainerRequest request, Subject subject) {
                    return subject != null && subject.isPermittedAll(rp.value());
                }

            });

        // User
        if (am.getAnnotation(RequiresUser.class) != null)
            return Collections.<ResourceFilter> singletonList(new BaseFilter() {

                @Override
                protected boolean checkConstraints(ContainerRequest request, Subject subject) {
                    return subject != null && subject.getPrincipal() != null;
                }

            });

        // Authenticated
        if (am.getAnnotation(RequiresAuthentication.class) != null)
            return Collections.<ResourceFilter> singletonList(new BaseFilter() {

                @Override
                protected boolean checkConstraints(ContainerRequest request, Subject subject) {
                    return subject != null && subject.isAuthenticated();
                }

            });

        return null;
    }
}
