package satori.view;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;


public class HandlebarsViewTest extends JerseyTest {

    public HandlebarsViewTest() {
        super("satori.controllers");
    }

    @Test
    public void testHello() {

        WebResource webResource = resource();
        ClientResponse response = webResource.path("helloworld/hello").get(ClientResponse.class);
        MultivaluedMap<String, String> headers = response.getHeaders();
        assertEquals("text/html", headers.get("content-type").get(0));
        String responseMsg = response.getEntity(String.class);
        assertEquals("Hello World áàíôü!\n", responseMsg);

    }

    @Test
    public void testInheritance() {

        WebResource webResource = resource();
        ClientResponse response = webResource.path("helloworld").get(ClientResponse.class);
        MultivaluedMap<String, String> headers = response.getHeaders();
        assertEquals("text/html", headers.get("content-type").get(0));
        String responseMsg = response.getEntity(String.class);
        InputStream is = getClass().getResourceAsStream("/views/home.expected");
        String expected = new java.util.Scanner(is).useDelimiter("\\A").next();
        assertEquals(expected, responseMsg);

    }

}
