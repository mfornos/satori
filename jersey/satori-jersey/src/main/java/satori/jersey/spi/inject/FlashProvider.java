package satori.jersey.spi.inject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import satori.view.flash.Flash;

@Provider
public class FlashProvider extends AbstractInjectableProvider<Flash> {

    @Context private HttpServletRequest request;
    
    @Override
    public Flash getValue() {
        
        return new Flash(request.getSession(false));
        
    }

}
