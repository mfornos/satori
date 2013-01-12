package satori.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Helper class for HTML generation.
 */
public class Html {

    /**
     * @return a new Html builder instance.
     */
    public static Html builder() {
        return new Html();
    }

    private final StringBuilder builder;
    private final LinkedList<String> openElements;
    private boolean openTag = false;

    private Html() {
        this.builder = new StringBuilder();
        openElements = new LinkedList<String>();
    }

    /**
     * Add attribute
     * 
     * @param name
     *            Attribute name
     * @param value
     *            Attribute value
     * @return this builder
     */
    public Html attr(String name, String value) {
        if (value != null) {
            builder.append(' ');
            builder.append(name);
            builder.append("=\"");
            builder.append(value);
            builder.append('\"');
        }
        return this;
    }

    /**
     * Add attributes
     * 
     * @param attrs
     *            Attribute name, Attribute value Strings series
     * @return this builder
     */
    public Html attrs(String... attrs) {
        if (attrs != null && (attrs.length % 2 == 0)) {
            for (int i = 0; i < attrs.length - 1; i++) {
                attr(attrs[i], attrs[++i]);
            }
        }
        return this;
    }

    /**
     * Begin an element
     * 
     * @param element
     *            Tag name
     * @return this builder
     */
    public Html begin(String element) {
        checkOpenTag();
        builder.append('<');
        builder.append(element);
        openElements.offerFirst(element);
        openTag = true;
        return this;
    }

    /**
     * Closes a begin tag
     * 
     * @return this builder
     */
    public Html close() {
        if (openTag) {
            builder.append('>');
            openTag = false;
        }
        return this;
    }

    /**
     * Emits the generated HTML. Closes orphan tags if needed.
     * 
     * @return String with the generated HTML
     */
    public String emit() {
        endAll();
        return builder.toString();
    }

    /**
     * Ends the current element if any.
     * 
     * @return this builder
     */
    public Html end() {
        if (!openElements.isEmpty()) {
            end(openElements.peek());
        }
        return this;
    }

    /**
     * End an element.
     * 
     * @param element
     *            Tag name
     * @return this builder
     */
    public Html end(String element) {
        if (openTag) {
            builder.append(" />");
            openTag = false;
        } else {
            builder.append("</");
            builder.append(element);
            builder.append('>');
        }
        openElements.remove(element);
        return this;
    }

    /**
     * Closes all remaining open elements
     * 
     * @return this builder
     */
    public Html endAll() {
        if (!openElements.isEmpty()) {
            List<String> oel = new ArrayList<String>(openElements);
            for (String el : oel) {
                end(el);
            }
        }
        return this;
    }

    /**
     * Write text
     * 
     * @param text
     *            Text (can be null)
     * @return this builder
     */
    public Html text(String text) {
        if (text != null) {
            checkOpenTag();
            builder.append(text);
        }
        return this;
    }

    /**
     * Write verbatim
     * 
     * @param text
     *            Text
     * @return this builder
     */
    public Html write(Object text) {
        checkOpenTag();
        builder.append(text);
        return this;
    }

    private void checkOpenTag() {
        if (openTag) {
            close();
        }
    }

}