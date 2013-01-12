package helpers;

import java.nio.charset.Charset;

public final class Gravatar {
    public enum DefaultImage {

        GRAVATAR_ICON(""),

        IDENTICON("identicon"),

        MONSTERID("monsterid"),

        WAVATAR("wavatar"),

        HTTP_404("404");

        private String code;

        private DefaultImage(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

    }

    public enum Rating {

        GENERAL_AUDIENCES("g"),

        PARENTAL_GUIDANCE_SUGGESTED("pg"),

        RESTRICTED("r"),

        XPLICIT("x");

        private String code;

        private Rating(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

    }

    private final static String GRAVATAR_URL = "http://www.gravatar.com/avatar/";

    /**
     * @return the Gravatar URL for the given email address.
     */
    public static String getUrl(String email, int size, Rating rating, DefaultImage defaultImg) {

        // hexadecimal MD5 hash of the requested user's lowercased email address
        // with all whitespace trimmed
        String emailHash = MD5(email.toLowerCase().trim());
        return String.format("%s?s=%s&r=%s&d=%s", GRAVATAR_URL + emailHash + ".jpg", size, rating.code, defaultImg.code);
    }

    public static String getUrl(String email, int size) {

        return getUrl(email, size, Rating.GENERAL_AUDIENCES, DefaultImage.MONSTERID);
    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes(Charset.forName("UTF8")));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
