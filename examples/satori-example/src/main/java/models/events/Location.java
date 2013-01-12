package models.events;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Location {
    private final static Pattern regexp = Pattern.compile("\\(?(\\-?\\d+\\.?\\d*)[,?\\s*]+(\\-?\\d+\\.?\\d*)\\)?");

    private double lat;
    private double lng;

    public Location() {

    }

    public Location(long lat, long lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Location(String input) {
        Matcher matcher = regexp.matcher(input);
        if (matcher.matches()) {
            this.lat = Double.parseDouble(matcher.group(1));
            this.lng = Double.parseDouble(matcher.group(2));
        }
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return lat + "," + lng;
    }

}
