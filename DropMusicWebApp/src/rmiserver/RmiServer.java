package rmiserver;


import java.io.IOException;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import uc.sd.apis.DropBoxApi2;
import ws.WebSocketInterface;

import static com.github.scribejava.core.model.OAuthConstants.EMPTY_TOKEN;

public class RmiServer extends UnicastRemoteObject implements ServerInterface {

    private static final long serialVersionUID = 1L;
    private static String MULTICAST_ADDRESS = "224.2.224.2";
    private static int PORT = 4501;
    private static int MULTICAST_PORT = 4500;
    private static final Set<WebSocketInterface> webSockets = new CopyOnWriteArraySet<>();
    private static ArrayList<String> usersList;
    private String album_aux;
    private static OAuthService service;
    private String DropboxUrl;
    private static final String API_APP_KEY = "698fv1gb3xza4f6";
    private static final String API_APP_SECRET = "oy0a9a9nuqiw7vx";
    private String code;
    final String NETWORK_NAME = "DropBox";
    private String authorizationUrl=null;

    //Link functions
    public RmiServer() throws RemoteException {
        super();
    }


    public String createservice(){

        service = new ServiceBuilder()
                .provider(DropBoxApi2.class)
                .apiKey(API_APP_KEY)
                .apiSecret(API_APP_SECRET)
                .callback("http://localhost:8080/dropboxAssociantion2") // Do not change this.
                .build();

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        this.authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize Scribe here:");

        this.DropboxUrl=authorizationUrl;
        System.out.println(this.DropboxUrl);
        return service.getAuthorizationUrl(EMPTY_TOKEN);
    }


    public boolean dropboxAssociation(String username, String code){
        boolean check = false;

        Verifier verifier = new Verifier(code);
        System.out.println(verifier.getValue());



        OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.dropboxapi.com/oauth2/token", service);
        request.addParameter("code",code);
        request.addParameter("grant_type","authorization_code");
        request.addParameter("redirect_uri","http://localhost:8080/dropboxAssociantion2");
        request.addParameter("client_id",API_APP_KEY);
        request.addParameter("client_secret",API_APP_SECRET);


        Response response =request.send();
        System.out.println(response.getBody());

        JSONObject rj;

        rj = (JSONObject) JSONValue.parse(response.getBody());
        String token = rj.get("access_token").toString();
        String account_id = rj.get("account_id").toString();
        System.out.println(token);
        System.out.println(account_id);




        String message_to_multicast = "type|dropbox_association;username|" + username + ";id|" + account_id+";token|"+token;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|dropbox_association;status|on") || message_from_multicast.equals("type|dropbox_association;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                check = true;
            }
        }

        return check;
    }


    public String loginService(){
        service = new ServiceBuilder()
                .provider(DropBoxApi2.class)
                .apiKey(API_APP_KEY)
                .apiSecret(API_APP_SECRET)
                .callback("http://localhost:8080/dropboxlogin2") // Do not change this.
                .build();

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        this.authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize Scribe here:");

        this.DropboxUrl=authorizationUrl;
        System.out.println(this.DropboxUrl);
        return service.getAuthorizationUrl(EMPTY_TOKEN);
    }

    public String loginDropbox(String code){
        Verifier verifier = new Verifier(code);
        System.out.println(verifier.getValue());



        OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.dropboxapi.com/oauth2/token", service);
        request.addParameter("code",code);
        request.addParameter("grant_type","authorization_code");
        request.addParameter("redirect_uri","http://localhost:8080/dropboxlogin2");
        request.addParameter("client_id",API_APP_KEY);
        request.addParameter("client_secret",API_APP_SECRET);

        Response response =request.send();
        System.out.println(response.getBody());

        JSONObject rj;

        rj = (JSONObject) JSONValue.parse(response.getBody());
        String token = rj.get("access_token").toString();
        String account_id = rj.get("account_id").toString();
        System.out.println(token);
        System.out.println(account_id);

        String message_to_multicast = "type|dropbox_login" + ";id|" + account_id;
        String message_from_multicast = sendPacket(message_to_multicast);

        if(message_from_multicast!=null){
            String [] data = message_from_multicast.split(";");

            String [] aux_username = data[1].split("\\|");
            String id = aux_username[1];

            return id;

        }

        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    private void sendToWebsocket() throws RemoteException{
        webSockets.forEach((s) -> {
            try {
                s.userToNotify();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendToWebsockeText() throws RemoteException{
        webSockets.forEach((s) -> {
            try {
                s.textToNotify();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendToWebsockeReload() throws RemoteException{
        webSockets.forEach((s) -> {
            try {
                s.reloadToNotify();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public synchronized void webRegisterForCallback(WebSocketInterface websocket) throws RemoteException {
        System.out.println("websocket registou");
        webSockets.add(websocket);
    }

    @Override
    public synchronized void  webUnregisterForCallback(WebSocketInterface websocket) throws RemoteException{
        if(webSockets.remove(websocket)) {
            System.out.println("websocket deresgistou");
        }
        else {
            System.out.println("Web Socket that was not registered tried to unregister");
        }
    }

    public String teste() throws RemoteException{
        return "the album" + album_aux + " has been edited";
    }




    /**
     *Função testConnectionRMI serve para trocar mensagens entre o Backup RMI  e o Primary RMI  para ver se o Primary RMI está conectado
     */
    public synchronized void testConnectionRMI() {
        System.out.println("Received call from RMI Backup");
    }

    /**
     *função para comunicar com o Servidor Multicast, enviando os pedidos e recebendo as respostas do mesmo, segundo o protocolo estabelecido
     * @param message
     * @return
     */
    private static String sendPacket(String message) {
        String receive_message = null;
        MulticastSocket send_socket = null;
        MulticastSocket receive_socket = null;
        try {
            Thread.sleep(500);
            send_socket = new MulticastSocket();
            receive_socket = new MulticastSocket(PORT);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            DatagramPacket send_packet = new DatagramPacket(message.getBytes(), message.getBytes().length, group, MULTICAST_PORT);
            send_socket.send(send_packet);

            receive_socket.setSoTimeout(1000);
            byte[] buffer = new byte[1000];
            receive_socket.joinGroup(group);

            DatagramPacket receive_packet = new DatagramPacket(buffer, buffer.length);
            receive_socket.receive(receive_packet);
            receive_message = new String(receive_packet.getData(), 0, receive_packet.getLength());
            System.out.println("Recebi do Multicast: " + receive_message);
            receive_socket.leaveGroup(group);

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            receive_socket.close();
            send_socket.close();
        }
        return receive_message;
    }

    /**
     * função que envia para o multicast os dados de login, recebendo uma resposta que dita se o utilizador pode autenticar
     * @param username
     * @param password
     * @return
     * @throws RemoteException
     */
    //Check
    public synchronized boolean checkLogin(String username, String password) throws RemoteException {
        boolean flag_check_login = false;

        String message_to_multicast = "type|check_login;username|" + username + ";password|" + password;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|check_login;status|on") || message_from_multicast.equals("type|check_login;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_check_login = true;
            }
        }

        return flag_check_login;
    }

    /**
     * função que envia o pedido para o multicast  para verificar se o user tem privilégio de editor ou não, recebendo uma resposta de volta.
     * @param username
     * @return
     */
    public synchronized boolean checkUserPrivilege(String username) {
        boolean flag_check_user_privilege = false;

        String message_to_multicast = "type|check_user_privilege;username|" + username;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|check_user_privilege;status|on") || message_from_multicast.equals("type|check_user_privilege;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_check_user_privilege = true;
            }
        }

        return flag_check_user_privilege;

    }

    /**
     * função que envia o pedido de verificação de username, recebendo uma resposta, que dita se o username já existe.
     * @param username
     * @return
     */
    public synchronized boolean checkUser(String username) {
        boolean flag_check_user = false;

        String message_to_multicast = "type|check_user;username|" + username;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|check_user;status|on") || message_from_multicast.equals("type|check_user;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_check_user = true;
            }
        }

        return flag_check_user;

    }

    /**
     * função que envia o pedido de verificação de artista, ditando se o artista existe
     * @param artist_name
     * @return
     */
    public synchronized boolean checkArtist(String artist_name) {
        boolean flag_check_artist = false;

        String message_to_multicast = "type|check_artist;artist_name|" + artist_name;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|check_artist;status|on") || message_from_multicast.equals("type|check_artist;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_check_artist = true;
            }
        }

        return flag_check_artist;
    }

    /**
     * função que envia o pedido de verificação do nome do album
     * @param album_name
     * @return
     */
    public synchronized boolean checkAlbum(String album_name) {
        boolean flag_check_album = false;

        String message_to_multicast = "type|check_album;album_name|" + album_name;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|check_album;status|on") || message_from_multicast.equals("type|check_album;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_check_album = true;
            }
        }

        return flag_check_album;
    }

    /**
     * função que envia o pedido de verificação da musica
     * @param music_name
     * @return
     */

    public synchronized boolean checkMusic(String music_name) {
        boolean flag_check_music = false;

        String message_to_multicast = "type|check_music;music_name|" + music_name;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|check_music;status|on") || message_from_multicast.equals("type|check_music;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_check_music = true;
            }
        }

        return flag_check_music;
    }

    /**
     * função que envia pedido de verificação de criticas
     * @param album_name
     * @param username
     * @return
     */
    public synchronized boolean checkReview(String album_name, String username) {
        boolean flag_check_review = false;

        String message_to_multicast = "type|check_review;album_name|" + album_name + ";username|" + username;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|check_review;status|on") || message_from_multicast.equals("type|check_review;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_check_review = true;
            }
        }

        return flag_check_review;
    }

    /**
     * função que envia o pedido de verificação do nome da playlist,ditando se ela existe ou nao
     * @param playlist_name
     * @return
     */
    public synchronized boolean checkPlaylist(String playlist_name) {
        boolean flag_check_playlist = false;

        String message_to_multicast = "type|check_playlist;playlist_name|" + playlist_name;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|check_playlist;status|on") || message_from_multicast.equals("type|check_playlist;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_check_playlist = true;
            }
        }

        return flag_check_playlist;
    }

    public synchronized String getAlbumRating(String album) {

        String message_to_multicast = "type|albumRating;album|" + album;
        String message_from_multicast = sendPacket(message_to_multicast);

        return message_from_multicast;
    }

    /**
     * envia o pedido de verificação da playlist do utilzador para ver ela existe
     * @param playlist_name
     * @param username
     * @return
     */
    public synchronized boolean checkUserPlaylist(String playlist_name, String username) {
        boolean flag_check_user_playlist = false;

        String message_to_multicast = "type|check_user_playlist;playlist_name|" + playlist_name + ";username|" + username;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|check_user_playlist;status|on") || message_from_multicast.equals("type|check_user_playlist;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_check_user_playlist = true;
            }
        }

        return flag_check_user_playlist;
    }

    //Insert

    /**
     * função que envia para multicast o o pedido de inserção do utilizador
     * @param username
     * @param password
     * @return
     * @throws RemoteException
     */
    public synchronized boolean insertUser(String username, String password) throws RemoteException {
        boolean flag_insert_user = false;

        String message_to_multicast = "type|insert_user;username|" + username + ";password|" + password;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|insert_user;status|on") || message_from_multicast.equals("type|insert_user;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_insert_user = true;
            }
        }

        return flag_insert_user;
    }

    /**
     * função que envia para o multicast o pedido de alteração de privilégios de um dado utilizador
     * @param username
     * @return
     * @throws RemoteException
     * @throws InterruptedException
     */
    public synchronized boolean insertUserPrivilege(String username) throws RemoteException, InterruptedException {
        boolean flag_insert_user_privilege = false;

        String message_to_multicast = "type|insert_user_privilege;username|" + username;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|insert_user_privilege;status|on") || message_from_multicast.equals("type|insert_user_privilege;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_insert_user_privilege = true;

            }

        }
        sendToWebsocket();
        return flag_insert_user_privilege;
    }


    /**
     * função que envia para multicast o pedido de inserção do artista
     * @param artist_name
     * @param artist_age
     * @param artist_music_style
     * @param artist_biography
     * @return
     */
    public synchronized boolean insertArtist(String artist_name, String artist_age, String artist_music_style, String artist_biography) {
        boolean flag_insert_artist = false;

        String message_to_multicast = "type|insert_artist;artist_name|" + artist_name + ";artist_age|" + artist_age + ";artist_music_style|" + artist_music_style + ";artist_biography|" + artist_biography;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|insert_artist;status|on") || message_from_multicast.equals("type|insert_artist;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_insert_artist = true;
            }
        }

        return flag_insert_artist;
    }

    /**
     * função que envia para multicast o pedido de inserção do album
     * @param album_name
     * @param album_record_label
     * @param artist_name
     * @return
     */
    public synchronized boolean insertAlbum(String album_name, String album_record_label, String artist_name) {
        boolean flag_insert_album = false;

        String message_to_multicast = "type|insert_album;album_name|" + album_name + ";album_record_label|" + album_record_label + ";artist_name|" + artist_name;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|insert_album;status|on") || message_from_multicast.equals("type|insert_album;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_insert_album = true;
            }
        }

        return flag_insert_album;
    }

    /**
     * função que envia para multicast o pedido de inserção do musica
     * @param music_name
     * @param music_gender
     * @param music_lyrics
     * @param music_composer
     * @param music_release_date
     * @param album_name
     * @param artist_name
     * @return
     */
    public synchronized boolean insertMusic(String music_name, String music_gender, String music_lyrics, String music_composer, String music_release_date, String album_name, String artist_name) {
        boolean flag_insert_music = false;

        String message_to_multicast = "type|insert_music;music_name|" + music_name + "; music_gender|" + music_gender + ";music_lyrics|" + music_lyrics + ";music_composer|" + music_composer + ";music_release_date|" + music_release_date + ";album_name|" + album_name + ";artist_name|" + artist_name;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|insert_music;status|on") || message_from_multicast.equals("type|insert_music;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_insert_music = true;
            }
        }

        return flag_insert_music;
    }

    /**
     * função que envia para multicast o pedido de inserção da critica
     * @param username
     * @param description
     * @param rating
     * @param album_name
     * @param artist_name
     * @return
     */
    public synchronized boolean insertReview(String username, String description, String rating, String album_name, String artist_name) {
        boolean flag_insert_review = false;

        String message_to_multicast = "type|insert_review;username|" + username + ";description|" + description + ";rating|" + rating + ";album_name|" + album_name + ";artist_name|" + artist_name;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|insert_review;status|on") || message_from_multicast.equals("type|insert_review;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_insert_review = true;
            }
        }
        album_aux = album_name;
        try {
            sendToWebsockeText();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            sendToWebsockeReload();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return flag_insert_review;
    }

    /**
     * função que envia para multicast o pedido de inserção da playlist
     * @param playlist_name
     * @param playlist_privilege
     * @param creator
     * @return
     */
    public synchronized boolean insertPlaylist(String playlist_name, String playlist_privilege, String creator) {
        boolean flag_insert_playlist = false;

        String message_to_multicast = "type|insert_playlist;playlist_name|" + playlist_name + ";playlist_privilege|" + playlist_privilege + ";creator|" + creator;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|insert_playlist;status|on") || message_from_multicast.equals("type|insert_playlist;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_insert_playlist = true;
            }
        }

        return flag_insert_playlist;
    }

    /**
     * função que envia para multicast o pedido de inserção de musica na playlist
     * @param playlist_name
     * @param music_name
     * @param album_name
     * @param artist_name
     * @param creator
     * @return
     */
    public synchronized boolean insertMusicPlaylist(String playlist_name, String music_name, String album_name, String artist_name, String creator) {
        boolean flag_insert_playlist = false;

        String message_to_multicast = "type|insert_music_playlist;playlist_name|" + playlist_name + ";music_name|" + music_name + ";album_name|" + album_name + ";artist_name|" + artist_name + ";creator|" + creator;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|insert_music_playlist;status|on") || message_from_multicast.equals("type|insert_music_playlist;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_insert_playlist = true;
            }
        }

        return flag_insert_playlist;
    }

    /**
     * função que envia para multicast o pedido de alteração do utilizador
     * @param user_to_edit
     * @param username
     * @param password
     * @param privilege
     * @return
     */

    //Edit
    public synchronized boolean editUser(String user_to_edit, String username, String password, String privilege) {
        boolean flag_edit_user = false;

        String message_to_multicast = "type|edit_user;user_to_edit|" + user_to_edit + ";username|" + username + ";password|" + password + ";privilege|" + privilege;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|edit_user;status|on") || message_from_multicast.equals("type|edit_user;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_edit_user = true;
            }
        }

        return flag_edit_user;
    }

    /**
     * função que envia para multicast o pedido de alteração do artista
     * @param artist_to_edit
     * @param artist_name
     * @param artist_age
     * @param artist_music_style
     * @param artist_biography
     * @return
     */
    public synchronized boolean editArtist(String artist_to_edit, String artist_name, String artist_age, String artist_music_style, String artist_biography) {
        boolean flag_edit_artist = false;

        String message_to_multicast = "type|edit_artist;artist_to_edit|" + artist_to_edit + ";artist_name|" + artist_name + ";artist_age|" + artist_age + ";artist_music_style|" + artist_music_style + ";artist_biography|" + artist_biography;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|edit_artist;status|on") || message_from_multicast.equals("type|edit_artist;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_edit_artist = true;
            }
        }

        return flag_edit_artist;
    }

    /**
     * função que envia para multicast o pedido de alteração do album
     * @param album_to_edit
     * @param album_name
     * @param album_record_label
     * @param artist_name
     * @return
     * @throws RemoteException
     */
    public synchronized boolean editAlbum(String album_to_edit, String album_name, String album_record_label, String artist_name) throws RemoteException {
        boolean flag_edit_album = false;

        String new_request ="type|users_list_album;album_name|"+album_to_edit;
        String users_list = sendPacket(new_request);
        System.out.println(users_list);

        String message_to_multicast = "type|edit_album;album_to_edit|" + album_to_edit + ";album_name|" + album_name + ";album_record_label|" + album_record_label + ";artist_name|" + artist_name;
        String message_from_multicast = sendPacket(message_to_multicast);
        System.out.println(message_from_multicast);
        if (message_from_multicast.equals("type|edit_album;status|on") || message_from_multicast.equals("type|edit_album;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_edit_album = true;

            }
        }
        album_aux = album_name;
        sendToWebsockeText();
        return flag_edit_album;
    }

    /**
     * função que envia para multicast o pedido de alteração da musica
     * @param music_to_edit
     * @param music_name
     * @param music_gender
     * @param music_lyrics
     * @param music_composer
     * @param music_release_date
     * @param album_name
     * @param artist_name
     * @return
     */
    public synchronized boolean editMusic(String music_to_edit, String music_name, String music_gender, String music_lyrics, String music_composer, String music_release_date, String album_name, String artist_name) {
        boolean flag_edit_music = false;

        String message_to_multicast = "type|edit_music;music_to_edit|" + music_to_edit + ";music_name|" + music_name + "; music_gender|" + music_gender + ";music_lyrics|" + music_lyrics + ";music_composer|" + music_composer + ";music_release_date|" + music_release_date + ";album_name|" + album_name + ";artist_name|" + artist_name;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|edit_music;status|on") || message_from_multicast.equals("type|edit_music;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_edit_music = true;
            }
        }

        return flag_edit_music;
    }

    /**
     * função que envia para multicast o pedido de alteração da critica
     * @param review_to_edit
     * @param username
     * @param description
     * @param rating
     * @param album_name
     * @param artist_name
     * @return
     */
    public synchronized boolean editReview(String review_to_edit, String username, String description, String rating, String album_name, String artist_name) {
        boolean flag_edit_review = false;

        String message_to_multicast = "type|edit_review;review_to_edit|" + review_to_edit + "username|" + username + ";description|" + description + ";rating|" + rating + ";album_name|" + album_name + ";artist_name|" + artist_name;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|edit_review;status|on") || message_from_multicast.equals("type|edit_review;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_edit_review = true;
            }
        }
        album_aux = album_name;
        try {
            sendToWebsockeText();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return flag_edit_review;
    }

    /**
     * função que envia para multicast o pedido de alteração da playlist
     * @param playlist_to_edit
     * @param playlist_name
     * @param playlist_privilege
     * @param creator
     * @return
     */
    public synchronized boolean editPlaylist(String playlist_to_edit, String playlist_name, String playlist_privilege, String creator) {
        boolean flag_edit_playlist = false;

        String message_to_multicast = "type|edit_playlist;playlist_to_edit|" + playlist_to_edit + ";playlist_name|" + playlist_name + ";playlist_privilege|" + playlist_privilege + ";creator|" + creator;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|edit_playlist;status|on") || message_from_multicast.equals("type|edit_playlist;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_edit_playlist = true;
            }
        }

        return flag_edit_playlist;
    }

    /**
     * função que envia para multicast o pedido de remoção do user
     * @param username
     * @return
     */
    //Remove
    public synchronized boolean removeUser(String username) {
        boolean flag_remove_user = false;

        String message_to_multicast = "type|remove_user;username|" + username;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|remove_user;status|on") || message_from_multicast.equals("type|remove_user;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_remove_user = true;
            }
        }

        return flag_remove_user;

    }

    /**
     * função que envia para multicast o pedido de remoção do artista
     * @param artist_name
     * @return
     */
    public synchronized boolean removeArtist(String artist_name) {
        boolean flag_remove_artist = false;

        String message_to_multicast = "type|remove_artist;artist_name|" + artist_name;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|remove_artist;status|on") || message_from_multicast.equals("type|remove_artist;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_remove_artist = true;
            }
        }

        return flag_remove_artist;
    }

    /**
     * função que envia para multicast o pedido de remoção do album
     * @param album_name
     * @param artist_name
     * @return
     */
    public synchronized boolean removeAlbum(String album_name, String artist_name) {
        boolean flag_remove_album = false;

        String message_to_multicast = "type|remove_album;album_name|" + album_name + ";artist_name|" + artist_name;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|remove_album;status|on") || message_from_multicast.equals("type|remove_album;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_remove_album = true;
            }
        }

        return flag_remove_album;
    }

    /**
     * função que envia para multicast o pedido de remoção da musica
     * @param music_name
     * @param album_name
     * @param artist_name
     * @return
     */
    public synchronized boolean removeMusic(String music_name, String album_name, String artist_name) {
        boolean flag_remove_music = false;

        String message_to_multicast = "type|remove_music;music_name|" + music_name + ";album_name|" + album_name + ";artist_name|" + artist_name;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|remove_music;status|on") || message_from_multicast.equals("type|remove_music;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_remove_music = true;
            }
        }

        return flag_remove_music;
    }

    /**
     * função que envia para multicast o pedido de remoção da critica
     * @param album_name
     * @param username
     * @param artist_name
     * @return
     */
    public synchronized boolean removeReview(String album_name, String username, String artist_name) {
        boolean flag_remove_review = false;

        String message_to_multicast = "type|remove_review;album_name|" + album_name + ";username|" + username + ";artist_name|" + artist_name;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|remove_review;status|on") || message_from_multicast.equals("type|remove_review;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_remove_review = true;
            }
        }

        return flag_remove_review;
    }

    /**
     * função que envia para multicast o pedido de remoção da playlist
     * @param playlist_name
     * @param username
     * @return
     */
    public synchronized boolean removePlaylist(String playlist_name, String username) {
        boolean flag_remove_playlist = false;

        String message_to_multicast = "type|remove_playlist;playlist_name|" + playlist_name + ";username|" + username;
        String message_from_multicast = sendPacket(message_to_multicast);

        if (message_from_multicast.equals("type|remove_playlist;status|on") || message_from_multicast.equals("type|remove_playlist;status|off")) {

            String[] data = message_from_multicast.split(";");
            String[] aux_status = data[1].split("\\|");
            String status = aux_status[1];

            if (status.equals("on")) {
                flag_remove_playlist = true;
            }
        }

        return flag_remove_playlist;
    }

    /**
     * função que envia para multicast o pedido de pesquisa de utilizadores
     * @return
     */
    //Search
    public synchronized ArrayList<String> searchUser() {
        ArrayList<String> users_list = new ArrayList<>();

        String message_to_multicast = "type|search_user";
        String raw = sendPacket(message_to_multicast);

        String[] data = raw.split(";");

        String[] aux_count = data[1].split("\\|");
        int count = Integer.parseInt(aux_count[1]);

        if (count == 0) {
            return users_list;
        } else {
            for (int i = 2; i < count + 2; i++) {
                String[] aux_user = data[i].split("\\|");
                String user = aux_user[1];

                users_list.add(user);
            }

            return users_list;
        }
    }


    /**
     * função que envia para multicast o pedido de pesquisa de artistas
     * @return
     */
    public synchronized ArrayList<String> searchArtist() {
        ArrayList<String> artists_list = new ArrayList<>();

        String message_to_multicast = "type|search_artist";
        String raw = sendPacket(message_to_multicast);

        String[] data = raw.split(";");

        String[] aux_count = data[1].split("\\|");
        int count = Integer.parseInt(aux_count[1]);

        if (count == 0) {
            return artists_list;
        } else {

            for (int i = 2; i < count + 2; i++) {
                String[] aux_artist = data[i].split("\\|");
                String artist = aux_artist[1];

                artists_list.add(artist);
            }

            return artists_list;
        }
    }

    /**
     * função que envia para multicast o pedido de pesquisa de albums
     * @return
     */
    public synchronized ArrayList<String> searchAlbum() {
        ArrayList<String> albums_list = new ArrayList<>();

        String message_to_multicast = "type|search_album";
        String raw = sendPacket(message_to_multicast);

        String[] data = raw.split(";");

        String[] aux_count = data[1].split("\\|");
        int count = Integer.parseInt(aux_count[1]);

        if (count == 0) {
            return albums_list;
        } else {
            for (int i = 2; i < count + 2; i++) {
                String[] aux_album = data[i].split("\\|");
                String album = aux_album[1];

                albums_list.add(album);
            }

            return albums_list;
        }
    }

    /**
     * função que envia para multicast o pedido de pesquisa de albums por nome do album
     * @param album_name
     * @return
     */
    public synchronized ArrayList<String> searchAlbumByName(String album_name) {
        ArrayList<String> album_by_name_list = new ArrayList<>();

        String message_to_multicast = "type|search_album_by_name;album_name|" + album_name;
        String raw = sendPacket(message_to_multicast);

        String[] data = raw.split(";");

        String[] aux_count = data[1].split("\\|");
        int count = Integer.parseInt(aux_count[1]);

        if (count == 0) {
            return album_by_name_list;
        } else {
            for (int i = 2; i < count + 2; i++) {
                String[] aux_album_by_name = data[i].split("\\|");
                String album_by_name = aux_album_by_name[1];

                album_by_name_list.add(album_by_name);
            }

            return album_by_name_list;
        }
    }

    /**
     * função que envia para multicast o pedido de pesquisa de albums por nome de artista
     * @param artist_name
     * @return
     */
    public synchronized ArrayList<String> searchAlbumByArtist(String artist_name) {
        ArrayList<String> album_by_artist_list = new ArrayList<>();

        String message_to_multicast = "type|search_album_by_artist;artist_name|" + artist_name;
        String raw = sendPacket(message_to_multicast);

        String[] data = raw.split(";");

        String[] aux_count = data[1].split("\\|");
        int count = Integer.parseInt(aux_count[1]);

        if (count == 0) {
            return album_by_artist_list;
        } else {
            for (int i = 2; i < count + 2; i++) {
                String[] aux_album_by_artist = data[i].split("\\|");
                String album_by_artist = aux_album_by_artist[1];

                album_by_artist_list.add(album_by_artist);
            }

            return album_by_artist_list;
        }
    }

    /**
     * função que envia para multicast o pedido de pesquisa de musicas
     * @return
     */
    public synchronized ArrayList<String> searchMusic() {
        ArrayList<String> musics_list = new ArrayList<>();

        String message_to_multicast = "type|search_music";
        String raw = sendPacket(message_to_multicast);

        String[] data = raw.split(";");

        String[] aux_count = data[1].split("\\|");
        int count = Integer.parseInt(aux_count[1]);

        if (count == 0) {
            return musics_list;
        } else {
            for (int i = 2; i < count + 2; i++) {
                String[] aux_music = data[i].split("\\|");
                String music = aux_music[1];

                musics_list.add(music);
            }

            return musics_list;
        }
    }

    /**
     * função que envia para multicast o pedido de pesquisa de musicas por nome de album de um artista
     * @param album_name
     * @param artist_name
     * @return
     */
    public synchronized ArrayList<String> searchMusicByAlbum(String album_name, String artist_name) {
        ArrayList<String> music_by_album_list = new ArrayList<>();

        String message_to_multicast = "type|search_music_by_album;album_name|" + album_name + ";artist_name|" + artist_name;
        String raw = sendPacket(message_to_multicast);

        String[] data = raw.split(";");

        String[] aux_count = data[1].split("\\|");
        int count = Integer.parseInt(aux_count[1]);

        if (count == 0) {
            return music_by_album_list;
        } else {
            for (int i = 2; i < count + 2; i++) {
                String[] aux_music_by_album = data[i].split("\\|");
                String music_by_album = aux_music_by_album[1];

                music_by_album_list.add(music_by_album);
            }

            return music_by_album_list;
        }
    }

    /**
     * função que envia para multicast o pedido de pesquisa de musicas através da playlista de um utilizador
     * @param playlist_name
     * @param creator
     * @return
     */
    public synchronized ArrayList<String> searchMusicByPlaylist(String playlist_name, String creator) {
        ArrayList<String> music_by_playlist_list = new ArrayList<>();

        String message_to_multicast = "type|search_music_by_playlist;playlist_name|" + playlist_name + ";creator|" + creator;
        String raw = sendPacket(message_to_multicast);

        String[] data = raw.split(";");

        String[] aux_count = data[1].split("\\|");
        int count = Integer.parseInt(aux_count[1]);

        if (count == 0) {
            return music_by_playlist_list;
        } else {
            for (int i = 2; i < count + 2; i++) {
                String[] aux_music_by_playlist = data[i].split("\\|");
                String music_by_playlist = aux_music_by_playlist[1];

                music_by_playlist_list.add(music_by_playlist);
            }

            return music_by_playlist_list;
        }
    }

    /**
     * função que envia para multicast o pedido de pesquisa de playlist publicas
     * @return
     */
    public synchronized ArrayList<String> searchPublicPlaylist() {
        ArrayList<String> public_playlists_list = new ArrayList<>();

        String message_to_multicast = "type|search_public_playlist";
        String raw = sendPacket(message_to_multicast);

        String[] data = raw.split(";");

        String[] aux_count = data[1].split("\\|");
        int count = Integer.parseInt(aux_count[1]);

        if (count == 0) {
            return public_playlists_list;
        } else {
            for (int i = 2; i < count + 2; i++) {
                String[] aux_public_playlist = data[i].split("\\|");
                String public_playlist = aux_public_playlist[1];

                public_playlists_list.add(public_playlist);
            }

            return public_playlists_list;
        }
    }

    /**
     * função que envia para multicast o pedido de pesquisa de playlists privadas
     * @param username
     * @return
     */
    public synchronized ArrayList<String> searchPrivatePlaylist(String username) {
        ArrayList<String> private_playlists_list = new ArrayList<>();

        String message_to_multicast = "type|search_private_playlist;username|" + username;
        String raw = sendPacket(message_to_multicast);

        String[] data = raw.split(";");

        String[] aux_count = data[1].split("\\|");
        int count = Integer.parseInt(aux_count[1]);

        if (count == 0) {
            return private_playlists_list;
        } else {
            for (int i = 2; i < count + 2; i++) {
                String[] aux_private_playlist = data[i].split("\\|");
                String private_playlist = aux_private_playlist[1];

                private_playlists_list.add(private_playlist);
            }

            return private_playlists_list;
        }
    }

    //Details

    /**
     * função que envia para multicast o pedido de consulta de detalhes de um utilizador
     * @param username
     * @return
     */
    public synchronized ArrayList<String> detailsUser(String username) {
        ArrayList<String> details_user = new ArrayList<>();

        String message_to_multicast = "type|details_user;username|" + username;
        String raw = sendPacket(message_to_multicast);

        String[] data = raw.split(";");

        String[] aux_password = data[1].split("\\|");
        String password = aux_password[1];

        String[] aux_privilege = data[2].split("\\|");
        String privilege = aux_privilege[1];

        details_user.add(username);
        details_user.add(password);
        details_user.add(privilege);

        return details_user;
    }

    /**
     * função que envia para multicast o pedido de consulta de detalhes de um artista
     * @param artist_name
     * @return
     */
    public synchronized ArrayList<String> detailsArtist(String artist_name) {
        ArrayList<String> details_artist = new ArrayList<>();

        String message_to_multicast = "type|details_artist;artist|" + artist_name;
        String raw = sendPacket(message_to_multicast);

        String[] data = raw.split(";");

        String[] aux_age = data[1].split("\\|");
        String age = aux_age[1];

        String[] aux_music_style = data[2].split("\\|");
        String music_style = aux_music_style[1];

        String[] aux_biography = data[3].split("\\|");
        String biography = aux_biography[1];

        details_artist.add(artist_name);
        details_artist.add(age);
        details_artist.add(music_style);
        details_artist.add(biography);

        return details_artist;
    }

    /**
     * função que envia para multicast o pedido de consulta de detalhes de um album
     * @param album_name
     * @param artist_name
     * @return
     */
    public synchronized ArrayList<String> detailsAlbum(String album_name, String artist_name) {
        ArrayList<String> details_album = new ArrayList<>();

        String message_to_multicast = "type|details_album;album_name|" + album_name + ";artist_name|" + artist_name;
        String raw = sendPacket(message_to_multicast);

        String[] data = raw.split(";");

        String[] aux_record_label = data[1].split("\\|");
        String record_label = aux_record_label[1];

        details_album.add(album_name);
        details_album.add(record_label);

        return details_album;
    }

    /**
     * função que envia para multicast o pedido de consulta de detalhes de uma musica
     * @param music_name
     * @param album_name
     * @param artist_name
     * @return
     */
    public synchronized ArrayList<String> detailsMusic(String music_name, String album_name, String artist_name) {
        ArrayList<String> details_music = new ArrayList<>();

        String message_to_multicast = "type|details_music;music_name|" + music_name + ";album_name|" + album_name + ";artist_name|" + artist_name;
        String raw = sendPacket(message_to_multicast);

        String[] data = raw.split(";");

        String[] aux_music_gender = data[1].split("\\|");
        String music_gender = aux_music_gender[1];

        String[] aux_lyrics = data[2].split("\\|");
        String lyrics = aux_lyrics[1];

        String[] aux_composer = data[3].split("\\|");
        String composer = aux_composer[1];

        String[] aux_release_date = data[4].split("\\|");
        String release_date = aux_release_date[1];

        details_music.add(music_name);
        details_music.add(music_gender);
        details_music.add(lyrics);
        details_music.add(composer);
        details_music.add(release_date);

        return details_music;
    }

    /**
     * função que envia para multicast o pedido de consulta de detalhes das criticas de um album
     * @param album_name
     * @param artist_name
     * @return
     */
    public synchronized ArrayList<String> detailsReview(String album_name, String artist_name) {
        ArrayList<String> details_review = new ArrayList<>();

        String message_to_multicast = "type|details_review;album_name|" + album_name + ";artist_name|" + artist_name;
        String raw = sendPacket(message_to_multicast);

        String[] data = raw.split(";");

        String[] aux_count = data[1].split("\\|");
        int count = Integer.parseInt(aux_count[1]);

        if (count == 0) {
            return details_review;
        } else {
            for (int i = 2; i < (count * 3) + 2; i += 3) {
                String[] aux_username = data[i].split("\\|");
                String username = aux_username[1];

                String[] aux_description = data[i + 1].split("\\|");
                String description = aux_description[1];

                String[] aux_rating = data[i + 2].split("\\|");
                String rating = aux_rating[1];

                details_review.add(username);
                details_review.add(description);
                details_review.add(rating);

            }
            for(String s: details_review){
                System.out.println(s);
            }
            return details_review;
        }

    }

    /**
     * função que envia para multicast o pedido de upload
     * @param music
     * @return
     */
    public synchronized int uploadMusic(String music){
        String message_to_multicast = "type|upload_music;music_name|"+music;
        String message_from_multicast =sendPacket(message_to_multicast);
        String [] data = message_from_multicast.split(";");
        String [] aux_port = data[1].split("\\|");
        String port= aux_port[1];
        int server_port = Integer.parseInt(port);
        return server_port;

    }

    /**
     * função que envia para multicast o pedido de download
     * @param music
     * @return
     */
    public synchronized int downloadMusic(String music){
        String message_to_multicast = "type|download_music;music_name|"+music;
        String message_from_multicast =sendPacket(message_to_multicast);
        String [] data = message_from_multicast.split(";");
        String [] aux_port = data[1].split("\\|");
        String port= aux_port[1];
        int server_port = Integer.parseInt(port);
        return server_port;

    }

    /**
     * função que elimina o cliente da ArrayList de ClientInterface
     * @return
     */


    //Main
    public static void main(String args[]) {



        int calls = 0;
        while (calls < 5) {
            try {
                ServerInterface i = (ServerInterface) Naming.lookup("rmi://localhost:7000/DropMusic");
                System.out.println("Call to primary RMI");
                i.testConnectionRMI();
                calls = 0;
            } catch (NotBoundException | RemoteException re) {
                System.out.println("Call failed");
                calls++;
            } catch (MalformedURLException e) {
                e.getMessage();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            RmiServer rmi_server = new RmiServer();
            Naming.rebind("rmi://localhost:7000/DropMusic", rmi_server);
            System.out.println("Primary RMI Server ready.");
        } catch (RemoteException re) {
            System.out.println("Exception in RMIServer.main: " + re);
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException in RMIServer.main: " + e);
        }
    }

}

