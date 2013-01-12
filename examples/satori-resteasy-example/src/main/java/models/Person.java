package models;

import java.util.ArrayList;
import java.util.List;

import com.tinkerpop.blueprints.Vertex;

public class Person {

    public final String name;
    public final String email;
    public Object id;

    public final List<Person> followers;
    public final List<Person> follows;

    public Person(Object id) {
        this(id, null, null);
    }

    public Person(Object id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.followers = new ArrayList<Person>();
        this.follows = new ArrayList<Person>();
    }

    public Person(String name, String email) {
        this(null, name, email);
    }

    public Person(Vertex vertex) {
        this(vertex.getId(), (String) vertex.getProperty("name"), (String) vertex.getProperty("email"));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public String getEmail() {
        return email;
    }

    public List<Person> getFollowers() {
        return followers;
    }

    public List<Person> getFollows() {
        return follows;
    }

    public Object getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public void setId(Object id) {
        this.id = id;
    }

}
