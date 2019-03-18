package ws;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WebSocketInterface extends Remote{
    /**
     * lists all active users
     * @throws RemoteException
     */
    public void userToNotify() throws RemoteException;
    public void textToNotify() throws RemoteException;
    public void reloadToNotify() throws RemoteException;
}
