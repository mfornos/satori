package satori.jersey.spi.filter;

import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import satori.security.CsrfHelper;

import com.sun.jersey.api.representation.Form;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

/**
 * CSRF prevention filter. Implements a double submit scheme with a secure token
 * that changes per request.
 * 
 */
public class CsrfFilter implements ResourceFilter, ContainerRequestFilter {

    @Override
    public ContainerRequest filter(ContainerRequest request) {

        Map<String, Cookie> cookies = request.getCookies();

        if (cookies.containsKey(CsrfHelper.CSRF_TOKEN_KEY)) {
            Cookie cookie = cookies.get(CsrfHelper.CSRF_TOKEN_KEY);
            String token = resolveToken(request);
            if (cookie != null && cookie.getValue().equals(token)) {
                return request;
            }
        }

        throw new WebApplicationException(Response.Status.BAD_REQUEST);

    }

    @Override
    public ContainerRequestFilter getRequestFilter() {
        return this;
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return null;
    }

    private String resolveToken(ContainerRequest request) {

        String method = request.getMethod();

        if (method.equals("POST") || method.equals("PUT")) {
            Form params = request.getFormParameters();
            return params.getFirst(CsrfHelper.CSRF_TOKEN_PARAM);
        } else if (method.equals("GET")) {
            MultivaluedMap<String, String> params = request.getQueryParameters();
            return params.getFirst(CsrfHelper.CSRF_TOKEN_PARAM);
        }

        return "";

    }

}
