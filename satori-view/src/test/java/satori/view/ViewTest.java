package satori.view;

import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response.ResponseBuilder;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import satori.utils.ResponseUtils;

public class ViewTest {

    @Mock private Request request;

    @SuppressWarnings("unchecked")
    @Test
    public void test() {

        View v = ResponseUtils.view();
        Assert.assertNotNull(v);
        Assert.assertEquals("/test", v.getTemplateName());

        v = ResponseUtils.view(this);
        Assert.assertNotNull(v);
        Assert.assertFalse(v.hasMap());
        Assert.assertEquals(this, v.getModel());
        Assert.assertTrue(v.getMap().isEmpty());

        v.with("hi", "ho");
        Assert.assertTrue(v.hasMap());
        Assert.assertEquals(this, ((Map<String, Object>) v.getModel()).get("model"));
        Assert.assertEquals("ho", ((Map<String, Object>) v.getModel()).get("hi"));
        Assert.assertEquals(2, ((Map<String, Object>) v.getModel()).size());

        Assert.assertEquals(200, v.ok().getStatus());

    }

    @Test
    public void etagTest() {
        
        MockitoAnnotations.initMocks(this);

        View v = ResponseUtils.view(this);
        Assert.assertNotNull(v);
        ResponseBuilder rb = v.tag(request);
        Assert.assertNotNull(rb);
        MultivaluedMap<String, Object> metadata = rb.build().getMetadata();
        Assert.assertTrue(metadata.containsKey("Etag"));

    }

}
