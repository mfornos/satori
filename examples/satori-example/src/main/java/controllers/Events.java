package controllers;

import static satori.utils.ResponseUtils.seeOther;
import static satori.utils.ResponseUtils.view;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import models.events.Event;
import models.events.Reservation;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;

import satori.security.CsrfGuard;
import satori.validation.BeanValidator;
import satori.validation.Errors;
import satori.view.View;
import satori.view.flash.Flash;

import com.sun.jersey.api.core.InjectParam;
import com.yammer.metrics.annotation.Timed;

import db.DB;

@Path("/eapp")
public class Events {

    private static final String HOME = "/eapp";

    @Context private Flash flash;

    @GET
    @Timed
    @Path("about")
    public View about() {

        return view();

    }

    @GET
    @Timed
    @Path("event/{id}")
    public View event(@PathParam("id") Integer id) {

        return view(DB.getEvent(id)).with("events", DB.events());

    }

    @GET
    @Timed
    @RequiresUser
    @Path("event/form/{id:\\d*}")
    public Response formEvent(@PathParam("id") Integer id) {

        return view(id == null ? DB.EMPTY_EVENT : DB.getEvent(id)).csrfToken().build();

    }

    @GET
    @Timed
    @RequiresUser
    @Path("reservation/form/{id:\\d*}")
    public View formReservation(@PathParam("id") Integer id) {

        return view(id == null ? DB.EMPTY_RESERV : DB.getReservation(id));

    }

    @GET
    @Timed
    @RequiresUser
    @Path("reservation/form/event/{id:\\d*}")
    public View reservationForEvent(@PathParam("id") Integer eventId) {

        return view("/eapp/form/reservation", eventId == null ? DB.EMPTY_RESERV : new Reservation(eventId));

    }

    @GET
    @Timed
    public View home() {

        return view(DB.events());

    }

    @GET
    @Timed
    @Path("events/form")
    @RequiresRoles("admin")
    public View formBulkEvents() {

        return view(DB.events()).with("flash", flash);

    }

    @GET
    @Timed
    @Path("events/form/delete")
    @RequiresRoles("admin")
    public View formBulkDeleteEvents() {

        return view(DB.events()).with("flash", flash);

    }

    @POST
    @Timed
    @Path("events/delete")
    @RequiresRoles("admin")
    public Response postDeleteEvents(@FormParam("id") List<Integer> ids) {

        DB.removeEvents(ids);
        flash.success(String.format("%s events removed.", ids.size()));
        return seeOther("/eapp/events/form/delete");

    }

    @POST
    @Timed
    @Path("events/form")
    @RequiresRoles("admin")
    public Response postFormEvents(@InjectParam Collection<Event> events) {

        DB.merge(events);
        flash.success(String.format("%s events saved successfully.", events.size()));
        return seeOther("/eapp/events/form");

    }

    @GET
    @Timed
    @Path("sample")
    public Response etag(@Context Request request) {

        return view("/eapp/home", DB.events()).tag(request).build();

    }

    @GET
    @Timed
    @Path("event/delete/{id}")
    @RequiresRoles("admin")
    public Response deleteEvent(@PathParam("id") Integer id) {

        DB.removeEvent(id);
        return seeOther(HOME);

    }

    @POST
    @Timed
    @CsrfGuard
    @Path("event")
    @RequiresRoles("admin")
    public View postFormEvent(@InjectParam Event model) {

        Errors<Event> errors = BeanValidator.validate(model);
        if (errors.hasErrors()) {
            return view(model).with("errors", errors);
        } else {
            DB.save(model);
            return view(model).with("success", true);
        }

    }

    @POST
    @Timed
    @RequiresUser
    @Path("reservation")
    public View postFormReservation(@InjectParam Reservation model) {

        Errors<Reservation> errors = BeanValidator.validate(model);
        if (errors.hasErrors()) {
            return view(model).with("errors", errors);
        } else {
            DB.save(model);
            return view(model).with("success", true);
        }

    }

}
