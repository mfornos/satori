package satori.hbs.forms;

public class SimpleSelectable implements Selectable {

    private final String label;
    private final Object value;

    public SimpleSelectable(String label) {
        this(label, label);
    }

    public SimpleSelectable(String label, Object value) {
        this.label = label;
        this.value = value;
    }

    @Override
    public String selectLabel() {
        return label;
    }

    @Override
    public Object selectValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SimpleSelectable [label=" + label + ", value=" + value + "]";
    }

}
