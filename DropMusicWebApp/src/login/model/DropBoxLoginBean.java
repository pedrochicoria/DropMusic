package login.model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import model.UserBean;
import rmiserver.ServerInterface;

public class DropBoxLoginBean extends UserBean {





	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;
	private int num;
	private String resp;
	private String username;




	public DropBoxLoginBean() {
		super();

	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getResp() {
		return resp;
	}
	public void setResp(String resp) {
		this.resp = resp;
	}


	public String getUrlDropbox(){
		try{
			return server.loginService();
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}

	}

	public String dropboxLogin(String code){
		try{
			return server.loginDropbox(code);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}


	}








	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}
