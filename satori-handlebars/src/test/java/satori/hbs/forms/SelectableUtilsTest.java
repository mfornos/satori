package satori.hbs.forms;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import satori.hbs.forms.Selectable;
import satori.hbs.forms.SelectableUtils;

public class SelectableUtilsTest {

    private enum TestEnum {
        ONE, TWO, THREE
    }

    @Test
    public void testFromEnum() {

        TestEnum t = TestEnum.ONE;
        Collection<Selectable> selectModel = SelectableUtils.fromEnum(t.getClass());
        assertNotNull(selectModel);
        assertEquals(3, selectModel.size());
        assertEquals("ONE", selectModel.iterator().next().selectLabel());

    }

    @Test
    public void testFromString() {

        Selectable[] serie = SelectableUtils.fromString("1..200").toArray(new Selectable[] {});
        assertNotNull(serie);
        assertEquals(200, serie.length);
        assertEquals("1", serie[0].selectValue());
        assertEquals("200", serie[199].selectValue());

        serie = SelectableUtils.fromString("1..2").toArray(new Selectable[] {});
        assertNotNull(serie);
        assertEquals(2, serie.length);
        assertEquals("1", serie[0].selectValue());
        assertEquals("2", serie[1].selectValue());

        serie = SelectableUtils.fromString("0..2").toArray(new Selectable[] {});
        assertNotNull(serie);
        assertEquals(3, serie.length);
        assertEquals("0", serie[0].selectValue());
        assertEquals("2", serie[2].selectValue());

        serie = SelectableUtils.fromString("10..20").toArray(new Selectable[] {});
        assertNotNull(serie);
        assertEquals(11, serie.length);
        assertEquals("10", serie[0].selectValue());
        assertEquals("20", serie[10].selectValue());

        serie = SelectableUtils.fromString("20..10").toArray(new Selectable[] {});
        assertNotNull(serie);
        assertEquals(11, serie.length);
        assertEquals("20", serie[0].selectValue());
        assertEquals("10", serie[10].selectValue());

        try {
            SelectableUtils.fromString("-10");
            fail("Malformed expression must throw and exception");
        } catch (IllegalArgumentException ex) {

        }

        try {
            SelectableUtils.fromString("-10..0");
            fail("Malformed expression must throw and exception");
        } catch (IllegalArgumentException ex) {

        }

    }

    @Test
    public void testFromReflection() {
        Set<Car> cars = new HashSet<Car>();

        for (int i = 0; i < 5; i++) {
            cars.add(new Car("Model" + i, new Long(i)));
        }

        Collection<Selectable> sel = SelectableUtils.fromCollection(cars, "model", "id");

        assertNotNull(sel);
        assertEquals(5, sel.size());
        Selectable s = sel.iterator().next();
        assertTrue(s.selectLabel().startsWith("Model"));
        long id = (Long) s.selectValue();
        assertTrue(id > -1 && id < 5);

    }

    public static class Car {
        private final String model;
        private final Long id;

        public String getModel() {
            return model;
        }

        public Long getId() {
            return id;
        }

        public Car(String model, Long id) {
            this.model = model;
            this.id = id;
        }
    }

}
