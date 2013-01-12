package models;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

import org.apache.bval.constraints.NotEmpty;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;

public class UserForm {

    @NotNull @NotEmpty @FormParam("username") private String username;
    @NotNull @NotEmpty @FormParam("password") private String password;
    @FormParam("rememberMe") private boolean rememberMe;
    private boolean loginAccepted;

    public void authenticate(HttpServletRequest request) {
        AuthenticationToken token = new UsernamePasswordToken(username, password, rememberMe, request.getRemoteHost());
        SecurityUtils.getSubject().login(token);
        setLoginAccepted(true);
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isLoginAccepted() {
        return loginAccepted;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setLoginAccepted(boolean loginAccepted) {
        this.loginAccepted = loginAccepted;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserForm uncatchAuthenticate(HttpServletRequest request) {
        try {
            authenticate(request);
        } catch (AuthenticationException ex) {
            setLoginAccepted(false);
        }
        return this;
    }

}
