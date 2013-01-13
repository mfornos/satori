package satori.security;

import java.security.SecureRandom;

public class CsrfHelper {

    public static final String CSRF_TOKEN_KEY = "satori.csrf.token";

    public static final String CSRF_TOKEN_PARAM = "csrfToken";

    private static final int TOKEN_LENGTH = 32;

    private static final CsrfHelper instance = new CsrfHelper();

    public static String newCsrfToken() {

        return instance.newToken();

    }

    private final SecureRandom prng;

    //
    // private Cookie getCsrfCookie(HttpServletRequest request) {
    //
    // if (request.getCookies() != null) {
    // for (Cookie cookie : request.getCookies()) {
    // if (cookie.getName().equals(CSRF_TOKEN_KEY)) {
    // return cookie;
    // }
    // }
    // }
    //
    // return new Cookie(CSRF_TOKEN_KEY, "");
    //
    // }
    //
    // private String prepare(HttpServletRequest request, HttpServletResponse
    // response) {
    //
    // String token =
    //
    // Cookie csrfCookie = getCsrfCookie(request);
    // csrfCookie.setValue(token);
    //
    // response.addCookie(csrfCookie);
    //
    // return token;
    //
    // }

    private CsrfHelper() {

        this.prng = new SecureRandom();

    }

    private String newToken() {
        
        return RandomGenerator.generateRandomId(prng, TOKEN_LENGTH);
        
    }

}
