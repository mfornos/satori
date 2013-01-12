package satori.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import satori.utils.PathUtils;

public class PathUtilsTest {

    @Test
    public void test() {
        
        assertEquals("/1/2/3", PathUtils.absolutize("1/2/3"));
        assertEquals("/1", PathUtils.absolutize("1"));
        assertEquals("3", PathUtils.relativize("/3"));
        assertEquals("1/2/3", PathUtils.relativize("/1/2/3"));
        assertEquals("//1/2/3", PathUtils.relativize("///1/2/3"));
        assertEquals("", PathUtils.relativize(""));
        assertEquals("/", PathUtils.absolutize(""));
        assertEquals(null, PathUtils.relativize(null));
        assertEquals("/null", PathUtils.absolutize(null));
        
    }

}
