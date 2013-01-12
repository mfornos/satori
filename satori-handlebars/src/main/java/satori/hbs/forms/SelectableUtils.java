package satori.hbs.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;

public class SelectableUtils {

    private static final Pattern SERIE_EXPR = Pattern.compile("(\\d+)\\.{2,3}(\\d+)");

    public static Collection<Selectable> fromCollection(Collection<?> objects) {

        Collection<Selectable> selectables = new ArrayList<Selectable>();

        for (Object o : objects) {
            String string = ConvertUtils.convert(o);
            selectables.add(new SimpleSelectable(string, string));
        }

        return selectables;

    }

    public static Collection<Selectable> fromCollection(Collection<?> objects, String valueField) {

        return fromCollection(objects, null, valueField);

    }

    public static Collection<Selectable> fromCollection(Collection<?> objects, String labelField, String valueField) {
        Collection<Selectable> selectables = new ArrayList<Selectable>();

        try {
            for (Object o : objects) {
                Object value = PropertyUtils.getProperty(o, valueField);
                Object label = (labelField == null) ? value : PropertyUtils.getProperty(o, labelField);
                selectables.add(new SimpleSelectable(label.toString(), value));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return selectables;
    }

    public static Collection<Selectable> fromEnum(Class<?> clazz) {
        try {
            Collection<Selectable> res = new ArrayList<Selectable>();

            for (Object e : clazz.getEnumConstants()) {
                res.add(new SimpleSelectable(e.toString()));
            }

            return res;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Collection<Selectable> fromString(String expr) {
        Matcher matcher = SERIE_EXPR.matcher(expr);
        if (matcher.matches()) {
            int from = Integer.parseInt(matcher.group(1));
            int to = Integer.parseInt(matcher.group(2));
            return buildSerie(from, to);
        } else {
            throw new IllegalArgumentException(String.format("'%s' is not a valid expression. V.gr. '0..50'"));
        }
    }

    private static Collection<Selectable> buildSerie(int from, int to) {
        Collection<Selectable> serie = new ArrayList<Selectable>();
        if (from > to) {
            while (to <= from) {
                serie.add(new SimpleSelectable(Integer.toString(from--)));
            }
        } else {
            while (from <= to) {
                serie.add(new SimpleSelectable(Integer.toString(from++)));
            }
        }
        return serie;
    }

}
