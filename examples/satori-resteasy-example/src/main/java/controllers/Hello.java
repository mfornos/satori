package controllers;

import static satori.utils.ResponseUtils.seeOther;
import static satori.utils.ResponseUtils.view;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import models.UserForm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.jboss.resteasy.annotations.Form;

import satori.validation.BeanValidator;
import satori.validation.Errors;
import satori.view.View;

import com.orientechnologies.orient.core.id.ORecordId;

import db.GraphDB;

@Path("/")
public class Hello {

    @Context private SecurityContext sec;

    @GET
    @RequiresUser
    @Path("follow/{id}")
    public View follow(@PathParam("id") ORecordId id) {
        return view("/person", GraphDB.follow(sec.getUserPrincipal().getName(), id));
    }

    @GET
    public View home() {
        return view(GraphDB.all());
    }

    @POST
    @Path("signin")
    public View postSignin(@Form UserForm form, @Context HttpServletRequest request) {

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
    @Path("signout")
    public Response signout() {

        SecurityUtils.getSubject().logout();
        return seeOther("/");

    }

    @GET
    @RequiresUser
    @Path("unfollow/{id}")
    public View unfollow(@PathParam("id") ORecordId id) {
        return view("/person", GraphDB.unfollow(sec.getUserPrincipal().getName(), id));
    }

    @GET
    @Path("secret")
    @RequiresRoles("admin")
    public Response secret() {
        return Response.ok("Super secret", MediaType.TEXT_PLAIN).build();
    }

}
