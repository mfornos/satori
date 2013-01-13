package satori.view;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import satori.crypto.Crypter;
import satori.security.CsrfHelper;
import satori.utils.MurmurHash;

/**
 * Default {@link SatoriView} implementation. Holds the state and the template
 * to render. Also offers some utility methods for building the wrapping
 * request, like ETags and CSRF prevention.
 * 
 */
public class View implements SatoriView {

    private final Map<String, Object> map = new HashMap<String, Object>();

    private final String templateName;

    private String csrfToken;

    private Object model;

    public View(String templateName) {
        this(templateName, null);
    }

    public View(String templateName, Object model) {
        this.templateName = templateName;
        this.model = model;
    }

    public ResponseBuilder builder() {
        return Response.ok(this);
    }

    /**
     * Updates the CSRF prevention token of this view and sets a cookie as
     * storage for later matching.
     * 
     * @return a response builder
     */
    public ResponseBuilder csrfToken() {

        this.csrfToken = CsrfHelper.newCsrfToken();
        return Response.ok(this).cookie(
                new NewCookie(CsrfHelper.CSRF_TOKEN_KEY, csrfToken, "/", null, 1, null, -1, false));

    }

    @Override
    public String getCsrfToken() {

        return csrfToken;

    }

    /**
     * @return the underlying model map
     */
    public Map<String, Object> getMap() {
        return map;
    }

    @Override
    public Object getModel() {
        if (map.isEmpty()) {
            return model;
        } else {
            map.put("model", model);
            return map;
        }
    }

    @Override
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @return true if the underlying model map is not null or empty
     */
    public boolean hasMap() {
        return !(map == null || map.isEmpty());
    }

    /**
     * Builds an HTTP 200 OK response.
     * 
     * @return HTTP 200 response with this view as entity
     */
    public Response ok() {
        return builder().build();
    }

    /**
     * Sends an HTTP 304 Not Modified status response if the request ETag
     * matches the ETag of the current resource, meaning that the entity has not
     * changed.
     * 
     * @param request
     *            the request to be evaluated
     * @return a response builder
     */
    public ResponseBuilder tag(Request request) {

        EntityTag etag = generateETag();
        Response.ResponseBuilder responseBuilder = request.evaluatePreconditions(etag);

        if (responseBuilder != null) {
            // Etag match
            return responseBuilder;
        }

        return Response.ok(this).tag(etag);

    }

    /**
     * Adds key value pairs to the model.
     * 
     * @param args
     *            sequence of 'String, Object' pairs
     * @return this view
     */
    public View with(Object... args) {

        checkArgument(args.length > 0 && args.length % 2 == 0, "Argument must be a sequence of 'String, Object' pairs");

        for (int i = 0; i < args.length; i++) {
            this.map.put((String) args[i], args[++i]);
        }

        return this;

    }

    /**
     * Adds all elements of the given map to the model.
     * 
     * @param map
     *            the map to add
     * @return this view
     */
    public View withAll(Map<? extends String, ?> map) {

        this.map.putAll(map);
        return this;

    }

    /**
     * Adds a ciphered value to the model. This is useful for sensitive data
     * like entity identifiers on update operations.
     * 
     * Example:
     * 
     * <pre>
     * view().withSecure(&quot;uid&quot;, id + &quot;:&quot; + username)[...].build();
     * </pre>
     * 
     * @param key
     *            the name of the model property
     * @param value
     *            the value to be ciphered
     * @return this view
     */
    public View withSecure(String key, Object value) {

        this.map.put(key, Crypter.encrypt(value.toString()));
        return this;

    }

    private EntityTag generateETag() {

        long hash = MurmurHash.hash64(getTemplateName() + (model != null ? model.hashCode() : 0));
        return EntityTag.valueOf(String.format("\"%s\"", hash));

    }

}
