package multicast;

import classes.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MulticastServer extends Thread{
    private String MULTICAST_ADDRESS = "224.2.224.2";
    private int PORT = 4500;
    private int RMI_PORT=4501;
    private long SLEEP_TIME = 1000;
    private static ArrayList<User> users;
    private static ArrayList<Artist> artists;
    private static File f1 = new File("users");
    private static File f2 = new File("artists");

    public static void main(String[] args) {
        loadDataUsers();
        loadDataArtists();
        MulticastServer multicast_server = new MulticastServer();
        multicast_server.start();

    }


    public void run() {
        MulticastSocket receive_socket = null;
        MulticastSocket send_socket = null;
        System.out.println("Multicast Server is ready:");
        readRequests(receive_socket,send_socket);
    }

    /**
     * função que vai receber os pedidos do rmi e enviar uma diferente
     * @param receive_socket
     * @param send_socket
     */
    private void readRequests(MulticastSocket receive_socket, MulticastSocket send_socket){

        try {
            receive_socket = new MulticastSocket(PORT);  // create socket and bind it
            send_socket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);;
            receive_socket.joinGroup(group);
            while (true) {
                byte[] buffer = new byte[256];
                String []data;
                DatagramPacket receive_packet = new DatagramPacket(buffer, buffer.length);
                DatagramPacket send_packet;

                receive_socket.receive(receive_packet);
                String message = new String(receive_packet.getData(), 0, receive_packet.getLength());
                System.out.println("Receive request from RMI Server: " + message);
                System.out.println();

                data = message.split(";");


                switch(data[0]){
                    case "type|check_login":
                        boolean flag_check_login;

                        flag_check_login = checkLogin(message);

                        if(flag_check_login){
                            String message_to_rmi = "type|check_login;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|check_login;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|check_user_privilege":
                        boolean flag_check_user_privilege;

                        flag_check_user_privilege = checkUserPrivilege(message);

                        if(flag_check_user_privilege){
                            String message_to_rmi = "type|check_user_privilege;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|check_user_privilege;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|check_user":
                        boolean flag_check_user;

                        flag_check_user = checkUser(message);

                        if(flag_check_user){
                            String message_to_rmi = "type|check_user;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|check_user;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|check_artist":
                        boolean flag_check_artist;

                        flag_check_artist = checkArtist(message);

                        if(flag_check_artist){
                            String message_to_rmi = "type|check_artist;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|check_artist;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|check_album":
                        boolean flag_check_album ;

                        flag_check_album = checkAlbum(message);

                        if(flag_check_album){
                            String message_to_rmi = "type|check_album;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|check_album;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|check_music":
                        boolean flag_check_music ;

                        flag_check_music = checkMusic(message);

                        if(flag_check_music){
                            String message_to_rmi = "type|check_music;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|check_music;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|check_review":
                        boolean flag_check_review ;

                        flag_check_review  = checkReview(message);

                        if(flag_check_review ){
                            String message_to_rmi = "type|check_review;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|check_review;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|check_playlist":
                        boolean flag_check_playlist ;

                        flag_check_playlist  = checkPlaylist(message);

                        if(flag_check_playlist){
                            String message_to_rmi = "type|check_playlist;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|check_playlist;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|check_user_playlist":
                        boolean flag_check_user_playlist ;

                        flag_check_user_playlist  = checkUserPlaylist(message);

                        if(flag_check_user_playlist){
                            String message_to_rmi = "type|check_user_playlist;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|check_user_playlist;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|insert_artist":
                        boolean flag_insert_artist;

                        flag_insert_artist = insertArtist(message);

                        if(flag_insert_artist){
                            String message_to_rmi = "type|insert_artist;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|insert_artist;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|insert_user":
                        boolean flag_insert_user ;

                        flag_insert_user = insertUser(message);

                        if(flag_insert_user){
                            String message_to_rmi = "type|insert_user;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|insert_user;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|insert_user_privilege":
                        boolean flag_insert_user_privilege;

                        flag_insert_user_privilege = insertUserPrivilege(message);

                        if(flag_insert_user_privilege){
                            String message_to_rmi = "type|insert_user_privilege;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|insert_user_privilege;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|insert_album":
                        boolean flag_insert_album ;

                        flag_insert_album  = insertAlbum(message);

                        if(flag_insert_album){
                            String message_to_rmi = "type|insert_album;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|insert_album;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|insert_review":
                        boolean flag_insert_review ;

                        flag_insert_review  = insertReview(message);

                        if(flag_insert_review){
                            String message_to_rmi = "type|insert_review;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|insert_review;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|insert_music":
                        boolean flag_insert_music;

                        flag_insert_music  = insertMusic(message);

                        if(flag_insert_music){
                            String message_to_rmi = "type|insert_music;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|insert_music;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|insert_playlist":
                        boolean flag_insert_playlist;

                        flag_insert_playlist  = insertPlaylist(message);

                        if(flag_insert_playlist){
                            String message_to_rmi = "type|insert_playlist;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|insert_playlist;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|insert_music_playlist":
                        boolean flag_insert_music_playlist ;

                        flag_insert_music_playlist  = insertMusicPlaylist(message);

                        if(flag_insert_music_playlist){
                            String message_to_rmi = "type|insert_music_playlist;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|insert_music_playlist;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|edit_user":
                        boolean flag_edit_user;

                        flag_edit_user  = editUser(message);

                        if(flag_edit_user){
                            String message_to_rmi = "type|edit_user;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|edit_user;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|edit_album":
                        boolean flag_edit_album ;

                        flag_edit_album  = editAlbum(message);

                        if(flag_edit_album){
                            String message_to_rmi = "type|edit_album;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|edit_album;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|edit_artist":
                        boolean flag_edit_artist ;

                        flag_edit_artist  = editArtist(message);

                        if(flag_edit_artist){
                            String message_to_rmi = "type|edit_artist;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|edit_artist;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|edit_music":
                        boolean flag_edit_music ;

                        flag_edit_music  = editMusic(message);

                        if(flag_edit_music){
                            String message_to_rmi = "type|edit_music;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|edit_music;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|edit_review":
                        boolean flag_edit_review ;

                        flag_edit_review  = editReview(message);

                        if(flag_edit_review){
                            String message_to_rmi = "type|edit_review;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|edit_review;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|edit_playlist":
                        boolean flag_edit_playlist ;

                        flag_edit_playlist  = editPlaylist(message);

                        if(flag_edit_playlist){
                            String message_to_rmi = "type|edit_playlist;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|edit_playlist;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|remove_user":
                        boolean flag_remove_user;

                        flag_remove_user  = removeUser(message);

                        if(flag_remove_user){
                            String message_to_rmi = "type|remove_user;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|remove_user;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|remove_artist":
                        boolean flag_remove_artist;

                        flag_remove_artist  = removeArtist(message);

                        if(flag_remove_artist){
                            String message_to_rmi = "type|remove_artist;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|remove_artist;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|remove_album":
                        boolean flag_remove_album ;

                        flag_remove_album  = removeAlbum(message);

                        if(flag_remove_album){
                            String message_to_rmi = "type|remove_album;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|remove_album;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|remove_music":
                        boolean flag_remove_music ;

                        flag_remove_music  = removeMusic(message);

                        if(flag_remove_music){
                            String message_to_rmi = "type|remove_music;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|remove_music;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|remove_review":
                        boolean flag_remove_review;

                        flag_remove_review  = removeReview(message);

                        if(flag_remove_review){
                            String message_to_rmi = "type|remove_review;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|remove_review;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|remove_playlist":
                        boolean flag_remove_playlist;

                        flag_remove_playlist  = removePlaylist(message);

                        if(flag_remove_playlist){
                            String message_to_rmi = "type|remove_playlist;status|on";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }else{
                            String message_to_rmi = "type|remove_playlist;status|off";
                            send_packet = new DatagramPacket(message_to_rmi.getBytes(),message_to_rmi.getBytes().length,group,RMI_PORT);
                            send_socket.send(send_packet);
                        }
                        break;
                    case "type|search_user":

                        String search_user = searchUser();
                        send_packet = new DatagramPacket(search_user.getBytes(),search_user.getBytes().length,group,RMI_PORT);
                        send_socket.send(send_packet);

                        break;
                    case "type|search_artist":

                        String search_artist = searchArtist();
                        send_packet = new DatagramPacket(search_artist.getBytes(),search_artist.getBytes().length,group,RMI_PORT);
                        send_socket.send(send_packet);

                        break;
                    case "type|search_album":

                        String search_album = searchAlbum();
                        send_packet = new DatagramPacket(search_album.getBytes(),search_album.getBytes().length,group,RMI_PORT);
                        send_socket.send(send_packet);

                        break;
                    case "type|search_album_by_name":

                        String search_album_by_name = searchAlbumByName(message);
                        send_packet = new DatagramPacket(search_album_by_name.getBytes(),search_album_by_name.getBytes().length,group,RMI_PORT);
                        send_socket.send(send_packet);

                        break;
                    case "type|search_album_by_artist":

                        String search_album_by_artist = searchAlbumByArtist(message);
                        send_packet = new DatagramPacket(search_album_by_artist.getBytes(),search_album_by_artist.getBytes().length,group,RMI_PORT);
                        send_socket.send(send_packet);

                        break;
                    case "type|search_music":

                        String search_music = searchMusic();
                        send_packet = new DatagramPacket(search_music.getBytes(),search_music.getBytes().length,group,RMI_PORT);
                        send_socket.send(send_packet);

                        break;
                    case "type|search_music_by_album":

                        String search_music_by_album = searchMusicByAlbum(message);
                        send_packet = new DatagramPacket(search_music_by_album.getBytes(),search_music_by_album.getBytes().length,group,RMI_PORT);
                        send_socket.send(send_packet);

                        break;
                    case "type|search_music_by_playlist":

                        String search_music_by_playlist = searchMusicByPlaylist(message);
                        send_packet = new DatagramPacket(search_music_by_playlist.getBytes(),search_music_by_playlist.getBytes().length,group,RMI_PORT);
                        send_socket.send(send_packet);

                        break;
                    case "type|search_public_playlist":

                        String search_public_playlist = searchPublicPlaylist();
                        send_packet = new DatagramPacket(search_public_playlist.getBytes(),search_public_playlist.getBytes().length,group,RMI_PORT);
                        send_socket.send(send_packet);

                        break;
                    case "type|search_private_playlist":

                        String search_private_playlist = searchPrivatePlaylist(message);
                        send_packet = new DatagramPacket(search_private_playlist.getBytes(),search_private_playlist.getBytes().length,group,RMI_PORT);
                        send_socket.send(send_packet);

                        break;
                    case "type|details_user":

                        String details_user = detailsUser(message);
                        send_packet = new DatagramPacket(details_user.getBytes(),details_user.getBytes().length,group,RMI_PORT);
                        send_socket.send(send_packet);

                        break;
                    case "type|details_artist":

                        String details_artist = detailsArtist(message);
                        send_packet = new DatagramPacket(details_artist.getBytes(),details_artist.getBytes().length,group,RMI_PORT);
                        send_socket.send(send_packet);

                        break;
                    case "type|details_album":

                        String details_album = detailsAlbum(message);
                        send_packet = new DatagramPacket(details_album.getBytes(),details_album.getBytes().length,group,RMI_PORT);
                        send_socket.send(send_packet);

                        break;
                    case "type|details_music":

                        String details_music = detailsMusic(message);
                        send_packet = new DatagramPacket(details_music.getBytes(),details_music.getBytes().length,group,RMI_PORT);
                        send_socket.send(send_packet);

                        break;
                    case "type|details_review":

                        String details_review = detailsReview(message);
                        send_packet = new DatagramPacket(details_review.getBytes(),details_review.getBytes().length,group,RMI_PORT);
                        send_socket.send(send_packet);

                        break;
                    case "type|users_list_album":
                        String users_list_album = usersOfAlbuns(message);
                        send_packet=new DatagramPacket(users_list_album.getBytes(),users_list_album.getBytes().length,group,RMI_PORT);
                        send_socket.send(send_packet);
                        break;
                    case "type|upload_music":
                        Random random =new Random();
                        int server_port = random.nextInt((6000-5000)+1) +5000;
                        ServerSocket serverSocket = new ServerSocket(server_port);
                        System.out.println("I'm connected in port : "+server_port);
                        String port_to_client = "type|upload_music;port|"+Integer.toString(server_port);
                        send_packet=new DatagramPacket(port_to_client.getBytes(),port_to_client.getBytes().length,group,RMI_PORT);
                        send_socket.send(send_packet);
                        uploadMusic(message,serverSocket);
                        break;
                    case "type|download_music":
                        Random rand =new Random();
                        int serverPort = rand.nextInt((6000-5000)+1) +5000;
                        ServerSocket server = new ServerSocket(serverPort);
                        System.out.println("I'm connected in port : "+serverPort);
                        String porttoclient = "type|download_music;port|"+Integer.toString(serverPort);
                        send_packet=new DatagramPacket(porttoclient.getBytes(),porttoclient.getBytes().length,group,RMI_PORT);
                        send_socket.send(send_packet);
                        downloadMusic(message,server);
                        break;
                }


                try { sleep((long) (Math.random() * SLEEP_TIME)); } catch (InterruptedException e) { }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            receive_socket.close();
            send_socket.close();
        }


    }

    /**
     * função que quando recebe o pedido por parte do cliente, liga uma Server Socket para poder fazer transferências de musicas para o client através de TCP
     * @param message_from_multicast
     * @param serverSocket
     */
    private static void downloadMusic(String message_from_multicast, ServerSocket serverSocket){
        String [] data = message_from_multicast.split(";");

        String [] aux_music = data[1].split("\\|");
        String music = aux_music[1];
        String path = "/Users/pedrochicoria/Desktop/UC/LEI/SD/SD_Project/DropMusic/src/"+music+".mp3";
        try {
            Socket client_socket = serverSocket.accept();;
            File file = new File(path);
            FileInputStream fis = null;

            fis = new FileInputStream(file);

            BufferedInputStream bis = new BufferedInputStream(fis);

            OutputStream os = null;
            try {
                os = client_socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] contents;
            long fileLength = file.length();
            long current = 0;

            while (current != fileLength) {
                int size = 10000;
                if (fileLength - current >= size)
                    current += size;
                else {
                    size = (int) (fileLength - current);
                    current = fileLength;
                }
                contents = new byte[size];
                bis.read(contents, 0, size);
                os.write(contents);
                System.out.print("Sending file ... " + (current * 100) / fileLength + "% complete!\n");
            }

            os.flush();
            client_socket.close();
            serverSocket.close();
            System.out.println("File sent succesfully!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * função que quando recebe o pedido por parte do cliente, liga uma Server Socket para poder receber transferências de musicas do cliente através de TCP
     * @param message_from_multicast
     * @param serverSocket
     */
    //upload and download musics
    private static void uploadMusic(String message_from_multicast,ServerSocket serverSocket){
        String [] data = message_from_multicast.split(";");

        String [] aux_music = data[1].split("\\|");
        String music = aux_music[1];
        String path = "/Users/pedrochicoria/Desktop/UC/LEI/SD/SD_Project/DropMusic/Upload/"+music+".mp3";

        try {

            Socket socket = serverSocket.accept();
            System.out.println(socket.isConnected());

            byte[] contents = new byte[10000];
            FileOutputStream fos = new FileOutputStream(path);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            InputStream is = socket.getInputStream();

            int bytesRead = 0;

            while ((bytesRead = is.read(contents)) != -1)
                bos.write(contents, 0, bytesRead);

            bos.flush();
            socket.close();



            serverSocket.close();
            System.out.println("File saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * função que verifica se a autenticação está correta
     * @param message_from_rmi
     * @return
     */
    //Check
    private boolean checkLogin(String message_from_rmi){

        String [] data = message_from_rmi.split(";");

        String [] aux_username = data[1].split("\\|");
        String username = aux_username[1];

        String [] aux_password = data[2].split("\\|");
        String password = aux_password[1];

        for (User user : users){
            if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * função que verifica o privilégio do utilizador
     * @param message_from_rmi
     * @return
     */
    private boolean checkUserPrivilege(String message_from_rmi){

        String [] data = message_from_rmi.split(";");

        String [] aux_username = data[1].split("\\|");
        String username = aux_username[1];

        for (User user : users){
            if(user.getUsername().equals(username) && user.getPrivilege()) {
                return true;
            }
        }
        return false;
    }

    /**
     * função que verifica se o username está na lista de utilzadores
     * @param message_from_rmi
     * @return
     */
    private boolean checkUser(String message_from_rmi){

        String [] data = message_from_rmi.split(";");

        String [] aux_username = data[1].split("\\|");
        String username = aux_username[1];

        for (User user : users){
            if(user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * função que verifica se o artista existe
     * @param message_from_rmi
     * @return
     */
    private boolean checkArtist(String message_from_rmi){

        String [] data = message_from_rmi.split(";");

        String [] aux_artist_name = data[1].split("\\|");
        String artist_name = aux_artist_name[1];

        for (Artist artist : artists){
            if(artist.getArtist_name().equals(artist_name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * função que verifica se o album existe
     * @param message_from_rmi
     * @return
     */
    private boolean checkAlbum(String message_from_rmi){

        String [] data = message_from_rmi.split(";");

        String [] aux_album_name = data[1].split("\\|");
        String album_name = aux_album_name[1];

        for (Artist a : artists){
            for(Album album : a.getAlbums()){
                if(album.getAlbum_name().equals(album_name)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * funcao que verifica se a musica existe
     * @param message_from_rmi
     * @return
     */
    private boolean checkMusic(String message_from_rmi){

        String [] data = message_from_rmi.split(";");

        String [] aux_music_name = data[1].split("\\|");
        String music_name = aux_music_name[1];

        for (Artist a : artists){
            for(Album album : a.getAlbums()){
                for(Music music : album.getMusics()){
                    if(music.getMusic_name().equals(music_name)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * funcao que verifica se a critica existe
     * @param message_from_rmi
     * @return
     */
    private boolean checkReview(String message_from_rmi){

        String [] data = message_from_rmi.split(";");

        String [] aux_album_name = data[1].split("\\|");
        String album_name = aux_album_name[1];

        String [] aux_username = data[2].split("\\|");
        String username = aux_username[1];

        for (Artist a : artists){
            for(Album album : a.getAlbums()){
                if(album.getAlbum_name().equals(album_name)){
                    for(Review review : album.getReviews()){
                        if(review.getUsername().equals(username)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * funcao que verifica se a playlist existe
     * @param message_from_rmi
     * @return
     */
    private boolean checkPlaylist(String message_from_rmi){

        String [] data = message_from_rmi.split(";");

        String [] aux_playlist_name = data[1].split("\\|");
        String playlist_name = aux_playlist_name[1];

        for (User user : users){
            for(Playlist playlist : user.getPlaylists()){
                if(playlist.getName().equals(playlist_name)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * funcao que verifica se a playlist existe
     * @param message_from_rmi
     * @return
     */
    private boolean checkUserPlaylist(String message_from_rmi){

        String [] data = message_from_rmi.split(";");

        String [] aux_playlist_name = data[1].split("\\|");
        String playlist_name = aux_playlist_name[1];

        String [] aux_username = data[2].split("\\|");
        String username = aux_username[1];

        for (User user : users){
            if(user.getUsername().equals(username)){
                for(Playlist playlist : user.getPlaylists()){
                    if(playlist.getName().equals(playlist_name)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Insert

    /**
     * funcao que insere o user na lista de utilizadores
     * @param message_from_rmi
     * @return
     */
    private boolean insertUser(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_username = data[1].split("\\|");
        String username = aux_username[1];

        String [] aux_password = data[2].split("\\|");
        String password = aux_password[1];

        User user = new User(username,password);

        if(!users.isEmpty()) {
            for(User u : users){
                if(u.getUsername().equals(username)){
                    return false;
                }
            }
            users.add(user);
            saveDataUsers();
            return true;
        }
        user.setPrivilege(true);
        users.add(user);
        saveDataUsers();
        return true;
    }

    /**
     * funcao que altera o privilegio de um utilizador
     * @param message_from_rmi
     * @return
     */
    private boolean insertUserPrivilege(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_username = data[1].split("\\|");
        String username = aux_username[1];


        if(!users.isEmpty()) {
            for(User u : users){
                if(u.getUsername().equals(username)){
                    u.setPrivilege(true);
                    saveDataUsers();
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * funcao que insere um artista na lista de artistas
     * @param message_from_rmi
     * @return
     */
    private boolean insertArtist(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_artist_name = data[1].split("\\|");
        String artist_name = aux_artist_name[1];

        String [] aux_age = data[2].split("\\|");
        int age = Integer.parseInt(aux_age[1]);

        String [] aux_music_style = data[3].split("\\|");
        String music_style = aux_music_style[1];

        String [] aux_bio = data[4].split("\\|");
        String bio = aux_bio[1];

        Artist artist = new Artist(artist_name,age,music_style,bio);

        if(!artists.isEmpty()) {
            for(Artist a : artists){
                if(a.getArtist_name().equals(artist_name)){
                    return false;
                }
            }
            artists.add(artist);
            saveDataArtists();
            return true;
        }
        artists.add(artist);
        saveDataArtists();
        return true;
    }

    /**
     * funcao que insere um album na lista de albuns de um artista
     * @param message_from_rmi
     * @return
     */
    private boolean insertAlbum(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_album_name = data[1].split("\\|");
        String album_name = aux_album_name[1];

        String [] aux_record_label = data[2].split("\\|");
        String record_lavel = aux_record_label[1];

        String [] aux_artist_name = data[3].split("\\|");
        String artist_name = aux_artist_name[1];

        String [] aux_username = data[4].split("\\|");
        String username = aux_username[1];

        Album album = new Album(album_name,record_lavel);
        album.getUsersOfAlbun().add(username);

        if(!artists.isEmpty()) {

            for(Artist a : artists){
                if(a.getArtist_name().equals(artist_name)){
                    a.addAlbums(album);
                    saveDataArtists();
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * funcao que insere uma musica
     * @param message_from_rmi
     * @return
     */
    private boolean insertMusic(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_music_name = data[1].split("\\|");
        String music_name = aux_music_name[1];

        String [] aux_music_gender = data[2].split("\\|");
        String music_gender = aux_music_gender[1];

        String [] aux_lyrics = data[3].split("\\|");
        String lyrics = aux_lyrics[1];

        String [] aux_composer = data[4].split("\\|");
        String composer = aux_composer[1];

        String [] aux_release_date = data[5].split("\\|");
        String release_date = aux_release_date[1];

        String [] aux_album_name = data[6].split("\\|");
        String album_name = aux_album_name[1];

        String [] aux_artist_name = data[7].split("\\|");
        String artist_name = aux_artist_name[1];

        Music music = new Music(music_name,music_gender,lyrics,composer,release_date);

        if(!artists.isEmpty()) {
            for(Artist a : artists){
                if(a.getArtist_name().equals(artist_name)) {
                    for (Album cd : a.getAlbums()) {
                        if (cd.getAlbum_name().equals(album_name)) {
                            cd.addMusics(music);
                            saveDataArtists();
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
     * funcao que insere uma critica a um album
     * @param message_from_rmi
     * @return
     */
    private boolean insertReview(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_username = data[1].split("\\|");
        String username = aux_username[1];

        String [] aux_description = data[2].split("\\|");
        String description = aux_description[1];

        String [] aux_rating = data[3].split("\\|");
        String rating = aux_rating[1];

        String [] aux_album_name = data[4].split("\\|");
        String album_name = aux_album_name[1];

        String [] aux_artist_name = data[5].split("\\|");
        String artist_name = aux_artist_name[1];

        Review review = new Review(username,description,Integer.parseInt(rating));

        if(!artists.isEmpty()) {
            for(Artist a : artists){
                if(a.getArtist_name().equals(artist_name)) {
                    for (Album cd : a.getAlbums()) {
                        if (cd.getAlbum_name().equals(album_name)) {
                            cd.addReviews(review);
                            saveDataArtists();
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
     * funcao que insere uma playlist a um utilizador
     * @param message_from_rmi
     * @return
     */
    private boolean insertPlaylist(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_playlist_name = data[1].split("\\|");
        String playlist_name = aux_playlist_name[1];

        String [] aux_playlist_privilege = data[2].split("\\|");
        String playlist_privilege = aux_playlist_privilege[1];

        String [] aux_creator = data[3].split("\\|");
        String creator = aux_creator[1];

        boolean privilege;

        if(playlist_privilege.equals("public")){
            privilege = false;
        }
        else if(playlist_privilege.equals("private")){
            privilege = true;
        }
        else{
            return false;
        }

        Playlist playlist = new Playlist(playlist_name,creator,privilege);

        if(!users.isEmpty()) {
            for(User u : users){
                if(u.getUsername().equals(creator)){
                    u.addPlaylists(playlist);
                    saveDataUsers();
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * funcao que insere uma musica na playlist
     * @param message_from_rmi
     * @return
     */
    private boolean insertMusicPlaylist(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_playlist_name = data[1].split("\\|");
        String playlist_name = aux_playlist_name[1];

        String [] aux_music_name = data[2].split("\\|");
        String music_name= aux_music_name[1];

        String [] aux_album_name = data[3].split("\\|");
        String album_name= aux_album_name[1];

        String [] aux_artist_name = data[4].split("\\|");
        String artist_name= aux_artist_name[1];

        String [] aux_creator = data[5].split("\\|");
        String creator = aux_creator[1];

        if(!artists.isEmpty()) {
            for(Artist a : artists){
                if(a.getArtist_name().equals(artist_name)){
                    for(Album album : a.getAlbums()) {
                        if(album.getAlbum_name().equals(album_name)) {
                            for (Music music : album.getMusics()) {
                                if (music.getMusic_name().equals(music_name)) {
                                    for (User user : users) {
                                        if (user.getUsername().equals(creator)) {
                                            for (Playlist playlist : user.getPlaylists()) {
                                                if (playlist.getName().equals(playlist_name)) {
                                                    playlist.addMusics(music);
                                                    saveDataUsers();
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    //Edit

    /**
     * funcao que altera os detalhes de um utilizador
     * @param message_from_rmi
     * @return
     */
    private boolean editUser(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_user_to_edit = data[1].split("\\|");
        String user_to_edit = aux_user_to_edit[1];

        String [] aux_username = data[2].split("\\|");
        String username = aux_username[1];

        String [] aux_password = data[3].split("\\|");
        String password = aux_password[1];

        String [] aux_privilege = data[4].split("\\|");
        String privilege = aux_privilege[1];

        boolean user_privilege;

        if(privilege.equals("editor")){
            user_privilege = true;
        }
        else if(privilege.equals("regular")){
            user_privilege = false;
        }
        else{
            return false;
        }

        if(!users.isEmpty()) {
            for(User u : users){
                if(u.getUsername().equals(user_to_edit)){
                    u.setUsername(username);
                    u.setPassword(password);
                    u.setPrivilege(user_privilege);
                    saveDataUsers();
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * funcao que altera os detalhes do artista
     * @param message_from_rmi
     * @return
     */
    private boolean editArtist(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_artist_to_edit = data[1].split("\\|");
        String artist_to_edit = aux_artist_to_edit[1];

        String [] aux_artist_name = data[2].split("\\|");
        String artist_name = aux_artist_name[1];

        String [] aux_age = data[3].split("\\|");
        int age = Integer.parseInt(aux_age[1]);

        String [] aux_music_style = data[4].split("\\|");
        String music_style = aux_music_style[1];

        String [] aux_bio = data[5].split("\\|");
        String bio = aux_bio[1];


        if(!artists.isEmpty()) {
            for(Artist a : artists){
                if(a.getArtist_name().equals(artist_to_edit)){
                    a.setArtist_name(artist_name);
                    a.setAge(age);
                    a.setMusic_style(music_style);
                    a.setBiography(bio);
                    saveDataArtists();
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * funcao que altera os detalhes de um album
     * @param message_from_rmi
     * @return
     */
    private boolean editAlbum(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_album_to_edit = data[1].split("\\|");
        String album_to_edit= aux_album_to_edit[1];

        String [] aux_album_name = data[2].split("\\|");
        String album_name = aux_album_name[1];

        String [] aux_record_label = data[3].split("\\|");
        String record_lavel = aux_record_label[1];

        String [] aux_artist_name = data[4].split("\\|");
        String artist_name = aux_artist_name[1];

        String [] aux_username = data[5].split("\\|");
        String username = aux_username[1];

        if(!artists.isEmpty()) {
            for(Artist a : artists){
                if(a.getArtist_name().equals(artist_name)){
                    for(Album album : a.getAlbums()){
                        if(album.getAlbum_name().equals(album_to_edit)){
                            album.setAlbum_name(album_name);
                            album.setRecord_label(record_lavel);
                            album.getUsersOfAlbun().add(username);
                            saveDataArtists();
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
     * funcao que altera os detalhes de uma musica
     * @param message_from_rmi
     * @return
     */
    private boolean editMusic(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_music_to_edit = data[1].split("\\|");
        String music_to_edit = aux_music_to_edit[1];

        String [] aux_music_name = data[2].split("\\|");
        String music_name = aux_music_name[1];

        String [] aux_music_gender = data[3].split("\\|");
        String music_gender = aux_music_gender[1];

        String [] aux_lyrics = data[4].split("\\|");
        String lyrics = aux_lyrics[1];

        String [] aux_composer = data[5].split("\\|");
        String composer = aux_composer[1];

        String [] aux_release_date = data[6].split("\\|");
        String release_date = aux_release_date[1];

        String [] aux_album_name = data[7].split("\\|");
        String album_name = aux_album_name[1];

        String [] aux_artist_name = data[8].split("\\|");
        String artist_name = aux_artist_name[1];

        if(!artists.isEmpty()) {
            for(Artist a : artists){
                if(a.getArtist_name().equals(artist_name)) {
                    for (Album cd : a.getAlbums()) {
                        if (cd.getAlbum_name().equals(album_name)) {
                            for(Music music : cd.getMusics()){
                                if(music.getMusic_name().equals(music_to_edit)){
                                    music.setMusic_name(music_name);
                                    music.setMusical_gender(music_gender);
                                    music.setLyrisc(lyrics);
                                    music.setComposer(composer);
                                    music.setRealease_date(release_date);
                                    saveDataArtists();
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
     * funcao que altera os detalhes de uma critica
     * @param message_from_rmi
     * @return
     */
    private boolean editReview(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_review_to_edit = data[1].split("\\|");
        String review_to_edit = aux_review_to_edit[1];

        String [] aux_username = data[2].split("\\|");
        String username = aux_username[1];

        String [] aux_description = data[3].split("\\|");
        String description = aux_description[1];

        String [] aux_rating = data[4].split("\\|");
        String rating = aux_rating[1];

        String [] aux_album_name = data[5].split("\\|");
        String album_name = aux_album_name[1];

        String [] aux_artist_name = data[6].split("\\|");
        String artist_name = aux_artist_name[1];

        if(!artists.isEmpty()) {
            for(Artist a : artists){
                if(a.getArtist_name().equals(artist_name)) {
                    for (Album cd : a.getAlbums()) {
                        if (cd.getAlbum_name().equals(album_name)) {
                            for (Review review : cd.getReviews()){
                                if(review.getUsername().equals(review_to_edit)){
                                    review.setUsername(username);
                                    review.setRating(Integer.parseInt(rating));
                                    review.setText(description);
                                    saveDataArtists();
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
     * funcao que altera os detalhes de uma playlist
     * @param message_from_rmi
     * @return
     */
    private boolean editPlaylist(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_playlist_to_edit = data[1].split("\\|");
        String playlist_to_edit = aux_playlist_to_edit[1];

        String [] aux_playlist_name = data[2].split("\\|");
        String playlist_name = aux_playlist_name[1];

        String [] aux_playlist_privilege = data[3].split("\\|");
        String playlist_privilege = aux_playlist_privilege[1];

        String [] aux_creator = data[4].split("\\|");
        String creator = aux_creator[1];

        boolean privilege;

        if(playlist_privilege.equals("public")){
            privilege = false;
        }
        else if(playlist_privilege.equals("private")){
            privilege = true;
        }
        else{
            return false;
        }

        if(!users.isEmpty()) {
            for(User u : users){
                if(u.getUsername().equals(creator)){
                   for(Playlist playlist : u.getPlaylists()){
                       if(playlist.getName().equals(playlist_to_edit)) {
                           playlist.setCreator(creator);
                           playlist.setName(playlist_name);
                           playlist.setPrivilege(privilege);
                           saveDataArtists();
                           return true;
                       }
                   }
                }
            }
            return false;
        }
        return false;
    }

    //Remove

    /**
     * funcao que remove um utilizador
     * @param message_from_rmi
     * @return
     */
    private boolean removeUser(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_username = data[1].split("\\|");
        String username = aux_username[1];

        if(!users.isEmpty()) {
            for(User u : users){
                if(u.getUsername().equals(username)){
                    users.remove(u);
                    saveDataUsers();
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * funcao que remove um artista
     * @param message_from_rmi
     * @return
     */
    private boolean removeArtist(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_artist_name = data[1].split("\\|");
        String artist_name = aux_artist_name[1];

        if(!artists.isEmpty()) {
            for(Artist a : artists){
                if(a.getArtist_name().equals(artist_name)){
                    artists.remove(a);
                    saveDataArtists();
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * funcao que remove um album
     * @param message_from_rmi
     * @return
     */
    private boolean removeAlbum(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_album_name = data[1].split("\\|");
        String album_name = aux_album_name[1];

        String [] aux_artist_name = data[2].split("\\|");
        String artist_name = aux_artist_name[1];


        if(!artists.isEmpty()) {
            for(Artist a : artists){
                if(a.getArtist_name().equals(artist_name)){
                    for(Album album : a.getAlbums()){
                        if(album.getAlbum_name().equals(album_name)){
                            a.getAlbums().remove(album);
                            saveDataArtists();
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
     * funcao que remove uma musica
     * @param message_from_rmi
     * @return
     */
    private boolean removeMusic(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_music_name = data[1].split("\\|");
        String music_name = aux_music_name[1];

        String [] aux_album_name = data[2].split("\\|");
        String album_name = aux_album_name[1];

        String [] aux_artist_name = data[3].split("\\|");
        String artist_name = aux_artist_name[1];

        if(!artists.isEmpty()) {
            for(Artist a : artists){
                if(a.getArtist_name().equals(artist_name)) {
                    for (Album cd : a.getAlbums()) {
                        if (cd.getAlbum_name().equals(album_name)) {
                            for(Music music : cd.getMusics()){
                                if(music.getMusic_name().equals(music_name)){
                                    cd.getMusics().remove(music);
                                    saveDataArtists();
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
     * funcao que remove uma critica
     * @param message_from_rmi
     * @return
     */
    private boolean removeReview(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_album_name = data[1].split("\\|");
        String album_name = aux_album_name[1];

        String [] aux_username = data[2].split("\\|");
        String username = aux_username[1];

        String [] aux_artist_name = data[3].split("\\|");
        String artist_name = aux_artist_name[1];

        if(!artists.isEmpty()) {
            for(Artist a : artists){
                if(a.getArtist_name().equals(artist_name)) {
                    for (Album cd : a.getAlbums()) {
                        if (cd.getAlbum_name().equals(album_name)) {
                            for (Review review : cd.getReviews()){
                                if(review.getUsername().equals(username)){
                                    cd.getReviews().remove(review);
                                    saveDataArtists();
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
     * funcao que remove uma playlist
     * @param message_from_rmi
     * @return
     */
    private boolean removePlaylist(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_playlist_name = data[1].split("\\|");
        String playlist_name = aux_playlist_name[1];

        String [] aux_creator = data[2].split("\\|");
        String creator = aux_creator[1];

        if(!users.isEmpty()) {
            for(User u : users){
                if(u.getUsername().equals(creator)){
                    for(Playlist playlist : u.getPlaylists()){
                        if(playlist.getName().equals(playlist_name)) {
                            u.getPlaylists().remove(playlist);
                            saveDataArtists();
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    //Search

    /**
     * funcao que pesquisa um utilizador
     * @return
     */
    private String searchUser(){
        String userList = "type|search_user;list_count|" + Integer.toString(users.size());

        int counter = 0;
        if(!users.isEmpty()) {
            for (User user : users) {
                userList += ";user_" + Integer.toString(counter) + "|" + user.getUsername();
                counter++;
            }
        }
        return userList;
    }

    /**
     * funcao que procura um artista
     * @return
     */
    private String searchArtist(){
        String artistList = "type|search_artist;list_count|" + Integer.toString(artists.size());

        int counter = 0;
        if(!artists.isEmpty()){
            for(Artist artist : artists){
                artistList += ";artist_" + Integer.toString(counter) + "|" + artist.getArtist_name();
                counter ++;
            }
        }
        return artistList;
    }

    /**
     * funcao que procura um album
     * @return
     */
    private String searchAlbum(){
        String albumList = "type|search_album" + ";list_count|";

        String aux = "";

        int counter = 0;
        if(!artists.isEmpty()) {
            for (Artist artist : artists) {
                for (Album album : artist.getAlbums()) {
                    aux += ";album_" + Integer.toString(counter) + "|" + album.getAlbum_name();
                    counter++;
                }
                return albumList + Integer.toString(counter) + aux;
            }
        }
        return albumList + Integer.toString(counter);

    }

    /**
     * funcao que procura um album atraves do nome do proprio
     * @param message_from_rmi
     * @return
     */
    private String searchAlbumByName(String message_from_rmi){
        String message_to_rmi = "type|search_album_by_name;item_count|";

        String aux = "";

        String [] data = message_from_rmi.split(";");

        String [] aux_album_name = data[1].split("\\|");
        String album_name = aux_album_name[1];

        int counter=0;
        if(!artists.isEmpty()){
            for(Artist a: artists){
                for(Album album: a.getAlbums()){
                    if(album.getAlbum_name().equals(album_name)){
                        aux +=";album_" + Integer.toString(counter) + "|" + a.getArtist_name();
                        counter ++;
                    }
                }
                return message_to_rmi + Integer.toString(counter) + aux;
            }

        }
        return message_to_rmi + Integer.toString(counter);
    }

    /**
     * funcao que procura um album atraves do artista
     * @param message_from_rmi
     * @return
     */
    private String searchAlbumByArtist(String message_from_rmi){
        String message_to_rmi = "type|search_album_by_name;item_count|";

        String aux = "";

        String [] data = message_from_rmi.split(";");

        String [] aux_artist = data[1].split("\\|");
        String artist = aux_artist[1];

        int counter = 0;
        if(!artists.isEmpty()){
            for(Artist a : artists){
                if(a.getArtist_name().equals(artist)){
                    message_to_rmi += Integer.toString(a.getAlbums().size());
                    for(Album album : a.getAlbums()){
                        aux += ";album_" + Integer.toString(counter) + "|" + album.getAlbum_name();
                        counter ++;
                    }
                    return message_to_rmi + Integer.toString(counter) + aux;
                }
            }

        }
        return message_to_rmi + Integer.toString(counter);
    }

    /**
     * funcao que procura uma musica
     * @return
     */
    private String searchMusic(){
        String albumList = "type|search_music;list_count|";

        String aux = "";

        int counter = 0;
        if(!artists.isEmpty()) {
            for (Artist artist : artists) {
                for (Album album : artist.getAlbums()) {
                    for (Music music : album.getMusics()) {
                        aux += ";music_" + Integer.toString(counter) + "|" + music.getMusic_name();
                        counter++;
                    }
                    return albumList + Integer.toString(counter) + aux;
                }
            }

        }
        return albumList + Integer.toString(counter);
    }

    /**
     * funcao que procura uma musica atraves de um album
     * @param message_from_rmi
     * @return
     */

    private String searchMusicByAlbum(String message_from_rmi){
        String albumList = "type|search_music_by_album;list_count|";

        String [] data = message_from_rmi.split(";");

        String [] aux_album_name = data[1].split("\\|");
        String album_name = aux_album_name[1];

        String [] aux_artist_name = data[2].split("\\|");
        String artist_name = aux_artist_name[1];

        String aux = "";

        int counter = 0;

        if(!artists.isEmpty()) {
            for (Artist artist : artists) {
                if (artist.getArtist_name().equals(artist_name)) {
                    for (Album album : artist.getAlbums()) {
                        if (album.getAlbum_name().equals(album_name)) {
                            for (Music music : album.getMusics()) {
                                aux += ";music_" + Integer.toString(counter) + "|" + music.getMusic_name();
                                counter++;
                            }
                            return albumList + Integer.toString(counter) + aux;
                        }
                    }
                }
            }

        }
        return albumList + Integer.toString(counter);
    }

    /**
     * funcao que procura musicas atraves do nome da playlist
     * @param message_from_rmi
     * @return
     */
    private String searchMusicByPlaylist(String message_from_rmi){
        String musicList = "type|search_user;list_count|";

        String [] data = message_from_rmi.split(";");

        String [] aux_playlist_name = data[1].split("\\|");
        String playlist_name = aux_playlist_name[1];

        String [] aux_creator = data[2].split("\\|");
        String creator = aux_creator[1];

        String aux = "";

        int counter = 0;
        if(!users.isEmpty()) {
            for (User user : users) {
                if(user.getUsername().equals(creator)){
                    for(Playlist playlist : user.getPlaylists()){
                        if(playlist.getName().equals(playlist_name)){
                            for(Music music : playlist.getMusics()){
                                aux += ";music_" + Integer.toString(counter) + "|" + music.getMusic_name();
                                counter++;
                            }
                            return musicList + Integer.toString(counter) + aux;
                        }
                    }
                }

            }

        }
        return musicList + Integer.toString(counter);
    }

    /**
     * funcao que procura playlists publicas
     * @return
     */
    private String searchPublicPlaylist(){
        String public_playlists = "type|search_public_playlist;list_count|";

        int counter = 0;
        String aux = "";

        if(!users.isEmpty()) {
            for (User user : users) {
                for(Playlist playlist : user.getPlaylists()){
                    if(!playlist.getPrivilege()){
                        aux += ";playlist_" + Integer.toString(counter) + "|" + playlist.getName();
                        counter++;
                    }
                }
            }
            return public_playlists + Integer.toString(counter) + aux;
        }

        return public_playlists + Integer.toString(counter);

    }

    /**
     * funcao que procura playlists privadas
     * @param message_from_rmi
     * @return
     */
    private String searchPrivatePlaylist(String message_from_rmi){
        String private_playlists = "type|search_private_playlist;list_count|";

        String [] data = message_from_rmi.split(";");

        String [] aux_username = data[1].split("\\|");
        String username = aux_username[1];

        int counter = 0;
        String aux = "";

        if(!users.isEmpty()) {
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    for (Playlist playlist : user.getPlaylists()) {
                        aux += ";playlist_" + Integer.toString(counter) + "|" + playlist.getName();
                        counter++;

                    }
                }
            }
            return private_playlists + Integer.toString(counter) + aux;
        }

        return private_playlists + Integer.toString(counter);

    }

    //Details

    /**
     * funcao que consulta os detalhes de um utilizador
     * @param message_from_rmi
     * @return
     */
    private String detailsUser(String message_from_rmi){
        String details_user = "type|details_user";

        String [] data = message_from_rmi.split(";");

        String [] aux_username = data[1].split("\\|");
        String username = aux_username[1];

        if(!users.isEmpty()) {
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    details_user += ";password|" + user.getPassword();
                    details_user += ";privilege|" + user.getPrivilege().toString();
                }
            }
        }

        return details_user;
    }

    /**
     * funcao que consulta os detalhes de um artista
     * @param message_from_rmi
     * @return
     */
    private String detailsArtist(String message_from_rmi){
        String details_artist = "type|details_artist";

        String [] data = message_from_rmi.split(";");

        String [] aux_artist_name = data[1].split("\\|");
        String artist_name = aux_artist_name[1];

        if(!artists.isEmpty()) {
            for (Artist artist : artists) {
                if (artist.getArtist_name().equals(artist_name)) {
                    details_artist += ";age|" + Integer.toString(artist.getAge());
                    details_artist += ";music_style|" + artist.getMusic_style();
                    details_artist += ";biography|" + artist.getBiography();
                }
            }
        }

        return details_artist;
    }

    /**
     * funcao que consulta os detalhes de um album
     * @param message_from_rmi
     * @return
     */
    private String detailsAlbum(String message_from_rmi){

        String album_details = "type|album_details";

        String [] data = message_from_rmi.split(";");

        String [] aux_album_name = data[1].split("\\|");
        String album_name = aux_album_name[1];

        String [] aux_artist_name = data[2].split("\\|");
        String artist_name = aux_artist_name[1];

        for(Artist artist : artists){
            if(artist.getArtist_name().equals(artist_name)){
                for(Album album : artist.getAlbums()){

                    if(album.getAlbum_name().equals(album_name)){

                        album_details += ";record_label|" + album.getRecord_label();
                    }
                }

            }

        }

        return album_details;
    }

    /**
     * funcao que consulta os detalhes de uma musica
     * @param message_from_rmi
     * @return
     */
    private String detailsMusic(String message_from_rmi){

        String music_details = "type|album_music";

        String [] data = message_from_rmi.split(";");

        String [] aux_music_name = data[1].split("\\|");
        String music_name = aux_music_name[1];

        String [] aux_album_name = data[2].split("\\|");
        String album_name = aux_album_name[1];

        String [] aux_artist_name = data[3].split("\\|");
        String artist_name = aux_artist_name[1];

        for(Artist artist : artists){
            if(artist.getArtist_name().equals(artist_name)){
                for(Album album : artist.getAlbums()){
                    if(album.getAlbum_name().equals(album_name)){
                        for(Music music : album.getMusics()){
                            if(music.getMusic_name().equals(music_name)){
                                music_details += ";musical_gender|" + music.getMusical_gender();
                                music_details += ";lyrics|" + music.getLyrics();
                                music_details += ";composer|" + music.getComposer();
                                music_details += ";release_date|" + music.getRealease_date();
                            }
                        }
                    }
                }

            }

        }

        return music_details;
    }

    /**
     * funcao que consulta os detalhes de uma critica
     * @param message_from_rmi
     * @return
     */
    private String detailsReview(String message_from_rmi){

        String review_details = "type|reviews_details;list_count|";

        String [] data = message_from_rmi.split(";");

        String [] aux_album_name = data[1].split("\\|");
        String album_name = aux_album_name[1];

        String [] aux_artist_name = data[2].split("\\|");
        String artist_name = aux_artist_name[1];

        int counter = 0;
        String aux ="";

        for(Artist artist : artists){
            if(artist.getArtist_name().equals(artist_name)){
                for(Album album : artist.getAlbums()){
                    if(album.getAlbum_name().equals(album_name)){
                        for(Review review : album.getReviews()){
                            aux += ";username|" + review.getUsername();
                            aux += ";description|" + review.getText();
                            aux += ";rating|" + Integer.toString(review.getRating());
                            counter ++;
                        }
                    }
                }

            }

        }
        if(counter == 0){
            return review_details + "0";
        }
        else{
            return review_details + Integer.toString(counter) + aux;
        }
    }

    /**
     * funcao que retorna os utilzadores que ja editaram um album
     * @param message_from_rmi
     * @return
     */
    private String usersOfAlbuns(String message_from_rmi){
        String [] data = message_from_rmi.split(";");

        String [] aux_album = data[1].split("\\|");
        String album = aux_album[1];

        String answer = null;
        for(Artist a: artists){
            ArrayList<Album> albuns = a.getAlbums();
            for(Album al : albuns){
                if( al.getAlbum_name().equals(album)){
                    String length = Integer.toString(al.getUsersOfAlbun().size());
                    answer="type|users_list_album;item_count|"+length;
                    for (int i=0;i<al.getUsersOfAlbun().size();i++){
                        answer+=";item_"+i+"_name|"+al.getUsersOfAlbun().get(i);
                    }
                }
            }
        }
        System.out.println(answer);
        return answer;
    }


    //File

    /**
     * funcao que carrega os dados dos utilizadores
     */
    private static void loadDataUsers(){
        //load data from file to Arraylist Users
        users = new ArrayList<>();
        try {
            FileInputStream is = new FileInputStream(f1);
            ObjectInputStream ois = new ObjectInputStream(is);
            users = (ArrayList<User>)ois.readObject();
            ois.close();

        } catch (FileNotFoundException ex) {
            users = new ArrayList<>();
        } catch (IOException ex) {
            users = new ArrayList<>();
        } catch (ClassNotFoundException ex) {
            users = new ArrayList<>();
        }
    }

    /**
     * funcao que carrega os dados dos artistas
     */
    private static void loadDataArtists(){
        //load data from file to Arraylist Users
        artists = new ArrayList<>();
        try {
            FileInputStream is = new FileInputStream(f2);
            ObjectInputStream ois = new ObjectInputStream(is);
            artists = (ArrayList<Artist>)ois.readObject();
            ois.close();

        } catch (FileNotFoundException ex) {
            artists = new ArrayList<>();
        } catch (IOException ex) {
            artists = new ArrayList<>();
        } catch (ClassNotFoundException ex) {
            artists = new ArrayList<>();
        }
    }

    /**
     * funcao que guarda os dados dos utilizadores
     */
    private static void saveDataUsers(){
        //carrega dados da arraylist para o ficheiro
        try {
            FileOutputStream os = new FileOutputStream(f1);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(users);
            oos.close();

        } catch (IOException ex) {
            Logger.getLogger(MulticastServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * funcao que guarda os dados dos artistas
     */
    private static void saveDataArtists(){
        try {
            FileOutputStream os = new FileOutputStream(f2);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(artists);
            oos.close();

            //Escreve no ficheiro
        } catch (IOException ex) {
            Logger.getLogger(MulticastServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
