package satori.hbs.forms;

import satori.hbs.helpers.CommonOptions;
import satori.utils.Html;

public interface SelectableCall {
    
    void execute(Html html, CommonOptions opts, int iter, String value, Selectable opt);
    
}
