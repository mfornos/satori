package satori.jersey.utils;

import static org.junit.Assert.assertEquals;

import org.apache.commons.beanutils.ConvertUtils;
import org.junit.Test;


public class ConvertUtilsTest {
    @Test
    public void test() {

        assertEquals(true, ConvertUtils.convert("true", Boolean.class));

        assertEquals(new Integer(1), ConvertUtils.convert("1", Integer.class));

        assertEquals(new Double(1.1), ConvertUtils.convert("1.1", Double.class));

        assertEquals("true", ConvertUtils.convert(true));

        assertEquals("10.89", ConvertUtils.convert(10.89));

        assertEquals("10", ConvertUtils.convert(10));

        int i = (Integer) ConvertUtils.convert(Boolean.TRUE, Integer.class);
        assertEquals(1, i);

    }

    private enum COLOR {
        RED, WHITE
    }

    @Test
    public void enumTest() {

        assertEquals("RED", ConvertUtils.convert(COLOR.RED));
        
    }
}
