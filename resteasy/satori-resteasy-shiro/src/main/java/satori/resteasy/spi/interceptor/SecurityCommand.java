package satori.resteasy.spi.interceptor;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jboss.resteasy.spi.HttpRequest;


public abstract class SecurityCommand<T> {
    
    @SuppressWarnings("unchecked")
    public void process(Object annotation, HttpRequest request) {

        if (!checkConstraints((T) annotation, request, SecurityUtils.getSubject())) {
         
            throw new WebApplicationException(Response.Status.FORBIDDEN);
            
        }

    }

    abstract protected boolean checkConstraints(T annotation, HttpRequest request, Subject subject);
}
