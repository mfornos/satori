package satori.jersey.spi.readers;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.core.util.ReaderWriter;

@Provider
@Consumes({ "application/x-www-form-urlencoded", "*/*" })
public class BeanCollectionReader implements MessageBodyReader<Collection<?>> {

    @Override
    public boolean isReadable(Class<?> paramClass, Type paramType, Annotation[] paramArrayOfAnnotation,
            MediaType paramMediaType) {
        return (Collection.class.isAssignableFrom(paramClass));
    }

    @Override
    public Collection<?> readFrom(Class<Collection<?>> paramClass, Type paramType, Annotation[] paramArrayOfAnnotation,
            MediaType paramMediaType, MultivaluedMap<String, String> paramMultivaluedMap, InputStream paramInputStream)
            throws IOException, WebApplicationException {

        MultivaluedMap<String, String> map = newMapFromRequest(paramMediaType, paramInputStream);

        if (map.isEmpty()) {
            return Collections.emptyList();
        }

        List<Object> beans = new ArrayList<Object>();
        Set<String> keySet = map.keySet();

        Class<?> clazz = (Class<?>) ((ParameterizedType) paramType).getActualTypeArguments()[0];
        int beansNum = map.values().iterator().next().size();

        for (int i = 0; i < beansNum; i++) {
            try {
                beans.add(clazz.newInstance());
            } catch (Exception e) {
                throw new WebApplicationException(e);
            }
        }

        BeanUtilsBean bu = BeanUtilsBean2.getInstance();
        for (String key : keySet) {
            List<String> values = map.get(key);
            int i = 0;
            for (String value : values) {
                Object bean = beans.get(i++);
                try {
                    bu.setProperty(bean, key, value);
                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
            }
        }

        return beans;
    }

    private MultivaluedMap<String, String> newMapFromRequest(MediaType paramMediaType, InputStream paramInputStream)
            throws IOException, UnsupportedEncodingException {
        String encoded = ReaderWriter.readFromAsString(paramInputStream, paramMediaType);
        MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        final String charsetName = ReaderWriter.getCharset(paramMediaType).name();

        final StringTokenizer tokenizer = new StringTokenizer(encoded, "&");
        String token;
        try {
            while (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken();
                int idx = token.indexOf('=');
                if (idx < 0) {
                    map.add(URLDecoder.decode(token, charsetName), null);
                } else if (idx > 0) {
                    map.add(URLDecoder.decode(token.substring(0, idx), charsetName),
                            URLDecoder.decode(token.substring(idx + 1), charsetName));
                }
            }
            return map;
        } catch (IllegalArgumentException ex) {
            throw new WebApplicationException(ex, Status.BAD_REQUEST);
        }
    }

}
