package satori.i18n.standard;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ISO8601Format extends SimpleDateFormat {

    private static final long serialVersionUID = 3830360318537405521L;

    public ISO8601Format() {
        super("yyyy-MM-dd'T'HH:mm:ssZ");
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
        String tmp = super.format(date, toAppendTo, pos).toString();
        StringBuffer sb = new StringBuffer(tmp.substring(0, 22));
        sb.append(':');
        sb.append(tmp.substring(22));
        return sb;
    }

    @Override
    public Date parse(String text, ParsePosition pos) {
        // UTC time zone
        String s = text.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException(String.format("Invalid length for input '%s'", s));
        }
        return super.parse(s, pos);
    }

}
