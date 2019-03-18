package register.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import register.model.RegisterBean;

import java.util.Map;

public class RegisterAction extends ActionSupport implements SessionAware {
    private String username;
    private String password;
    private Map<String, Object> session;

    public String execute(){

        // any username is accepted without confirmation (should check using RMI)

        if(this.username != null && !username.equals("")) {
            this.getRegisterBean().setUsername(this.username);
            this.getRegisterBean().setPassword(this.password);
            if(this.getRegisterBean().getAuthentication()){
                session.put("username", username);
                session.put("loggedin", true); // this marks the user as logged in
                return SUCCESS;
            }
            return ERROR;
        }
        else
            return LOGIN;
    }

    public RegisterBean getRegisterBean() {
        if(!session.containsKey("registerBean"))
            this.setRegisterBean(new RegisterBean());
        return (RegisterBean) session.get("registerBean");
    }

    public void setRegisterBean(RegisterBean registerBean) {
        this.session.put("registerBean", registerBean);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
