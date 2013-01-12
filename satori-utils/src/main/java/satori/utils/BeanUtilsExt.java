package satori.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

public class BeanUtilsExt {
    private static final String CLASS_PROPERTY = "class";

    /**
     * Copies all the non-null properties from src to dst.
     */
    public static <T> void copyNonNullProperties(T src, T dst) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> properties = PropertyUtils.describe(src);
            for (String key : properties.keySet()) {
                if (!CLASS_PROPERTY.equals(key)) { // By default class property
                                                   // exists and is excluded
                    Object value = PropertyUtils.getProperty(src, key);
                    // property has a value and it's writable
                    if (value != null && PropertyUtils.getPropertyDescriptor(dst, key).getWriteMethod() != null) {
                        PropertyUtils.setProperty(dst, key, value);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
