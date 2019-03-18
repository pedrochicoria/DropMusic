package login.action;

import com.opensymphony.xwork2.ActionSupport;
import login.model.DropBoxLoginBean;
import login.model.LoginBean;
import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;


public class LoginAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private String username = null;
    private String password = null;
    private Boolean privilege = null;
    private String DropboxUrl;
    private String code;

    public String login(){
        if(this.username != null && !username.equals("")) {
            this.getLoginBean().setUsername(this.username);
            this.getLoginBean().setPassword(this.password);
            getPrivilege();

            if(this.getLoginBean().getAuthentication()){
                session.put("USERNAME", username);
                session.put("PRIVILEGE", privilege);
                session.put("LOGGED_IN", true);
                return SUCCESS;
            }
        }
        return ERROR;
    }

    public String dropBroxLogin() {
        this.DropboxUrl=getDropboxLoginBean().getUrlDropbox();
        if(this.DropboxUrl!=null){

            return SUCCESS;
        }else{
            return LOGIN;
        }




    }


    public String dropboxLogin2() {
        System.out.println(getCode());
        if(getCode()!=null){
            String username = getDropboxLoginBean().dropboxLogin(getCode());

            if(username!=null){
                session.put("username",username);
                session.put("LOGGED_IN",true);
                getPrivilege();
                session.put("PRIVILEGE", privilege);
                return SUCCESS;
            }else{
                return LOGIN;
            }
        }
        return ERROR;

    }

    public String logout() {
        if (session.containsKey("USERNAME") && session.containsKey("LOGGED_IN")) {
            session.remove("USERNAME");
            session.remove("LOGGED_IN");
        }
        return SUCCESS;
    }

    public String checkLogin(){
        if(session.containsKey("LOGGED_IN")){
            return SUCCESS;
        }
        return ERROR;
    }

    public void getPrivilege(){
        this.privilege = this.getLoginBean().getPrivilege();
    }

    public LoginBean getLoginBean() {
        if(!session.containsKey("loginBean"))
            this.setLoginBean(new LoginBean());
        return (LoginBean) session.get("loginBean");
    }

    public void setLoginBean(LoginBean loginBean) {
        this.session.put("loginBean", loginBean);
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

    public DropBoxLoginBean getDropboxLoginBean() {
        if(!session.containsKey("loginBean"))
            this.setDropBoxLoginBean(new DropBoxLoginBean());
        return (DropBoxLoginBean) session.get("dropboxLoginBean");
    }

    public void setDropBoxLoginBean(DropBoxLoginBean dropBoxLoginBean) {
        this.session.put("dropboxLoginBean", dropBoxLoginBean);
    }

    public String getDropboxUrl() {
        return DropboxUrl;
    }

    public void setDropboxUrl(String dropboxUrl) {
        DropboxUrl = dropboxUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}