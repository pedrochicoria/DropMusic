package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
     /**
      * metodo remoto para ir buscar o username do cliente
      * @return
      * @throws RemoteException
      */
     String getMy_username() throws RemoteException;

     /**
      * metodo remote para alterar o username do cliente
      * @param my_username
      * @throws RemoteException
      */
     void setMy_username(String my_username) throws RemoteException;

     /**
      * metodo remoto para notificar o cliente de que os seus privilegios foram alterados
      * @throws RemoteException
      */
     void notifyPrivileges() throws RemoteException;

     /**
      * metodo remoto para notificar os entigos editores que o album foi alterado
      * @throws RemoteException
      */
     void notifyTextualDescription() throws RemoteException;
}