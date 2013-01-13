package controllers;

import static satori.utils.ResponseUtils.seeReferer;
import static satori.utils.ResponseUtils.view;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import models.UserForm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;

import satori.validation.BeanValidator;
import satori.validation.Errors;
import satori.view.View;

import com.sun.jersey.api.core.InjectParam;

@Path("/")
public class Main {

    @GET
    public View home() {

        return view();

    }

    @GET
    @Path("logout")
    public Response logout(@Context HttpServletRequest request) {

        SecurityUtils.getSubject().logout();
        return seeReferer(request, "/");

    }

    @POST
    @Path("signin")
    public View postSignin(@InjectParam UserForm form, @Context HttpServletRequest request) {

        Errors<UserForm> errors = BeanValidator.validate(form);
        if (errors.hasErrors()) {
            return view(form).with("errors", errors);
        }

        try {
            form.authenticate(request);
        } catch (AuthenticationException ex) {
            return view(form).with("errors", errors.putSimpleError("auth", "auth.error"));
        }

        return view().with("success", true);

    }

    @GET
    @Path("signin")
    public View signin() {

        return view();

    }

}
