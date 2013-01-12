package db;

import java.util.ArrayList;
import java.util.List;

import models.Person;

import com.orientechnologies.orient.core.id.ORecordId;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GraphDB {

    static {
        Graph graph = new OrientGraph("local:target/orient");
        try {
            if (!graph.getVertices().iterator().hasNext()) {
                Vertex admin = add(graph, new Person("admin", "root@gmail.com"));
                Vertex user = add(graph, new Person("user", "user@user.com"));
                Vertex dark = add(graph, new Person("darkhelmet", "darkhelmet@dark.com"));
                Vertex green = add(graph, new Person("green", "colors@gmail.com"));
                graph.addEdge(null, admin, user, "follows");
                graph.addEdge(null, user, admin, "follows");
                graph.addEdge(null, dark, green, "follows");
            }
        } finally {
            graph.shutdown();
        }
    }

    public static Vertex add(Graph g, Person p) {

        Vertex v = g.addVertex(p);
        v.setProperty("name", p.name);
        v.setProperty("email", p.email);
        return v;

    }

    public static Iterable<Person> all() {
        return execute(new Command<Iterable<Person>>() {
            @Override
            public Iterable<Person> call(Graph graph) {
                List<Person> persons = new ArrayList<Person>();
                Iterable<Vertex> vertices = graph.getVertices();
                for (Vertex v : vertices) {
                    persons.add(expand(v));
                }
                return persons;
            }
        });

    }

    public static Person byId(final Object id) {
        return execute(new Command<Person>() {
            @Override
            public Person call(Graph graph) {
                Vertex vertex = graph.getVertex(id);
                return (vertex == null) ? null : expand(vertex);
            }
        });
    }

    public static Person byName(final String name) {
        return execute(new Command<Person>() {
            @Override
            public Person call(Graph graph) {
                Iterable<Vertex> result = graph.getVertices("name", name);
                return (result.iterator().hasNext()) ? expand(result.iterator().next()) : null;
            }
        });
    }

    public static <T> T execute(Command<T> cmd) {
        Graph graph = new OrientGraph("local:target/orient");
        try {
            return cmd.call(graph);
        } finally {
            graph.shutdown();
        }
    }

    public static Person follow(final Person a, final Person b) {
        execute(new Command<Object>() {
            @Override
            public Object call(Graph graph) {
                return graph.addEdge(null, graph.getVertex(a.getId()), graph.getVertex(b.getId()), "follows");
            }
        });

        return byId(b.id);
    }

    public static Person follow(String name, ORecordId id) {
        return follow(GraphDB.byName(name), GraphDB.byId(id));
    }

    public static Person unfollow(final Person src, final Person dst) {
        execute(new Command<Object>() {

            @Override
            public Object call(Graph graph) {
                Vertex a = graph.getVertex(src.getId());
                Vertex b = graph.getVertex(dst.getId());

                Iterable<Edge> relations = a.getEdges(Direction.OUT);
                for (Edge e : relations) {
                    if (e.getVertex(Direction.IN).getId().equals(b.getId())) {
                        graph.removeEdge(e);
                    }
                }

                return null;
            }
        });

        return byId(dst.id);
    }

    public static Person unfollow(String name, ORecordId id) {
        return unfollow(GraphDB.byName(name), GraphDB.byId(id));
    }

    protected static Person expand(Vertex vertex) {
        Person p = new Person(vertex);
        Iterable<Edge> relations = vertex.getEdges(Direction.IN);
        for (Edge e : relations) {
            p.followers.add(new Person(e.getVertex(Direction.OUT)));
        }

        relations = vertex.getEdges(Direction.OUT);
        for (Edge e : relations) {
            p.follows.add(new Person(e.getVertex(Direction.IN)));
        }

        return p;
    }

}
