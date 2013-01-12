package satori.utils;

import org.apache.commons.lang3.StringUtils;

public class PathUtils {
    
    public static String relativize(String path) {
        return StringUtils.isNotBlank(path) && path.charAt(0) == '/' ? path.substring(1) : path;
    }

    public static String absolutize(String path) {
        return StringUtils.isBlank(path) || path.charAt(0) != '/' ? '/' + path : path;
    }
    
}
