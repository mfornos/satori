package models.events;

import java.util.Collection;
import java.util.Date;

import javax.ws.rs.FormParam;

import org.apache.bval.constraints.NotEmpty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import satori.hbs.forms.Selectable;
import satori.jersey.params.DateTimeParam;
import validators.AfterDate;
import db.DB;

@JsonIgnoreProperties({ "questions" })
@AfterDate(start = "dateStart", end = "dateEnd")
public class Event implements Selectable {

    private @NotEmpty @FormParam("title") String title;

    private @NotEmpty @FormParam("description") String description;

    private @FormParam("id") Integer id;

    private @FormParam("dateStart") DateTimeParam dateStart;

    private @FormParam("dateEnd") DateTimeParam dateEnd;

    private @FormParam("address") String address;

    private @FormParam("location") Location location;

    public Event() {
        Date date = new Date();
        this.dateStart = new DateTimeParam(date);
        this.dateEnd = new DateTimeParam(new Date(date.getTime() + 1000 * 60 * 60)); // +1h
    }

    public Event(Integer id, String title, String description, String address, Location location) {
        this();
        this.id = id;
        this.title = title;
        this.description = description;
        this.address = address;
        this.location = location;
    }

    public Event(Event event) {
        this(event.getId(), event.getTitle(), event.getDescription(), event.getAddress(), event.getLocation());
        setDateStart(event.getDateStart());
        setDateEnd(event.getDateEnd());
    }

    public String getAddress() {
        return address;
    }

    public Date getDateEnd() {
        return dateEnd.get();
    }

    public Date getDateStart() {
        return dateStart.get();
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public Collection<Reservation> getReservations() {
        return DB.getReservations(this);
    }

    public int getReservationsNum() {
        return DB.getReservations(this).size();
    }

    public String getTitle() {
        return title;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDateEnd(Date date) {
        this.dateEnd = new DateTimeParam(date);
    }

    public void setDateStart(Date date) {
        this.dateStart = new DateTimeParam(date);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String selectLabel() {
        return title;
    }

    @Override
    public Object selectValue() {
        return id;
    }

}
