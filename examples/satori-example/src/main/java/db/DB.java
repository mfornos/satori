package db;

import helpers.SampleHelpers;
import humanize.text.EmojiInterpolator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import models.events.Event;
import models.events.Location;
import models.events.Reservation;
import satori.utils.BeanUtilsExt;
import satori.validation.BeanValidator;
import satori.validation.Errors;

public class DB {
    private static final String EMOJI_PATTERN = "<img src='{{'@\"assets/styles/img/emojis/{0}.png\"'}}' class=\"emoji\" alt=\"{0}\" />";

    public static final Reservation EMPTY_RESERV = new Reservation();
    public static final Event EMPTY_EVENT = new Event();

    private static Map<Integer, Event> events = new TreeMap<Integer, Event>();
    private static Map<Integer, Reservation> reservations = new TreeMap<Integer, Reservation>();
    
    private static final AtomicInteger autoIncrement = new AtomicInteger(3);

    static {
        save(new Event(
                1,
                "Psych Rock Summer Show",
                "Continuing in the summer of psychedelic, indie rock goodness... :metal: Grand Atlantic will be joined by The Moderns (Gold Coast) and Howling Rabbits to kick off 2012 in style at The Beetle Bar, Brisbane.",
                "The Beetle Bar, Brisbane", new Location("-27.466451,153.01378")));
        save(new Event(
                2,
                "A musical smorgasbord",
                "A musical smorgasbord of indie/rock/psychobilly/rockabilly/surf/psychedelic flavours guaranteed to tickle the ear-buds. Couple this with the prestige of The Zoo, and you have a match made in heaven.",
                "The Zoo, Fortitude Valley", new Location("-27.457818,153.036069")));
        save(new Event(
                3,
                "Rusko at Rain",
                "VIP All Access Wristband– includes complementary express entry to Moon, Rain, & Ghostbar. Customer must sign for and pick up wristband inside Stuff St.:beer:",
                " Palms Resort & Casino: Rain, Las Vegas, NV", new Location("36.127615,-115.194054")));

        save(new Reservation(1, "Koala Superexpert", "test@superexpert.com", 1));
        save(new Reservation(2, "Tricky", "mfornos@amneris.es", 1));
        save(new Reservation(3, "Octocat", "octocat@github.com", 2));
    }

    public static Collection<Event> events() {
        List<Event> values = new ArrayList<Event>(events.values());
        Collections.reverse(values);
        return values;
    }

    public static Event getEvent(Integer id) {
        Event event = events.get(id);
        return (event == null) ? EMPTY_EVENT : event;
    }

    public static Reservation getReservation(Integer id) {
        return reservations.get(id);
    }

    public static Collection<Reservation> getReservations(Event event) {
        Collection<Reservation> qs = new ArrayList<Reservation>();
        for (Reservation q : reservations.values()) {
            if (q.getEventId().intValue() == event.getId().intValue()) {
                qs.add(q);
            }
        }
        return qs;
    }

    public static void merge(Collection<Event> events) {
        for (Event event : events) {
            Integer eid = event.getId();
            if (eid == null) {
                mergeNew(event);
            } else {
                mergeExistent(event, eid);
            }
        }
    }

    public static Event removeEvent(Integer id) {
        Collection<Reservation> rs = getReservations(getEvent(id));
        for (Reservation r : rs) {
            removeReservation(r.getId());
        }
        return events.remove(id);
    }

    public static void removeEvents(List<Integer> ids) {
        for (int id : ids) {
            removeEvent(id);
        }
    }

    public static Reservation removeReservation(Integer id) {
        return reservations.remove(id);
    }

    public static Collection<Reservation> reservations() {
        return reservations.values();
    }

    public static void save(Event event) {
        if (event.getId() == null)
            event.setId(autoIncrement.incrementAndGet());
        event.setDescription(SampleHelpers.render(EmojiInterpolator.interpolateAlias(EMOJI_PATTERN, event.getDescription())));
        events.put(event.getId(), event);
    }

    public static void save(Reservation reservation) {
        reservation.handleSecurity();

        if (reservation.getId() == null)
            reservation.setId(autoIncrement.incrementAndGet());

        reservations.put(reservation.getId(), reservation);
    }

    private static Event mergeExistent(Event event, Integer eid) {
        Event mergeInto = new Event(events.get(eid));

        if (mergeInto != null) {
            BeanUtilsExt.copyNonNullProperties(event, mergeInto);
            Errors<Event> errors = BeanValidator.validate(mergeInto);
            if (errors.hasErrors()) {
                // notify errors
            } else {
                save(mergeInto);
            }
        }

        return mergeInto;
    }

    private static void mergeNew(Event event) {
        Errors<Event> errors = BeanValidator.validate(event);
        if (errors.hasErrors()) {
            // notify errors
        } else {
            save(event);
        }
    }

}
