package satori.utils;

public class LoadUtils {
    
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = LoadUtils.class.getClassLoader();
        }
        return cl;
    }
    
}
