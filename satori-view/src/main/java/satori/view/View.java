package satori.view;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import satori.security.CsrfHelper;
import satori.utils.MurmurHash;

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

    public View add(Object... args) {
        if (args.length == 0 || args.length % 2 != 0) {
            throw new IllegalArgumentException("Argument must be a serie of 'String, Object' pairs");
        }

        for (int i = 0; i < args.length; i++) {
            this.map.put((String) args[i], args[++i]);
        }
        return this;
    }

    public View addAll(Map<? extends String, ?> map) {
        this.map.putAll(map);
        return this;
    }

    public ResponseBuilder builder() {
        return Response.ok(this);
    }

    public ResponseBuilder csrfToken() {

        this.csrfToken = CsrfHelper.newCsrfToken();
        return Response.ok(this)
                       .cookie(new NewCookie(CsrfHelper.CSRF_TOKEN_KEY, csrfToken, "/", null, 1, null, -1, false));

    }

    @Override
    public String getCsrfToken() {

        return csrfToken;

    }

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

    public boolean hasMap() {
        return !(map == null || map.isEmpty());
    }

    public Response ok() {
        return builder().build();
    }

    public ResponseBuilder tag(Request request) {

        EntityTag etag = generateETag();
        Response.ResponseBuilder responseBuilder = request.evaluatePreconditions(etag);

        if (responseBuilder != null) {
            // Etag match
            return responseBuilder;
        }

        return Response.ok(this).tag(etag);

    }

    private EntityTag generateETag() {

        long hash = MurmurHash.hash64(getTemplateName() + (model != null ? model.hashCode() : 0));
        return EntityTag.valueOf(String.format("\"%s\"", hash));

    }

}
