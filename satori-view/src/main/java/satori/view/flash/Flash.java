package satori.view.flash;

import javax.servlet.http.HttpSession;

public class Flash {

    public static final String SUCCESS = "satori.flash.success";
    public static final String ERROR = "satori.flash.error";
    public static final String INFO = "satori.flash.info";
    public static final String WARNING = "satori.flash.warning";

    private final HttpSession session;

    public Flash(HttpSession session) {
        this.session = session;
    }

    public String getSuccessMessage() {
        return getMessage(SUCCESS);
    }

    public boolean isSuccess() {
        return isMessage(SUCCESS);
    }

    public void success(String msg) {
        message(SUCCESS, msg);
    }

    public String getErrorMessage() {
        return getMessage(ERROR);
    }

    public boolean isError() {
        return isMessage(ERROR);
    }

    public void error(String msg) {
        message(ERROR, msg);
    }

    public String getInfoMessage() {
        return getMessage(INFO);
    }

    public boolean isInfo() {
        return isMessage(INFO);
    }

    public void info(String msg) {
        message(INFO, msg);
    }

    public String getWarningMessage() {
        return getMessage(WARNING);
    }

    public boolean isWarning() {
        return isMessage(WARNING);
    }

    public void warning(String msg) {
        message(WARNING, msg);
    }

    private String getMessage(String key) {
        if (session == null) {
            return null;
        }
        String msg = (String) session.getAttribute(key);
        session.removeAttribute(key);
        return msg;
    }

    private boolean isMessage(String key) {
        return session != null && session.getAttribute(key) != null;
    }

    private void message(String key, String msg) {
        if (session != null) {
            session.setAttribute(key, msg);
        }
    }

}
