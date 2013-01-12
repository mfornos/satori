package satori.jersey.spi.inject;

import javax.ws.rs.ext.Provider;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

@Provider
public class SubjectProvider extends AbstractInjectableProvider<Subject> {

    @Override
    public Subject getValue() {

        return SecurityUtils.getSubject();

    }

}
