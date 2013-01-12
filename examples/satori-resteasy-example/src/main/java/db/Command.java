package db;

import com.tinkerpop.blueprints.Graph;

public interface Command<T> {
    T call(Graph graph);
}
