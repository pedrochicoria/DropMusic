package user.model;

import model.UserBean;

import java.rmi.RemoteException;


public class DropBoxBean extends UserBean {


    private static final long serialVersionUID = 4L;

    private String username;
    private String id;



    public DropBoxBean() {
        super();

    }


    public String getUrlDropbox(){
        try{
            return server.createservice();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }

    }

    public boolean dropboxAssociation(String username,String code){
        try{
            return server.dropboxAssociation(username, code);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }

    }









    public String getUsername() {
        return username;
    }





    public void setUsername(String username) {
        this.username = username;
    }





    public String getId() {
        return id;
    }





    public void setId(String id) {
        this.id = id;
    }





}
