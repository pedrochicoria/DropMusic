package register.model;

import model.UserBean;

import java.rmi.RemoteException;

public class RegisterBean extends UserBean {
    private String username;
    private String password;


    public RegisterBean(){
        super();
    }

    public boolean getAuthentication() {
        int ID;

        boolean result = false;
        try {
            //identifies voter
            result = server.insertUser(this.username,this.password);//authenticastes voter
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

        return result;
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
