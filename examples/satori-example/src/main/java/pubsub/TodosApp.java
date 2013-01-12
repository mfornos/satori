package pubsub;

import static satori.utils.ResponseUtils.ok;
import static satori.utils.ResponseUtils.view;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.UserForm;
import models.todos.Todo;

import org.atmosphere.annotation.Broadcast;
import org.atmosphere.annotation.Suspend;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.jersey.Broadcastable;

import satori.validation.BeanValidator;
import satori.validation.Errors;
import satori.view.View;

@Path("/todos")
public class TodosApp {

    @Context BroadcasterFactory factory;

    @GET
    @Path("suscribe/{topic}")
    @Suspend(contentType = MediaType.APPLICATION_JSON)
    public Broadcastable suscribe(@PathParam("topic") Broadcaster topic) {
        
        return new Broadcastable(topic);
        
    }

    @DELETE
    @Broadcast
    @Path("{id:\\d+}")
    public Broadcastable delete(@PathParam("id") Integer id) {

        Todo.delete(id);
        return new Broadcastable(id, factory.lookup("deletes"));

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(Todo todo) {

        todo.save();
        return ok();

    }

    @PUT
    @Broadcast
    @Path("{id:\\d+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Broadcastable put(@PathParam("id") Integer id, Todo todo) {

        BeanValidator.validate(todo).croak().save();
        return new Broadcastable(todo, factory.lookup("updates"));

    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Todo> todos() {

        return Todo.all();

    }

    @GET
    public View home() {

        return view();

    }

    @POST
    @Path("signin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserForm jsonSignin(UserForm form, @Context HttpServletRequest request) {

        Errors<UserForm> errors = BeanValidator.validate(form);
        if (errors.hasErrors()) {
            form.setLoginAccepted(false);
            return form;
        }

        return form.uncatchAuthenticate(request);

    }

}
