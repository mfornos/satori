package satori.hbs.helpers;

import static satori.i18n.Translation.translate;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;

import satori.hbs.forms.Selectable;
import satori.hbs.forms.SelectableCall;
import satori.hbs.forms.SelectableUtils;
import satori.i18n.Translation;
import satori.utils.Html;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public class FormHelpers {

    private static abstract class BaseInput implements Helper<Object> {

        @Override
        public CharSequence apply(Object ctx, Options options) throws IOException {
            CommonOptions opts = CommonOptions.instance(options);

            Html html = render(opts).end();

            helpIfNeeded(html, opts.help);

            return new Handlebars.SafeString(html.emit());
        }

        abstract protected Html render(CommonOptions opts);

    }

    private static class Input extends BaseInput {

        protected String coerce(CommonOptions opts) {
            return opts.stringValue();
        }

        @Override
        protected Html render(CommonOptions opts) {
            String placeholder = opts.get("placeholder");
            String type = opts.get("type", "text");

            Html html = Html.builder();
            html.begin("input").attrs("type", type, "name", opts.name, "id", opts.id, "class", opts.css);

            if (opts.hasValue()) {
                html.attr("value", coerce(opts));
            }

            if (placeholder != null) {
                html.attr("placeholder", translate(placeholder));
            }

            return Util.addInformalOptions(html, opts.options);
        }

    }

    // TODO extraer en varios ficheros
    public static void register(final Handlebars handlebars, ServletContext context) {

        handlebars.registerHelper("form", form(context.getContextPath()));
        handlebars.registerHelper("input", new Input());
        handlebars.registerHelper("textarea", textArea());
        handlebars.registerHelper("checkbox", checkbox());
        handlebars.registerHelper("dateField", dateField());
        // TODO actions div
        // TODO test file*
        handlebars.registerHelper("fieldset", fieldSet());
        handlebars.registerHelper("group", controlGroup());
        // TODO allow select multiple
        handlebars.registerHelper("select", select());
        handlebars.registerHelper("radioGroup", radioGroup());

        // TODO Extended: well resolved palette with filter and order...

    }

    private static Helper<Object> checkbox() {

        return new BaseInput() {

            @Override
            protected Html render(CommonOptions opts) {
                String label = opts.get("label");
                Object checked = opts.get("checked");
                String value = opts.hasValue() ? opts.stringValue() : "true";

                Html html = Html.builder();

                if (label != null) {
                    html.begin("label").attrs("for", opts.id, "class", "checkbox");
                }

                html.begin("input").attrs("type", "checkbox", "name", opts.name, "value", value, "id", opts.id,
                        "class", opts.css);

                if (checked != null) {
                    String tmp = ConvertUtils.convert(checked);
                    if (StringUtils.isNotBlank(tmp) && !tmp.equals("false")) {
                        html.attr("checked", "checked");
                    }
                }

                Util.addInformalOptions(html, opts.options);

                if (label != null) {
                    html.end();
                    html.text(Translation.translate(label));
                    html.end();
                }

                return html;
            }
        };

    }

    private static Helper<Object> controlGroup() {

        return new Helper<Object>() {

            @Override
            public CharSequence apply(Object ctx, Options options) throws IOException {
                String css = options.hash("class");
                css = (css == null) ? "control-group" : "control-group " + css;

                if (options.hash("error") != null) {
                    css += " error";
                }

                String label = options.hash("label");
                String id = options.hash("id");
                if (id == null)
                    id = label;

                Html html = Html.builder();
                html.begin("div").attr("class", css);

                if (label != null) {
                    html.begin("label").attrs("for", id, "class", "control-label");
                    html.text(translate(label));
                    html.end();
                }

                html.begin("div").attr("class", "controls");

                html.write(options.fn());

                html.endAll();

                return new Handlebars.SafeString(html.emit());

            }

        };
    }

    private static Helper<Object> dateField() {

        return new Input() {
            @Override
            protected String coerce(CommonOptions opts) {
                String pattern = opts.get("pattern");
                Format f = (pattern == null) ? Translation.getFormat((String) opts.get("format", "date"))
                        : new SimpleDateFormat(pattern);
                return f.format(opts.val);
            }
        };

    }

    private static Helper<Object> fieldSet() {

        return new Helper<Object>() {

            @Override
            public CharSequence apply(Object ctx, Options options) throws IOException {
                String legend = options.hash("legend");
                String css = options.hash("class");

                Html html = Html.builder().begin("fieldset").attr("class", css);
                if (legend != null) {
                    html.begin("legend");
                    html.text(legend);
                    html.end();
                }
                html.write(options.fn());
                html.end();

                return new Handlebars.SafeString(html.emit());
            }
        };
    }

    private static Helper<String> form(final String contextPath) {

        return new Helper<String>() {

            @Override
            public CharSequence apply(String action, Options options) throws IOException {
                String method = options.hash("method", "POST");

                Html html = Html.builder().begin("form");
                html.attrs("action", Util.withContextPath(contextPath, action, false).toString(), "method", method);

                Util.addInformalOptions(html, options);

                html.write(options.fn());
                html.end();

                return new Handlebars.SafeString(html.emit());

            }

        };
    }

    private static void helpIfNeeded(Html html, String help) {

        if (help != null) {
            html.begin("span").attr("class", "help-inline");
            html.text(translate(help));
            html.end();
        }

    }

    @SuppressWarnings("unchecked")
    private static void iterateSelectables(Html html, CommonOptions opts, SelectableCall command) {
        Object selectOpts = resolveSelectables(opts);

        if (selectOpts == null) {
            return;
        }

        String value = opts.stringValue();
        int i = 0;
        Iterator<Selectable> iterator;
        try {
            iterator = ((Iterable<Selectable>) selectOpts).iterator();
            command.execute(html, opts, i++, value, iterator.next());
        } catch (Exception e) {
            i = 0;
            iterator = SelectableUtils.fromCollection((Collection<Object>) selectOpts).iterator();
            command.execute(html, opts, i++, value, iterator.next());
        }

        while (iterator.hasNext()) {
            command.execute(html, opts, i++, value, iterator.next());
        }
    }

    private static Helper<Object> radioGroup() {

        return new Helper<Object>() {

            @Override
            public CharSequence apply(Object ctx, Options options) throws IOException {
                CommonOptions opts = CommonOptions.instance(options);

                Html html = Html.builder();

                iterateSelectables(html, opts, new SelectableCall() {
                    public void execute(Html html, CommonOptions opts, int iter, String value, Selectable opt) {
                        String sLabel = opt.selectLabel();
                        String sValue = ConvertUtils.convert(opt.selectValue());

                        html.begin("label").attr("class", "radio");
                        html.begin("input").attrs("type", "radio", "value", sValue, "name", opts.name, "id",
                                opts.id + iter);
                        if (sValue.equals(value)) {
                            html.attr("checked", "checked");
                        }
                        Util.addInformalOptions(html, opts.options);

                        html.text(translate(sLabel));
                        html.endAll();
                    }
                });

                html.endAll();

                helpIfNeeded(html, opts.help);

                return new Handlebars.SafeString(html.emit());
            }

        };
    }

    private static Object resolveSelectables(CommonOptions opts) {
        Object selectOpts = opts.get("options");

        if (selectOpts == null && opts.hasValue() && opts.val.getClass().isEnum()) {
            selectOpts = SelectableUtils.fromEnum(opts.val.getClass());
        } else if (selectOpts != null && String.class.isAssignableFrom(selectOpts.getClass())) {
            selectOpts = SelectableUtils.fromString(selectOpts.toString());
        }

        return selectOpts;
    }

    private static Helper<Object> select() {

        return new Helper<Object>() {

            @Override
            public CharSequence apply(Object ctx, Options options) throws IOException {
                CommonOptions opts = CommonOptions.instance(options);

                Html html = Html.builder();
                html.begin("select").attrs("name", opts.name, "id", opts.id, "class", opts.css);

                iterateSelectables(html, opts, new SelectableCall() {
                    public void execute(Html html, CommonOptions opts, int iter, String value, Selectable opt) {
                        String sLabel = opt.selectLabel();
                        String sValue = ConvertUtils.convert(opt.selectValue());

                        html.begin("option").attr("value", sValue);
                        if (sValue.equals(value)) {
                            html.attr("selected", "selected");
                        }

                        html.text(translate(sLabel));
                        html.end();
                    }
                });

                Util.addInformalOptions(html, options);

                html.end();

                helpIfNeeded(html, opts.help);

                return new Handlebars.SafeString(html.emit());
            }

        };
    }

    private static Helper<Object> textArea() {

        return new BaseInput() {

            @Override
            protected Html render(CommonOptions opts) {
                Html html = Html.builder();
                html.begin("textarea").attrs("name", opts.name, "id", opts.id, "class", opts.css);
                Util.addInformalOptions(html, opts.options);

                if (opts.hasValue()) {
                    html.text(opts.stringValue());
                }

                return html.end();
            }

        };
    }

}
