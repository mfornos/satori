package satori.security;

import javax.ws.rs.core.NewCookie;

public class SecureCookie extends NewCookie {

    private final boolean httpOnly;

    public SecureCookie(String name, String value, String path, String domain, int version, String comment, int maxAge,
            boolean secure, boolean httpOnly) {
        super(name, value, path, domain, version, comment, maxAge, secure);
        this.httpOnly = httpOnly;
    }

    public SecureCookie(String name, String value, boolean httpOnly) {
        this(name, value, "/", null, 1, null, -1, false, httpOnly);
    }

    public String toString() {
        return httpOnly ? new StringBuilder(super.toString()).append(";HTTPOnly").toString() : super.toString();
    }

}
