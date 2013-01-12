package satori.jersey.params;

import java.util.Date;

import satori.i18n.Translation;

public class DateTimeParam extends DateParam {

    public DateTimeParam() {
        super(new Date());
    }

    public DateTimeParam(Date date) {
        super(date);
    }

    public DateTimeParam(String input) {
        super(input);
    }

    public String getDateString() {
        return Translation.getFormat("date").format(get());
    }

    public String getTimeString() {
        return Translation.getFormat("time").format(get());
    }

    public String toString() {
        return Translation.getFormat("date.time").format(get());
    }

    @Override
    protected Date parse(String input) throws Exception {
        return (Date) Translation.getFormat("date.time").parseObject(input);
    }

}
