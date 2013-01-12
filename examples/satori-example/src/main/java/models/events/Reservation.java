package models.events;

import java.util.Collection;

import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;

import org.apache.bval.constraints.Email;
import org.apache.bval.constraints.NotEmpty;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;

import db.DB;

public class Reservation {
    public enum EventType {
        ZERO, ONE, TWO, THREE
    }

    @NotEmpty @FormParam("name") private String name;

    @NotEmpty @Email @FormParam("email") private String email;

    @NotNull @FormParam("event") private Integer eventId;

    @FormParam("type") private EventType type;

    @FormParam("id") private Integer id;

    @FormParam("owner") private String owner;

    @Context private Subject subject;

    public Reservation() {
        this.type = EventType.ONE;
        this.owner = "admin";
    }

    public Reservation(Integer id, String name, String email, Integer eventId) {
        this();
        this.id = id;
        this.name = name;
        this.email = email;
        this.eventId = eventId;
    }

    public Reservation(Integer eventId) {
        this(null, null, null, eventId);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Event getEvent() {
        return DB.getEvent(eventId);
    }

    public void setEvent(Event event) {
        this.eventId = event.getId();
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer categoryId) {
        this.eventId = categoryId;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Event> getEvents() {
        return DB.events();
    }

    public boolean isOwner(Object principal) {
        return owner != null && owner.equals(principal);
    }

    public void handleSecurity() {
        if (subject != null) {
            if (!(id == null || subject.hasRole("admin") || DB.getReservation(id).isOwner(subject.getPrincipal()))) {
                throw new UnauthorizedException(String.format(
                        "'%s' does not have permissions to alter reveservation '%s'", subject.getPrincipal(), id));
            }
            this.owner = subject.getPrincipal().toString();
        }
    }
}
