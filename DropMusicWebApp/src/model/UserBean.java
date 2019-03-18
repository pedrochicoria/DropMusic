package model;

import rmiserver.ServerInterface;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class UserBean implements Serializable {
    protected ServerInterface server;

    public UserBean() {

        try {
            server = (ServerInterface) Naming.lookup("rmi://localhost:7000/DropMusic");
        }
        catch(NotBoundException | RemoteException | MalformedURLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void lookupRMI() throws RemoteException, NotBoundException, MalformedURLException {
        server = (ServerInterface) Naming.lookup("rmi://localhost:7000/DropMusic");
    }
}
