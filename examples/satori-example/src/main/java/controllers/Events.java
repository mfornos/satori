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
    @Path("about")
    @Timed
    public View about() {

        return view();

    }

    @GET
    @Path("event/{id}")
    @Timed
    public View event(@PathParam("id") Integer id) {

        return view(DB.getEvent(id)).add("events", DB.events());

    }

    @GET
    @RequiresUser
    @Path("event/form/{id:\\d*}")
    @Timed
    public View formEvent(@PathParam("id") Integer id) {

        return view(id == null ? DB.EMPTY_EVENT : DB.getEvent(id));

    }

    @GET
    @RequiresUser
    @Path("reservation/form/{id:\\d*}")
    @Timed
    public View formReservation(@PathParam("id") Integer id) {

        return view(id == null ? DB.EMPTY_RESERV : DB.getReservation(id));

    }

    @GET
    @RequiresUser
    @Path("reservation/form/event/{id:\\d*}")
    @Timed
    public View reservationForEvent(@PathParam("id") Integer eventId) {

        return view("/eapp/form/reservation", eventId == null ? DB.EMPTY_RESERV : new Reservation(eventId));

    }

    @GET
    @Timed
    public View home() {

        return view(DB.events());

    }

    @GET
    @Path("events/form")
    @RequiresRoles("admin")
    @Timed
    public View formBulkEvents() {

        return view(DB.events()).add("flash", flash);

    }

    @GET
    @Path("events/form/delete")
    @RequiresRoles("admin")
    @Timed
    public View formBulkDeleteEvents() {

        return view(DB.events()).add("flash", flash);

    }

    @POST
    @Path("events/delete")
    @RequiresRoles("admin")
    @Timed
    public Response postDeleteEvents(@FormParam("id") List<Integer> ids) {

        DB.removeEvents(ids);
        flash.success(String.format("%s events removed.", ids.size()));
        return seeOther("/eapp/events/form/delete");

    }

    @POST
    @Path("events/form")
    @RequiresRoles("admin")
    @Timed
    public Response postFormEvents(@InjectParam Collection<Event> events) {

        DB.merge(events);
        flash.success(String.format("%s events saved successfully.", events.size()));
        return seeOther("/eapp/events/form");

    }

    @GET
    @Path("sample")
    @Timed
    public Response etag(@Context Request request) {

        return view("/eapp/home", DB.events()).tag(request).build();

    }

    @GET
    @Path("event/delete/{id}")
    @RequiresRoles("admin")
    @Timed
    public Response deleteEvent(@PathParam("id") Integer id) {

        DB.removeEvent(id);
        return seeOther(HOME);

    }

    @POST
    @Path("event")
    @RequiresRoles("admin")
    @Timed
    public View postFormEvent(@InjectParam Event model) {

        Errors<Event> errors = BeanValidator.validate(model);
        if (errors.hasErrors()) {
            return view(model).add("errors", errors);
        } else {
            DB.save(model);
            return view(model).add("success", true);
        }

    }

    @POST
    @RequiresUser
    @Path("reservation")
    @Timed
    public View postFormReservation(@InjectParam Reservation model) {

        Errors<Reservation> errors = BeanValidator.validate(model);
        if (errors.hasErrors()) {
            return view(model).add("errors", errors);
        } else {
            DB.save(model);
            return view(model).add("success", true);
        }

    }

}
