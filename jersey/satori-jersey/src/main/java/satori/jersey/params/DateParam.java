package satori.jersey.params;

import java.text.DateFormat;
import java.util.Date;

import satori.i18n.Translation;

public class DateParam extends AbstractParam<Date> {

    public DateParam(String input) {
        super(input);
    }

    public DateParam() {
        super(new Date());
    }

    public DateParam(Date date) {
        super(date);
    }

    public String toString() {
        return Translation.getFormat("date").format(get());
    }

    @Override
    protected Date parse(String input) throws Exception {
        return ((DateFormat) Translation.getFormat("date")).parse(input);
    }

}
