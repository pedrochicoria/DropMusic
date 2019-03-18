package user.model;

import java.rmi.RemoteException;

public class UserBean extends model.UserBean {

    private String username;

    public UserBean() {
        super();
    }

    public boolean getGrantPrivileges(){
        try{
            return server.insertUserPrivilege(username);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
}
