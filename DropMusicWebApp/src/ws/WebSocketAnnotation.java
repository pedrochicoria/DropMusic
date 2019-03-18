package ws;

import rmiserver.ServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;

@ServerEndpoint(value = "/ws")
public class WebSocketAnnotation extends UnicastRemoteObject implements WebSocketInterface{
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private static ServerInterface server;
    private final String username;
    private Session session;
    private Map<String, Object> sessionMap;

    public WebSocketAnnotation() throws RemoteException {
        super();
        username = "teste";
    }

    @OnOpen
    public void start(Session session) throws RemoteException{
        this.session = session;

        if(server == null) {
            connectRMI();
        }

        try {
            server = (ServerInterface) Naming.lookup("rmi://localhost:7000/DropMusic");
            server.webRegisterForCallback((WebSocketInterface)this);
        }
        catch(NotBoundException | RemoteException | MalformedURLException e) {
            System.out.println(e.getMessage());
        }
    }

    @OnClose
    public void end() throws RemoteException {
        try {
            server.webUnregisterForCallback((WebSocketInterface) this);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
        try {
            this.session.close();
        } catch (IOException e) {

        }
    }
    @OnMessage
    public void receiveMessage(String message) throws RemoteException {
            sendMessage(message);
    }

    @OnError public void handleError(Throwable t) throws RemoteException{
        t.printStackTrace();
    }

    private void sendMessage(String text) {
    	// uses *this* object's session to call sendText()
    	try {
			this.session.getBasicRemote().sendText(text);
		} catch (IOException e) {
			// clean up once the WebSocket connection is closed
			try {
				this.session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
    }

    @Override
    public void userToNotify() throws RemoteException {
        sendMessage("your privilege has been changed to editor");
    }

    @Override
    public void textToNotify() throws RemoteException {
        sendMessage(server.teste());
    }

    @Override
    public void reloadToNotify() throws RemoteException {
        sendMessage("reload");
    }
    private void connectRMI() throws RemoteException{
        try {
            server = (ServerInterface) Naming.lookup("rmi://localhost:7000/DropMusic");
        }
        catch(NotBoundException | RemoteException | MalformedURLException e) {
            System.out.println(e.getMessage());
        }
    }
}
