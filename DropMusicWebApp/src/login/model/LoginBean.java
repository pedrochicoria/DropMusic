package login.model;

import model.UserBean;
import java.rmi.RemoteException;

public class LoginBean extends UserBean {
    private String username;
    private String password;

    public LoginBean(){
        super();
    }

    public boolean getAuthentication() {
        try {
            return server.checkLogin(username,password);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean getPrivilege(){
        try {
            return server.checkUserPrivilege(username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
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
}
