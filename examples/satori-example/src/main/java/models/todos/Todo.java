package models.todos;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.constraints.Size;

public class Todo {

    @Size(max = 512) private String title;

    private Integer order;

    private boolean done;

    public Todo() {

    }

    public Todo(String title, Integer order, boolean done) {
        this.title = title;
        this.order = order;
        this.done = done;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    // ----

    public static Collection<Todo> all() {
        return map.values();
    }

    public void save() {
        map.put(order, this);
    }

    public static void delete(Integer id) {
        map.remove(id);
    }

    private static Map<Integer, Todo> map = new TreeMap<Integer, Todo>();

    static {
        map.put(1, new Todo("Something about Bill Evans.", 1, false));
    }

}
