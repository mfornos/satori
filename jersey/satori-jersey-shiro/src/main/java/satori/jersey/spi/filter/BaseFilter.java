package satori.jersey.spi.filter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

abstract class BaseFilter implements ResourceFilter, ContainerRequestFilter {

    // ResourceFilter

    public ContainerRequestFilter getRequestFilter() {
        return this;
    }

    public ContainerResponseFilter getResponseFilter() {
        return null;
    }

    // ContainerRequestFilter

    public ContainerRequest filter(ContainerRequest request) {

        if (checkConstraints(request, SecurityUtils.getSubject())) {
            return request;
        }

        throw new WebApplicationException(Response.Status.FORBIDDEN);

    }

    abstract protected boolean checkConstraints(ContainerRequest request, Subject subject);
}
