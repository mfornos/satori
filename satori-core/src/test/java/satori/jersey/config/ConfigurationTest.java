package satori.jersey.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import satori.config.Configuration;

public class ConfigurationTest {

    private enum ENUM {
        ONE, TWO
    }

    @Test
    public void test() {
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("hello", "world");

        Configuration config = new Configuration();
        config.loadYaml("test.yml").loadResourceConfig(map);

        Assert.assertEquals("dbuserdevelopment", config.child("development").get("dbuser"));
        Assert.assertEquals(new Integer(2), config.child("development").get("one", 2));
        Assert.assertEquals(new Boolean(true), config.child("none").get("one", true));
        Assert.assertTrue(config.has("development"));
        Assert.assertFalse(config.has("empty"));
        Assert.assertEquals(ENUM.TWO, config.get("num", ENUM.ONE));
        Assert.assertEquals("world", config.get("hello"));
        List<String> classes = config.get("classes");
        Assert.assertEquals(2, classes.size());
        
    }

}
