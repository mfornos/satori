package satori.config;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import satori.utils.LoadUtils;

public class Configuration {

    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    public static Configuration from(Map<String, Object> properties) {

        return new Configuration(properties);

    }

    public static Configuration from(String yamlFile) {

        return new Configuration(yamlToMap(yamlFile));

    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> yamlToMap(String path) {

        Yaml yaml = new Yaml();
        InputStream in = LoadUtils.getDefaultClassLoader().getResourceAsStream(path);
        if (in == null) {
            log.warn("'{}' not found in classpath root", path);
            return Collections.emptyMap();
        } else {
            return (Map<String, Object>) yaml.load(in);
        }

    }

    private final Map<String, Object> map;

    public Configuration() {

        this.map = new HashMap<String, Object>();

    }

    public Configuration(Map<String, Object> map) {

        this.map = map;

    }

    @SuppressWarnings("unchecked")
    public Configuration child(String property) {

        return new Configuration((Map<String, Object>) (map.containsKey(property) ? map.get(property)
                : Collections.emptyMap()));

    }

    @SuppressWarnings("unchecked")
    public <T> T get(String property) {

        try {
            return (T) map.get(property);
        } catch (Exception ex) {
            throw new ClassCastException(String.format("Error casting property '%s'. Try getAs() method.", property));
        }

    }

    @SuppressWarnings("unchecked")
    public <T> T get(String property, T defaultValue) {

        return map.containsKey(property) ? (T) getAs(property, defaultValue.getClass()) : defaultValue;

    }

    public <T> T getAs(String property, Class<? extends T> target) {

        return to(map.get(property), target);

    }

    public boolean has(String name) {

        return map.containsKey(name) && map.get(name) != null;

    }

    public Configuration loadResourceConfig(Map<String, Object> properties) {

        map.putAll(properties);
        return this;

    }

    public Configuration loadYaml(String path) {

        map.putAll(yamlToMap(path));
        return this;

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <T> T to(Object object, Class<? extends T> target) {

        if (target.isEnum()) {
            return (T) Enum.valueOf((Class<? extends Enum>) target, (String) object);
        }

        try {
            return target.cast(object);
        } catch (Exception ex) {
            return (T) ConvertUtils.convert(object, target);
        }

    }

}
