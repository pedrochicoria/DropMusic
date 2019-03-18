package rmiserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
     /**
      * metodo remoto para ir buscar o username do cliente
      * @return
      * @throws RemoteException
      */
     String getMy_username() throws RemoteException;
}