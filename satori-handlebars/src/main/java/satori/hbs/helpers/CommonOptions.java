package satori.hbs.helpers;

import org.apache.commons.beanutils.ConvertUtils;

import com.github.jknack.handlebars.Options;

public class CommonOptions {
    public static CommonOptions instance(Options options) {
        return new CommonOptions(options);
    }

    String name;
    String id;
    Object appendId;
    String help;
    String css;
    Object val;
    Options options;

    private CommonOptions(Options options) {
        name = Util.rem(options, "name", "");
        appendId = Util.rem(options, "appendId");
        id = ConvertUtils.convert(Util.rem(options, "id", name));
        help = Util.rem(options, "help");
        css = Util.rem(options, "class");
        val = Util.rem(options, "value");
        this.options = options;

        if (appendId != null) {
            id += ConvertUtils.convert(appendId);
        }
    }

    public <T> T get(String name) {
        return Util.rem(options, name);
    }

    public <T> T get(String name, T dval) {
        return Util.rem(options, name, dval);
    }

    public boolean hasValue() {
        return val != null;
    }

    public String stringValue() {
        return (val == null) ? null : ConvertUtils.convert(val);
    }
}
