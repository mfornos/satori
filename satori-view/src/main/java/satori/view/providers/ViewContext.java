package satori.view.providers;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

public class ViewContext {

    public final HttpServletRequest request;
    
    public final HttpServletResponse response;
    
    public final UriInfo uriInfo;

    public final SecurityContext securityContext;

    public final HttpHeaders headers;

    public final Annotation[] annotations;

    public ViewContext(HttpServletRequest request, HttpServletResponse response, UriInfo uriInfo, SecurityContext securityContext,
            HttpHeaders headers, Annotation[] annotations) {
        
        this.request = request;
        this.uriInfo = uriInfo;
        this.securityContext = securityContext;
        this.headers = headers;
        this.annotations = annotations;
        this.response = response;
        
    }

}
