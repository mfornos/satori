package satori.utils;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.junit.Test;

import satori.utils.ResponseUtils;
import satori.view.View;

public class ResponseUtilsTest {

    @Test
    public void test() {

        assertEquals("/test", tn(ResponseUtils.view()));
        assertEquals("/test", tn(ResponseUtils.view("test")));
        assertEquals("/test", tn(ResponseUtils.view("/test")));
        assertEquals("/hello/world", tn(new Hello().world()));
        assertEquals("/hello/world/again", tn(new Hello().worldAgain()));
        assertEquals("/hello/10", loc(ResponseUtils.seeOther("/hello/{id}", 10)));
        assertEquals("/hello/10/world/20", loc(ResponseUtils.seeOther("/hello/{id}/world/{wid}", 10, 20)));
        assertEquals("/hello/10", loc(ResponseUtils.seeOther(Hello.class, "/{id}", 10)));
        assertEquals(200, ResponseUtils.ok().getStatus());
        
    }

    private String loc(Response rp) {
        return rp.getMetadata().getFirst("Location").toString();
    }

    private String tn(View view) {
        return view.getTemplateName();
    }

    @Path("/hello")
    static class Hello {
        View world() {
            return ResponseUtils.view();
        }

        View worldAgain() {
            return ResponseUtils.view();
        }
    }

}
