package models;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

import org.apache.bval.constraints.NotEmpty;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;

public class UserForm {

    @NotNull @NotEmpty @FormParam("username") private String username;
    @NotNull @NotEmpty @FormParam("password") private String password;
    @FormParam("rememberMe") private boolean rememberMe;

    public void authenticate(HttpServletRequest request) {
        AuthenticationToken token = new UsernamePasswordToken(username, password, rememberMe, request.getRemoteHost());
        SecurityUtils.getSubject().login(token);
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
