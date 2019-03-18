package rmi;


import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

public class RmiClient extends UnicastRemoteObject implements ClientInterface {
    private static ServerInterface server;
    private static String my_username;
    private static ClientInterface client;
    private static int timeout= 30000;


    public RmiClient(String username) throws RemoteException {
        super();
        this.my_username = username;
    }


    /**
     * funcao que incializa a aplicação
     */
    //Main menus
    private static void welcomeMenu() {
        System.out.println("\n---------------Welcome to DropMusic-----------------");

        System.out.println("1-Login");
        System.out.println("2-Register");
        System.out.print("\nOption: ");

        Scanner keyboard = new Scanner(System.in);
        String option = keyboard.next();

        try {
            switch (Integer.parseInt(option)) {
                case 1:
                    loginMenu();
                    break;
                case 2:
                    registerMenu();
                    break;
                default:
                    System.out.println("\n-----------------");
                    System.out.println("Option is invalid");
                    System.out.println("-----------------");
                    welcomeMenu();
            }

        } catch (NumberFormatException ne) {
            System.out.println("\n-----------------");
            System.out.println("Option is invalid");
            System.out.println("-----------------");
            welcomeMenu();
        }
    }

    /**
     * funcao que permite ao utilizador inserir as suas credencias
     */
    private static void loginMenu() {
        System.out.println("\n----------------------Login-------------------------");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("\nUsername: ");
        String username = keyboard.next();
        my_username = username;

        System.out.print("\nPassword: ");
        String password = keyboard.next();

        boolean check_login = false;
        try {
            client = new RmiClient(username);
            check_login = server.checkLogin(username, password);

        } catch (RemoteException e) {
            check_login= catchRemoteExceptionLogin(username,password);
        }

        if (check_login) {
            System.out.println("\n------------------------------");
            System.out.println("Logged in successfully.");
            System.out.println("------------------------------");
            boolean check_privilege = false;
            try {
                check_privilege = server.checkUserPrivilege(username);
            } catch (RemoteException e) {
                check_privilege=catchRemoteExceptionCheckPrivilege(username);
            }


            if (check_privilege) {
                editorMenu();
            } else {
                regularMenu();
            }


        } else {
            System.out.println("\n------------------------------");
            System.out.println("Username or Password is incorrect.");
            System.out.println("Please try again.");
            System.out.println("------------------------------");
            loginMenu();
        }


    }

    /**
     * funcao que permite ao utilizador registar-se
     */
    private static void registerMenu() {
        System.out.println("\n-------------------Register User--------------------");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Username: ");
        String username = keyboard.next();
        boolean check_username = false;
        try {
            check_username = server.checkUser(username);
        } catch (RemoteException e) {
            check_username=catchRemoteExceptionCheckUser(username);
        }

        if (check_username) {
            System.out.println("\n------------------------------");
            System.out.println("Username already in use. Try again");
            System.out.println("------------------------------");
            registerMenu();
        }

        System.out.print("\nPassword: ");
        String password = keyboard.next();

        boolean check_register = false;
        try {
            client = new RmiClient(username);
            check_register = server.insertUser(username, password);

        } catch (RemoteException e) {
            check_register=catchRemoteExceptionInsertUser(username,password);
        }

        if (check_register) {

            my_username = username;
            System.out.println("\n------------------------------");
            System.out.println("New user created successfully.");
            System.out.println("------------------------------");

            try{
                boolean privilege = server.checkUserPrivilege(username);
                if(privilege){
                    editorMenu();
                }
                else{
                    regularMenu();
                }
            } catch (RemoteException e) {
                catchRemoteExceptionCheckPrivilege(username);
            }
        }else {
            System.out.println("\n------------------------------");
            System.out.println("Error while creating new user.");
            System.out.println("Please try again.");
            System.out.println("------------------------------");
            registerMenu();
        }


    }

    /**
     * Menu de utilizador sem privilegios
     */
    //Regular menus
    private static void regularMenu() {
        System.out.println("\n--------------------Regular Menu----------------------");
        System.out.println("1-User");
        System.out.println("2-Artist");
        System.out.println("3-Album");
        System.out.println("4-Music");
        System.out.println("5-Exit");
        System.out.print("\nOption: ");

        Scanner keyboard = new Scanner(System.in);
        String option = keyboard.next();

        try {
            switch (Integer.parseInt(option)) {
                case 1:
                    regularUserMenu();
                    break;
                case 2:
                    regularArtistMenu();
                    break;
                case 3:
                    regularAlbumMenu();
                    break;
                case 4:
                    regularMusicMenu();
                    break;
                case 5:
                    server.deleteClient(client);
                    System.exit(0);
                    break;
                default:
                    System.out.println("\n-----------------");
                    System.out.println("Option is invalid");
                    System.out.println("-----------------");
                    regularMenu();
            }

        } catch (NumberFormatException ne) {
            System.out.println("\n-----------------");
            System.out.println("Option is invalid");
            System.out.println("-----------------");
            regularMenu();
        } catch (RemoteException e) {
            catchRemoteExceptionDeletetClient(client);
        }
    }

    /**
     * submenu para fazer operacoes na sua conta
     */
    private static void regularUserMenu() {
        System.out.println("\n--------------------Regular Menu----------------------");
        System.out.println("1-Edit account ");
        System.out.println("2-Delete account");
        System.out.println("3-Playlists");
        System.out.println("4-Back");
        System.out.print("\nOption: ");

        Scanner keyboard = new Scanner(System.in);
        String option = keyboard.next();

        try {
            switch (Integer.parseInt(option)) {
                case 1:
                    regularUserEditAccount();
                    regularUserMenu();
                    break;
                case 2:
                    regularUserRemoveAccount();
                    regularUserMenu();
                    break;
                case 3:
                    regularUserPlaylists();
                    regularUserMenu();
                    break;
                case 4:
                    regularMenu();
                    break;
                default:
                    System.out.println("\n-----------------");
                    System.out.println("Option is invalid");
                    System.out.println("-----------------");
                    regularMenu();
            }

        } catch (NumberFormatException ne) {
            System.out.println("\n-----------------");
            System.out.println("Option is invalid");
            System.out.println("-----------------");
            regularMenu();
        }
    }

    /**
     * funcao que permite ao utilizador alterar a sua conta
     */
    private static void regularUserEditAccount() {
        System.out.println("\n--------------------Edit User Account----------------------");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("\nNew username: ");
        String new_username = keyboard.nextLine();
        boolean check_username = false;
        try {
            check_username = server.checkUser(new_username);

        } catch (RemoteException e) {
            check_username=catchRemoteExceptionCheckUser(new_username);
        }

        if (check_username) {
            System.out.println("\n------------------------------");
            System.out.println("Username already in use. Try again");
            System.out.println("------------------------------");
            regularUserEditAccount();
        }

        System.out.print("\nNew password: ");
        String new_password = keyboard.nextLine();
        boolean check_edit_user = false;
        try {
            check_edit_user = server.editUser(my_username, new_username, new_password, "regular");

        } catch (RemoteException e) {
            check_edit_user= catchRemoteExceptionEditUser(my_username,new_username,new_password,"regular");
        }

        if (check_edit_user) {
            System.out.println("\n------------------------------");
            System.out.println("User edited successfully.");
            System.out.println("------------------------------");
        } else {
            System.out.println("\n------------------------------");
            System.out.println("Error while editing user.");
            System.out.println("Please try again.");
            System.out.println("------------------------------");
            regularUserEditAccount();
        }

    }

    /**
     * funcao que permite ao utilizador remover a sua conta
     */
    private static void regularUserRemoveAccount() {
        System.out.println("\n--------------------Remove account----------------------");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Are you sure, you want to delete your account?[yes|no]");
        String option = keyboard.nextLine();

        if (option.equals("yes")) {
            boolean remove_user = false;
            try {
                remove_user = server.removeUser(my_username);

            } catch (RemoteException e) {
                remove_user = catchRemoteExceptionRemoveUser(my_username);
            }

            if (remove_user) {
                System.out.println("\n------------------------------------");
                System.out.println("Your account was successfully removed.");
                System.out.println("--------------------------------------");
                try {
                    server.deleteClient(client);
                } catch (RemoteException e) {
                    catchRemoteExceptionDeletetClient(client);
                }
                System.exit(0);
            } else {
                System.out.println("\n------------------------------");
                System.out.println("Error while removing your account.");
                System.out.println("Please try again.");
                System.out.println("------------------------------");
            }
        }

    }

    /**
     * funcao que permite ao utilizador fazer operacoes quando às playlists
     */
    private static void regularUserPlaylists() {
        System.out.println("\n--------------------Regular Playlists Menu----------------------");
        System.out.println("1-Create playlist");
        System.out.println("2-My playlists");
        System.out.println("3-Edit my playlists");
        System.out.println("4-Remove playlist");
        System.out.println("5-Public playlists");
        System.out.println("6-Add music to playlist");
        System.out.println("7-Back");
        System.out.print("\nOption: ");

        Scanner keyboard = new Scanner(System.in);
        String option = keyboard.next();

        try {
            switch (Integer.parseInt(option)) {
                case 1:
                    regularCreatPlaylist();
                    regularUserPlaylists();
                    break;
                case 2:
                    regularMyPlaylists();
                    regularUserPlaylists();
                    break;
                case 3:
                    regularEditMyPlaylists();
                    regularUserPlaylists();
                    break;
                case 4:
                    regularRemoveMyPlaylist();
                    regularUserPlaylists();
                    break;
                case 5:
                    publicPlaylist();
                    regularUserPlaylists();
                    break;
                case 6:
                    addMusicToPlaylist();
                    regularUserPlaylists();
                    break;
                case 7:
                    regularMenu();
                    break;
                default:
                    System.out.println("\n-----------------");
                    System.out.println("Option is invalid");
                    System.out.println("-----------------");
                    regularUserPlaylists();
            }

        } catch (NumberFormatException ne) {
            System.out.println("\n-----------------");
            System.out.println("Option is invalid");
            System.out.println("-----------------");
            regularUserPlaylists();
        }


    }

    /**
     * funcao que permite ao utilizador adicionar musicas à playlist
     */
    private static void addMusicToPlaylist() {
        System.out.println("\n--------------------Add music to playlist----------------------");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("\nAlbum name: ");
        String album = keyboard.nextLine();
        boolean check_album = false;
        try {
            check_album = server.checkAlbum(album);
        } catch (RemoteException e) {
            check_album=  catchRemoteExceptionCheckAlbum(album);
        }
        if (check_album) {
            System.out.print("\nArtist name: ");
            String artist = keyboard.nextLine();
            boolean check_artist = false;
            try {
                check_artist = server.checkArtist(artist);

            } catch (RemoteException e) {
                catchRemoteExceptionCheckArtist(artist);
            }

            if (check_artist) {
                System.out.print("\nMusic name: ");
                String music = keyboard.nextLine();
                boolean check_music = false;
                try {
                    check_music = server.checkMusic(music);

                } catch (RemoteException e) {
                    check_music=catchRemoteExceptionCheckMusic(music);
                }

                if (check_music) {
                    System.out.print("\nplaylist name: ");
                    String playlist = keyboard.nextLine();
                    boolean check_playlist = false;
                    try {
                        check_playlist = server.checkPlaylist(playlist);

                    } catch (RemoteException e) {
                        check_playlist = catchRemoteExceptionCheckPlayList(playlist);
                    }

                    if (check_playlist) {
                        boolean check_add = false;
                        try {
                            check_add = server.insertMusicPlaylist(playlist, music, album, artist, my_username);

                        } catch (RemoteException e) {
                            check_add = catchRemoteExceptionInsertMusicPlayList(playlist,music,album,artist,my_username);
                        }
                        if (check_add) {
                            System.out.println("Add success");
                        } else {
                            System.out.println("Error add");
                        }
                    } else {
                        System.out.println("-------------------------------");
                        System.out.println("playlist name invalid.Try again");
                        System.out.println("-------------------------------");
                        addMusicToPlaylist();
                    }
                } else {
                    System.out.println("-------------------------------");
                    System.out.println("Music name invalid.Try again");
                    System.out.println("-------------------------------");
                    addMusicToPlaylist();
                }

            } else {
                System.out.println("-------------------------------");
                System.out.println("Artist name invalid.Try again");
                System.out.println("-------------------------------");
                addMusicToPlaylist();
            }
        } else {
            System.out.println("-------------------------------");
            System.out.println("Artist name invalid.Try again");
            System.out.println("-------------------------------");
            addMusicToPlaylist();
        }


    }

    /**
     * funcao que permite mostrar ao utilizador as suas playlists
     */
    private static void regularMyPlaylists() {
        System.out.println("\n--------------------My Playlists----------------------");
        ArrayList<String> my_playlists = null;
        try {
            my_playlists = server.searchPrivatePlaylist(my_username);

        } catch (RemoteException e) {
            my_playlists= catchRemoteExceptionsearchPrivatePlayList(my_username);
        }

        if (my_playlists.isEmpty()) {
            System.out.println("Your playlists is empty");
        }

        for (String playlist_name : my_playlists) {
            System.out.println("-" + playlist_name);
        }

    }

    /**
     * funcao que permite criar playlists
     */
    private static void regularCreatPlaylist() {
        System.out.println("\n--------------------Create Playlist----------------------");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("\nPlaylist name: ");
        String plalist_name = keyboard.nextLine();

        System.out.print("\nPlaylist privilege:[public|private]");
        String plalist_privilege = keyboard.nextLine();

        if (plalist_privilege.equals("public") || plalist_privilege.equals("private")) {
            boolean check_playlist = false;

            try {
                check_playlist = server.insertPlaylist(plalist_name, plalist_privilege, my_username);

            } catch (RemoteException e) {
                check_playlist=catchRemoteExceptionInsertPlayList(plalist_name,plalist_privilege,my_username);

            }
            if (check_playlist) {
                System.out.println("\n------------------------------");
                System.out.println("Playlist Sucessfullly created.");
                System.out.println("------------------------------");
            } else {
                System.out.println("\n------------------------------");
                System.out.println("Error while creating playlist. Try again");
                System.out.println("------------------------------");
                regularCreatPlaylist();
            }
        } else {
            System.out.println("\n------------------------------");
            System.out.println("Insert a valid privilege. Try again");
            System.out.println("------------------------------");
            regularCreatPlaylist();
        }


    }

    /**
     * funcao que permite editar playlists
     */
    private static void regularEditMyPlaylists() {
        System.out.println("\n--------------------Edit Playlist----------------------");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("\nPlaylist name: ");
        String plalist_name = keyboard.nextLine();
        boolean check_new_playlist = false;
        try {
            check_new_playlist = server.checkUserPlaylist(plalist_name, my_username);

        } catch (RemoteException e) {
            check_new_playlist=catchRemoteExceptionCheckUserPlayList(plalist_name,my_username);
        }


        if (check_new_playlist) {

            System.out.print("\nNew playlist name: ");
            String new_plalist_name = keyboard.nextLine();

            System.out.print("\nPlaylist privilege:[public|private]");
            String plalist_privilege = keyboard.nextLine();


            if (plalist_privilege.equals("public") || plalist_privilege.equals("private")) {
                boolean check_playlist = false;
                try {
                    check_playlist = server.editPlaylist(plalist_name, new_plalist_name, plalist_privilege,my_username);

                } catch (RemoteException e) {
                    check_playlist = catchRemoteExceptionEditPlayList(plalist_name,new_plalist_name,plalist_privilege,my_username);
                }
                if (check_playlist) {
                    System.out.println("\n------------------------------");
                    System.out.println("Playlist Sucessfullly edited.");
                    System.out.println("------------------------------");
                } else {
                    System.out.println("\n------------------------------");
                    System.out.println("Error while editing playlist. Try again");
                    System.out.println("------------------------------");
                    regularEditMyPlaylists();
                }
            } else {
                System.out.println("\n------------------------------");
                System.out.println("Insert a valid privilege. Try again");
                System.out.println("------------------------------");
                regularEditMyPlaylists();
            }

        } else {
            System.out.println("\n------------------------------");
            System.out.println("Playlist name invalid. Pls try again");
            System.out.println("------------------------------");
            regularEditMyPlaylists();
        }
    }

    /**
     * funcao que permite remover playlists
     */
    private static void regularRemoveMyPlaylist() {
        System.out.println("\n--------------------Remove Playlist----------------------");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("\nPlaylist name: ");
        String plalist_name = keyboard.nextLine();
        boolean check_new_playlist=false;
        try {
            check_new_playlist = server.checkUserPlaylist(plalist_name, my_username);

        } catch (RemoteException e) {
            check_new_playlist = catchRemoteExceptionCheckUserPlayList(plalist_name,my_username);
        }

        if (check_new_playlist) {
            boolean remove_playlist;
            try {
                remove_playlist = server.removePlaylist(plalist_name, my_username);

            } catch (RemoteException e) {
                remove_playlist = catchRemoteExceptionRemovePlayList(plalist_name,my_username);
            }
            if (remove_playlist) {
                System.out.println("\n------------------------------");
                System.out.println("Playlist Sucessfullly removed.");
                System.out.println("------------------------------");
            } else {
                System.out.println("\n------------------------------");
                System.out.println("Error while removing playlist. Try again.");
                System.out.println("------------------------------");
                regularRemoveMyPlaylist();
            }
        } else {
            System.out.println("\n------------------------------");
            System.out.println("Plalist name its not valid");
            System.out.println("------------------------------");
            regularRemoveMyPlaylist();
        }

    }

    /**
     * funcao que imprime playlists publicas
     */
    private static void publicPlaylist() {
        System.out.println("\n--------------------Public Playlists----------------------");
        ArrayList<String> public_playlists = null;
        try {
            public_playlists = server.searchPublicPlaylist();

        } catch (RemoteException e) {
            public_playlists = catchRemoteExceptionsearchPublicPlayList();
        }

        if (public_playlists.isEmpty()) {
            System.out.println("Your playlists is empty");
        }

        for (String playlist_name : public_playlists) {
            System.out.println("-" + playlist_name);
        }

    }

    /**
     * menu de listar artistas
     */
    private static void regularArtistMenu() {
        System.out.println("\n--------------------Artist Menu----------------------");
        System.out.println("1-Artist list");
        System.out.println("2-Details artist");
        System.out.println("3-Back");
        System.out.print("\nOption: ");

        Scanner keyboard = new Scanner(System.in);
        String option = keyboard.next();

        try {
            switch (Integer.parseInt(option)) {
                case 1:
                    artistList();
                    regularArtistMenu();
                    break;
                case 2:
                    artistDetails();
                    regularArtistMenu();
                    break;
                case 3:
                    regularUserMenu();
                    break;
                default:
                    System.out.println("\n-----------------");
                    System.out.println("Option is invalid");
                    System.out.println("-----------------");
                    regularMenu();
            }

        } catch (NumberFormatException ne) {
            System.out.println("\n-----------------");
            System.out.println("Option is invalid");
            System.out.println("-----------------");
            regularMenu();
        }

    }

    /**
     * funcao que lista os artistas
     */
    private static void artistList() {
        System.out.println("\n--------------------Artist List----------------------");
        ArrayList<String> artists = null;
        try {
            artists = server.searchArtist();

        } catch (RemoteException e) {
            artists = catchRemoteExceptionsearchArtist();
        }
        if (artists.isEmpty()) {
            System.out.println("artist list is empty");
        }

        for (String artist_name : artists) {
            System.out.println("-" + artist_name);
        }
    }

    /**
     * funcao que mostra os detalhes de um artista
     */
    private static void artistDetails() {
        System.out.println("\n--------------------Artist details----------------------");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Insert artist name: ");
        String artist_name = keyboard.nextLine();
        boolean check_artist = false;
        try {
            check_artist = server.checkArtist(artist_name);

        } catch (RemoteException e) {
            check_artist=catchRemoteExceptionCheckArtist(artist_name);
        }

        if (check_artist) {
            ArrayList<String> artist_details = null;
            try {
                artist_details = server.detailsArtist(artist_name);

            } catch (RemoteException e) {
                artist_details=catchRemoteExceptionDetailsArtist(artist_name);
            }

            System.out.println("Artist name: " + artist_name);
            System.out.println("Artist age: " + artist_details.get(0));
            System.out.println("Artist music style: " + artist_details.get(1));
            System.out.println("Artist biography: " + artist_details.get(2));

            ArrayList<String> artist_albums = null;
            try {
                artist_albums = server.searchAlbumByArtist(artist_name);
            } catch (RemoteException e) {
                artist_albums = catchRemoteExceptionsearchAlbumByArtist(artist_name);
            }

            if (artist_albums.isEmpty()) {
                System.out.println("Artist discography: empty");
            } else {
                System.out.println("Artist discography:");
                for (String album_name : artist_albums) {
                    System.out.println("- " + album_name);
                }
            }

        } else {
            System.out.println("\n-----------------");
            System.out.println("Artist name is incorrect. Try again");
            System.out.println("-----------------");
            artistDetails();
        }
    }

    /**
     * menu que permite obter dados sobre os albuns
     */
    private static void regularAlbumMenu() {
        System.out.println("\n--------------------album Menu----------------------");
        System.out.println("1-Album list");
        System.out.println("2-Details album");
        System.out.println("3-Album reviews");
        System.out.println("4-Back");
        System.out.print("\nOption: ");

        Scanner keyboard = new Scanner(System.in);
        String option = keyboard.next();

        try {
            switch (Integer.parseInt(option)) {
                case 1:
                    albumList();
                    regularAlbumMenu();
                    break;
                case 2:
                    albumDetails();
                    regularAlbumMenu();
                    break;
                case 3:
                    albumReviews();
                    regularAlbumMenu();
                    break;
                case 4:
                    regularUserMenu();
                    break;
                default:
                    System.out.println("\n-----------------");
                    System.out.println("Option is invalid");
                    System.out.println("-----------------");
                    regularMenu();
            }

        } catch (NumberFormatException ne) {
            System.out.println("\n-----------------");
            System.out.println("Option is invalid");
            System.out.println("-----------------");
            regularMenu();
        }

    }

    /**
     * funcao que permite imprimir a lista de albuns
     */
    private static void albumList() {
        System.out.println("\n--------------------Album List----------------------");
        ArrayList<String> albums = null;
        try {
            albums = server.searchAlbum();
        } catch (RemoteException e) {
            albums= catchRemoteExceptionsearchAlbuns();
        }
        if (albums.isEmpty()) {
            System.out.println("Album list is empty");
        }

        for (String album_name : albums) {
            System.out.println("-" + album_name);
        }
    }

    /**
     * funcao que permite consultar os detalhes de um album
     */
    private static void albumDetails() {
        System.out.println("\n--------------------Album details----------------------");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Insert album name: ");
        String album_name = keyboard.nextLine();

        System.out.print("Insert artist name: ");
        String artist_name = keyboard.nextLine();
        boolean check_album = false;
        try {
            check_album = server.checkAlbum(album_name);

        } catch (RemoteException e) {
            check_album=catchRemoteExceptionCheckAlbum(album_name);
        }
        if (check_album) {
            boolean check_artist = false;
            try {
                check_artist = server.checkArtist(artist_name);
            } catch (RemoteException e) {
                check_artist=catchRemoteExceptionCheckArtist(artist_name);
            }
            if (check_artist) {
                ArrayList<String> album_details = null;
                try {
                    album_details = server.detailsAlbum(album_name, artist_name);
                } catch (RemoteException e) {
                    album_details=catchRemoteExceptionDetailsAlbum(album_name,artist_name);
                }
                System.out.println("album name: " + album_name);
                System.out.println("album artist: " + artist_name);
                assert album_details != null;
                System.out.println("album record_label: " + album_details.get(0));
                ArrayList<String> album_musics = null;
                try {
                    album_musics = server.searchMusicByAlbum(album_name, artist_name);

                } catch (RemoteException e) {
                    album_musics=catchRemoteExceptionsearchMusicByAlbum(album_name,artist_name);
                }


                System.out.println("album discography:");

                for (String music_name : album_musics) {
                    System.out.println("- " + music_name);
                }
                    ArrayList<String> album_reviews = null;
                    try {
                        album_reviews = server.detailsReview(album_name, artist_name);
                    } catch (RemoteException e) {
                        album_reviews = catchRemoteExceptionDetailsReview(album_name,artist_name);
                    }
                        System.out.println("album reviews: empty");
                for (int i = 0; i < album_reviews.size(); i += 3) {
                    System.out.println("username:" + album_reviews.get(i));
                    System.out.println("description:" + album_reviews.get(i + 1));
                    System.out.println("rating:" + album_reviews.get(i + 2));
                    }


            } else {
                System.out.println("\n-----------------");
                System.out.println("artist name is incorrect. Try again");
                System.out.println("-----------------");
                albumDetails();
            }

        } else {
            System.out.println("\n-----------------");
            System.out.println("album name is incorrect. Try again");
            System.out.println("-----------------");
            albumDetails();
        }
    }

    /**
     * menu de criticas de um album
     */
    private static void albumReviews() {
        System.out.println("\n--------------------Reviews Menu----------------------");
        System.out.println("1-Insert reviews");
        System.out.println("2-Edit reviews");
        System.out.println("3-Remove reviews");
        System.out.println("4-Back");
        System.out.print("\nOption: ");

        Scanner keyboard = new Scanner(System.in);
        String option = keyboard.next();

        try {
            switch (Integer.parseInt(option)) {
                case 1:
                    insertReview();
                    regularAlbumMenu();
                    break;
                case 2:
                    editReview();
                    regularAlbumMenu();
                    break;
                case 3:
                    removeReview();
                    regularAlbumMenu();
                    break;
                case 4:
                    regularUserMenu();
                    break;
                default:
                    System.out.println("\n-----------------");
                    System.out.println("Option is invalid");
                    System.out.println("-----------------");
                    regularMenu();
            }

        } catch (NumberFormatException ne) {
            System.out.println("\n-----------------");
            System.out.println("Option is invalid");
            System.out.println("-----------------");
            regularMenu();
        }

    }

    /**
     * funcao que insere criticas a um album
     */
    private static void insertReview() {
        System.out.println("\n--------------------Insert review----------------------");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Album name: ");
        String album = keyboard.nextLine();
        boolean check_album = false;
        try {
            check_album = server.checkAlbum(album);

        } catch (RemoteException e) {
            check_album=catchRemoteExceptionCheckAlbum(album);
        }
        if (check_album) {
            System.out.println("Album exist");
        } else {
            System.out.println("-------------------------------");
            System.out.println("Artist name invalid.Try again");
            System.out.println("-------------------------------");
            insertReview();
        }


        System.out.print("Artist name: ");
        String artist = keyboard.nextLine();
        boolean check_artist = false;

        try {
            check_artist = server.checkArtist(artist);

        } catch (RemoteException e) {
            check_artist=catchRemoteExceptionCheckArtist(artist);
        }
        if (check_artist) {
            System.out.println("Artist exist");
        } else {
            System.out.println("-------------------------------");
            System.out.println("Artist name invalid.Try again");
            System.out.println("-------------------------------");
            insertReview();
        }


        System.out.print("Description: ");
        String description = keyboard.nextLine();

        System.out.print("Rating: ");
        String rating = keyboard.nextLine();
        boolean check_review = false;
        try {
            check_review = server.insertReview(my_username, description, rating, album, artist);

        } catch (RemoteException e) {
            check_review=catchRemoteExceptionInsertReview(my_username,description,rating,album,artist);
        }
        if (check_review) {
            System.out.println("-------------------------------");
            System.out.println("review insrted sucesfully");
            System.out.println("-------------------------------");
        } else {
            System.out.println("-------------------------------");
            System.out.println("error insert review");
            System.out.println("-------------------------------");
        }


    }

    /**
     * funcao que altera uma critica
     */
    private static void editReview() {
        System.out.println("\n--------------------edit review----------------------");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Album name: ");
        String album = keyboard.next();
        boolean check_album = false;
        try {
            check_album = server.checkAlbum(album);

        } catch (RemoteException e) {
            check_album = catchRemoteExceptionCheckAlbum(album);
        }
        if (check_album) {
            System.out.println("Album exist");
        } else {
            System.out.println("-------------------------------");
            System.out.println("Artist name invalid.Try again");
            System.out.println("-------------------------------");
            editReview();
        }


        System.out.print("Artist name: ");
        String artist = keyboard.next();
        boolean check_artist = false;
        try {
            check_artist = server.checkArtist(artist);
        } catch (RemoteException e) {
            check_artist= catchRemoteExceptionCheckArtist(artist);
        }
        if (check_artist) {
            System.out.println("Artist exist");
        } else {
            System.out.println("-------------------------------");
            System.out.println("Artist name invalid.Try again");
            System.out.println("-------------------------------");
            insertReview();
        }


        System.out.print("Description: ");
        String description = keyboard.next();

        System.out.print("Rating: ");
        String rating = keyboard.next();
        boolean check_review = false;
        try {
            check_review = server.editReview(my_username, my_username, description, rating, album, artist);

        } catch (RemoteException e) {
            check_review = catchRemoteExceptionEditReview(my_username,my_username,description,rating,album,artist);
        }
        if (check_review) {
            System.out.println("-------------------------------");
            System.out.println("Review edit sucesfully");
            System.out.println("-------------------------------");
        } else {
            System.out.println("-------------------------------");
            System.out.println("Error edit review");
            System.out.println("-------------------------------");
        }


    }

    /**
     * funcao que remove uma critica
     */

    private static void removeReview() {
        System.out.println("\n--------------------Remove review----------------------");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Album name: ");
        String album = keyboard.next();
        boolean check_album = false;
        try {
            check_album = server.checkAlbum(album);
        } catch (RemoteException e) {
            check_album= catchRemoteExceptionCheckAlbum(album);
        }
        if (check_album) {
            System.out.println("Album exist");
        } else {
            System.out.println("-------------------------------");
            System.out.println("Artist name invalid.Try again");
            System.out.println("-------------------------------");
            removeReview();
        }


        System.out.print("Artist name: ");
        String artist = keyboard.next();
        boolean check_artist = false;
        try {
            check_artist = server.checkArtist(artist);

        } catch (RemoteException e) {
            check_artist=catchRemoteExceptionCheckArtist(artist);
        }
        if (check_artist) {
            System.out.println("Artist exist");
        } else {
            System.out.println("-------------------------------");
            System.out.println("Artist name invalid.Try again");
            System.out.println("-------------------------------");
            removeReview();
        }
        boolean check_review = false;
        try {
            check_review = server.removeReview(album, my_username, artist);
        } catch (RemoteException e) {
            check_review=catchRemoteExceptionRemoveReview(album,my_username,artist);
        }
        if (check_review) {
            System.out.println("-------------------------------");
            System.out.println("Review removed sucesfully");
            System.out.println("-------------------------------");
        } else {
            System.out.println("-------------------------------");
            System.out.println("Error removed review");
            System.out.println("-------------------------------");
        }


    }

    /**
     * menu de musicas que permite ao utilizador consultar musicas
     */
    private static void regularMusicMenu() {
        System.out.println("\n--------------------music Menu----------------------");
        System.out.println("1-Music list");
        System.out.println("2-Details music");
        System.out.println("3-Download");
        System.out.println("4-Upload");
        System.out.println("5-Back");
        System.out.print("\nOption: ");

        Scanner keyboard = new Scanner(System.in);
        String option = keyboard.next();

        try {
            switch (Integer.parseInt(option)) {
                case 1:
                    musicList();
                    regularMusicMenu();
                    break;
                case 2:
                    musicDetails();
                    regularMusicMenu();
                    break;
                case 3:
                    downloadMusic();
                    regularMusicMenu();
                    break;
                case 4:
                    uploadMusic();
                    regularMusicMenu();
                    break;
                case 5:
                    regularUserMenu();
                    break;
                default:
                    System.out.println("\n-----------------");
                    System.out.println("Option is invalid");
                    System.out.println("-----------------");
                    regularMenu();
            }

        } catch (NumberFormatException ne) {
            System.out.println("\n-----------------");
            System.out.println("Option is invalid");
            System.out.println("-----------------");
            regularMenu();
        }

    }

    /**
     * funcao que permite imprimir a lista de musicas
     */
    private static void musicList() {
        System.out.println("\n--------------------Music List----------------------");
        ArrayList<String> musics = null;
        try {
            musics = server.searchMusic();

        } catch (RemoteException e) {
            musics = catchRemoteExceptionsearchMusics();
        }
        assert musics != null;
        if (musics.isEmpty()) {
            System.out.println("music list is empty");
        }

        for (String music_name : musics) {
            System.out.println("-" + music_name);
        }
    }

    /**
     * funcao que mostra os detalhes de uma musica
     */
    private static void musicDetails() {
        System.out.println("\n--------------------Music details----------------------");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Insert music name: ");
        String music_name = keyboard.nextLine();

        System.out.print("Insert album name: ");
        String album_name = keyboard.nextLine();

        System.out.print("Insert artist name: ");
        String artist_name = keyboard.nextLine();
        boolean check_album = false;
        try {
            check_album = server.checkAlbum(album_name);


        } catch (RemoteException e) {
            check_album=catchRemoteExceptionCheckAlbum(album_name);
        }
        if (check_album) {
            boolean check_artist = false;
            try {
                check_artist = server.checkArtist(artist_name);

            } catch (RemoteException e) {
                check_album=catchRemoteExceptionCheckArtist(artist_name);
            }
            if (check_artist) {
                boolean check_music = false;
                try {
                    check_music = server.checkMusic(music_name);


                } catch (RemoteException e) {
                    check_music=catchRemoteExceptionCheckMusic(music_name);
                }
                if (check_music) {
                    ArrayList<String> music_details = null;
                    try {
                        music_details = server.detailsMusic(music_name, album_name, artist_name);

                    } catch (RemoteException e) {
                        music_details=catchRemoteExceptionDetailsMusic(music_name,album_name,artist_name);
                    }
                    System.out.println("Music name: " + music_name);
                    System.out.println("Music gender: " + music_details.get(0));
                    System.out.println("Music lyrics: " + music_details.get(1));
                    System.out.println("Music composer: " + music_details.get(2));
                    System.out.println("Music release date: " + music_details.get(3));


                } else {
                    System.out.println("\n-----------------");
                    System.out.println("music name is incorrect. Try again");
                    System.out.println("-----------------");
                    albumDetails();
                }
            } else {
                System.out.println("\n-----------------");
                System.out.println("artist name is incorrect. Try again");
                System.out.println("-----------------");
                albumDetails();
            }

        } else {
            System.out.println("\n-----------------");
            System.out.println("album name is incorrect. Try again");
            System.out.println("-----------------");
            albumDetails();
        }
    }


    /**
     * menu de editor
     */
    //Editor menus
    private static void editorMenu() {
        System.out.println("\n--------------------editor Menu----------------------");
        System.out.println("1-User");
        System.out.println("2-Artist");
        System.out.println("3-Album");
        System.out.println("4-Music");
        System.out.println("5-Exit");
        System.out.print("\nOption: ");

        Scanner keyboard = new Scanner(System.in);
        String option = keyboard.next();

        try {
            switch (Integer.parseInt(option)) {
                case 1:
                    editorUserMenu();
                    break;
                case 2:
                    editorArtistMenu();
                    break;
                case 3:
                    editorAlbumMenu();
                    break;
                case 4:
                    editorMusicMenu();
                    break;
                case 5:
                    server.deleteClient(client);
                    System.exit(0);
                    break;
                default:
                    System.out.println("\n-----------------");
                    System.out.println("Option is invalid");
                    System.out.println("-----------------");
                    editorMenu();
            }

        } catch (NumberFormatException ne) {
            System.out.println("\n-----------------");
            System.out.println("Option is invalid");
            System.out.println("-----------------");
            editorMenu();
        } catch (RemoteException e) {
            catchRemoteExceptionDeletetClient(client);
        }
    }

    /**
     * permite ao editor operar sobre a sua conta ou sobre a conta de outros
     */
    private static void editorUserMenu() {
        System.out.println("\n--------------------Regular Menu----------------------");
        System.out.println("1-Edit account ");
        System.out.println("2-Delete account");
        System.out.println("3-Playlists");
        System.out.println("4-Grant privilege");
        System.out.println("5-Back");
        System.out.print("\nOption: ");

        Scanner keyboard = new Scanner(System.in);
        String option = keyboard.next();

        try {
            switch (Integer.parseInt(option)) {
                case 1:
                    regularUserEditAccount();
                    editorUserMenu();
                    break;
                case 2:
                    regularUserRemoveAccount();
                    editorUserMenu();
                    break;
                case 3:
                    regularUserPlaylists();
                    editorUserMenu();
                    break;
                case 4:
                    grantPrivilege();
                    editorUserMenu();
                    break;
                case 5:
                    editorMenu();
                    break;
                default:
                    System.out.println("\n-----------------");
                    System.out.println("Option is invalid");
                    System.out.println("-----------------");
                    regularMenu();
            }

        } catch (NumberFormatException ne) {
            System.out.println("\n-----------------");
            System.out.println("Option is invalid");
            System.out.println("-----------------");
            regularMenu();
        }
    }

    /**
     * funcao que permite dar privilegios a outro utilizador
     */
    private static void grantPrivilege() {
        System.out.println("\n--------------------Grant privilege----------------------");
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Username:");
        String username = keyboard.next();
        boolean check_username = false;
        try {
            check_username = server.checkUser(username);

        } catch (RemoteException e) {
            check_username= catchRemoteExceptionCheckUser(username);
        }
        if (check_username) {
            System.out.println("User exist");
        } else {
            System.out.println("username invalid.Try again");
            grantPrivilege();
        }

        boolean check_privilege = false;
        try {
            check_privilege = server.insertUserPrivilege(username);

        } catch (RemoteException | InterruptedException e) {
            check_privilege=catchRemoteExceptionInsertUserPrivilege(username);
        }
        if (check_privilege) {
            System.out.println("Privilege change ok");
        } else {
            System.out.println("Error while changing privilege");
        }
    }

    /**
     * menu que permite operar sobre os artistas
     */
    private static void editorArtistMenu() {
        System.out.println("\n--------------------Artist Menu----------------------");
        System.out.println("1-Artist list");
        System.out.println("2-Details artist");
        System.out.println("3-Insert artist");
        System.out.println("4-Edit artist");
        System.out.println("5-Remove artist");
        System.out.println("6-Back");
        System.out.print("\nOption: ");

        Scanner keyboard = new Scanner(System.in);
        String option = keyboard.next();

        try {
            switch (Integer.parseInt(option)) {
                case 1:
                    artistList();
                    editorArtistMenu();
                    break;
                case 2:
                    artistDetails();
                    editorArtistMenu();
                    break;
                case 3:
                    insertArtist();
                    editorArtistMenu();
                    break;
                case 4:
                    editArtist();
                    editorArtistMenu();
                    break;
                case 5:
                    removeArtist();
                    editorArtistMenu();
                    break;
                case 6:
                    editorMenu();
                    break;
                default:
                    System.out.println("\n-----------------");
                    System.out.println("Option is invalid");
                    System.out.println("-----------------");
                    editorMenu();
            }

        } catch (NumberFormatException ne) {
            System.out.println("\n-----------------");
            System.out.println("Option is invalid");
            System.out.println("-----------------");
            regularMenu();
        }
    }

    /**
     * fucnao que permite inserir um artista
     */
    private static void insertArtist() {
        System.out.println("              DropMusic                ");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("\nName: ");
        String name = keyboard.nextLine();

        System.out.print("\nAge: ");
        String age = keyboard.nextLine();

        System.out.print("\nMusic_style: ");
        String music_style = keyboard.nextLine();

        System.out.print("\nBiography: ");
        String bio = keyboard.nextLine();

        boolean insert_artist_check = false;

        try {
            insert_artist_check = server.insertArtist(name, age, music_style, bio);

        } catch (RemoteException e) {
            insert_artist_check=catchRemoteExceptionInsertArtist(name,age,music_style,bio);
        }
        if (insert_artist_check) {
            System.out.println("\nNew artist created successfully.\n");
        } else {
            System.out.println("\nError while creating new artist.");
            System.out.println("Please try again.\n");
            insertArtist();
        }
    }

    /**
     * funcao que permite editar um artista
     */
    private static void editArtist() {
        System.out.println("            Alterar Artista               ");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("\nCurrent name: ");
        String current_name = keyboard.nextLine();

        boolean check_artist = false;
        try {
            check_artist=server.checkArtist(current_name);
        }catch (RemoteException re){
            check_artist=catchRemoteExceptionCheckArtist(current_name);
        }

        if(check_artist) {
            System.out.print("\nNew Name: ");
            String name = keyboard.nextLine();

            System.out.print("\nNew Age: ");
            String age = keyboard.nextLine();

            System.out.print("\nNew Music_style: ");
            String music_style = keyboard.nextLine();

            System.out.print("\nNew Biography: ");
            String bio = keyboard.nextLine();

            boolean change_artist_check = false;

            try {
                change_artist_check = server.editArtist(current_name, name, age, music_style, bio);

            } catch (RemoteException e) {
                change_artist_check=catchRemoteExceptionEditArtist(current_name,name,age,music_style,bio);
            }
            if (change_artist_check) {
                System.out.println("\nArtist changed successfully.\n");
            } else {
                System.out.println("\nError while changing artist.");
                System.out.println("Please try again.\n");
                editArtist();
            }
        }else{
            System.out.println("Artist not exist\n");
            editArtist();
        }
    }

    /**
     * funcao que permite remover um artista
     */
    private static void removeArtist() {
        System.out.println("               Remover Artista                ");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("\nName: ");
        String name = keyboard.nextLine();
        boolean check_artist=false;
        try {
            check_artist = server.checkArtist(name);

        } catch (RemoteException e) {
            check_artist=catchRemoteExceptionCheckArtist(name);
        }

        if(check_artist){
            boolean remove_artist_check = false;
            try {
                remove_artist_check = server.removeArtist(name);

            } catch (RemoteException e) {
                remove_artist_check=catchRemoteExceptionRemoveArtist(name);
            }


            if (remove_artist_check) {
                System.out.println("\nArtist removed successfully.\n");

            } else {
                System.out.println("\nError while removing artist.");
                System.out.println("Please try again.\n");
                removeArtist();
            }
        }else{
            System.out.println("Artist not exist\n");
            removeArtist();
        }


    }

    /**
     * menu que permite operar sobre os albuns
     */
    private static void editorAlbumMenu() {

        System.out.println("\n--------------------Album Menu----------------------");
        System.out.println("1-Album list");
        System.out.println("2-Details of Album");
        System.out.println("3-Insert Album");
        System.out.println("4-Edit Album");
        System.out.println("5-Remove Album");
        System.out.println("6-Back");
        System.out.print("\nOption: ");

        Scanner keyboard = new Scanner(System.in);
        String option = keyboard.next();

        try {
            switch (Integer.parseInt(option)) {
                case 1:
                    albumList();
                    editorAlbumMenu();
                    break;
                case 2:
                    albumDetails();
                    editorAlbumMenu();
                    break;
                case 3:
                    insertAlbum();
                    editorAlbumMenu();
                    break;
                case 4:
                    editAlbum();
                    regularAlbumMenu();
                    break;
                case 5:
                    removeAlbum();
                    editorAlbumMenu();
                    break;
                case 6:
                    editorMenu();
                    break;
                default:
                    System.out.println("\n-----------------");
                    System.out.println("Option is invalid");
                    System.out.println("-----------------");
                    editorMenu();
            }

        } catch (NumberFormatException ne) {
            System.out.println("\n-----------------");
            System.out.println("Option is invalid");
            System.out.println("-----------------");
            regularMenu();
        }

    }

    /**
     * funcao que permite inserir albuns
     */
    private static void insertAlbum() {

        System.out.println("                DropMusic                ");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("\nName: ");
        String name = keyboard.nextLine();

        System.out.print("\nRecord Label: ");
        String record_label = keyboard.nextLine();

        System.out.print("\nArtist Name: ");
        String artist_name = keyboard.nextLine();


        boolean insert_album_check = false;

        try {
            insert_album_check = server.insertAlbum(name, record_label, artist_name, client.getMy_username());


        } catch (RemoteException e) {
            insert_album_check=catchRemoteExceptionInsertAlbum(name,record_label,artist_name,my_username);
        }

        if (insert_album_check) {
            System.out.println("\nNew album created successfully.\n");

        } else {
            System.out.println("\nError while creating new album.");
            System.out.println("Please try again.\n");
            insertAlbum();
        }
    }

    /**
     * funcao que permite alterar albuns
     */
    private static void editAlbum() {
        System.out.println("             Alterar Album              ");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("\nArtist name: ");
        String artist_name = keyboard.nextLine();

        boolean check_artist = false;
        try{
            check_artist=server.checkArtist(artist_name);
        }catch(RemoteException re){
            check_artist=catchRemoteExceptionCheckArtist(artist_name);
        }

        if (check_artist) {
            System.out.print("\nCurrent name of album: ");
            String album_name = keyboard.nextLine();
            boolean check_album = false;
            try{
                check_album=server.checkAlbum(album_name);
            } catch (RemoteException e) {
                check_album=catchRemoteExceptionCheckAlbum(album_name);
            }

            if(check_album) {
                System.out.print("\nNew Name: ");
                String new_album_name = keyboard.nextLine();

                System.out.print("\nNew Record Label: ");
                String new_record_label = keyboard.nextLine();

                boolean change_album_check = false;

                try {
                    change_album_check = server.editAlbum(album_name, new_album_name, new_record_label, artist_name,client.getMy_username());

                } catch (RemoteException e) {
                    change_album_check=catchRemoteExceptionEditAlbum(album_name,new_album_name,new_record_label,artist_name,my_username);
                }
                if (change_album_check) {
                    System.out.println("\nAlbum changed successfully.\n");

                } else {
                    System.out.println("\nError while changing album.");
                    System.out.println("Please try again.\n");
                    editAlbum();
                }
            }else{
                System.out.println("Album not exist\n");
                editAlbum();
            }
        }else{
            System.out.println("Artist not exist\n");
            editAlbum();
        }
    }

    /**
     * funcao que permite remover albuns
     */
    private static void removeAlbum() {
        System.out.println("                Remover Album            ");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("\nName of artist: ");
        String name_artist = keyboard.nextLine();
        boolean check_artist=false;
        try {
            check_artist=server.checkArtist(name_artist);
        } catch (RemoteException e) {
            check_artist=catchRemoteExceptionCheckArtist(name_artist);
        }

        if(check_artist) {
            System.out.println("\nName of album: ");
            String name_album = keyboard.nextLine();
            boolean check_album=false;
            try{
                check_album = server.checkAlbum(name_album);
            } catch (RemoteException e) {
                check_album=catchRemoteExceptionCheckAlbum(name_album);
            }
            if(check_album){
                boolean remove_album_check = false;

                try {
                    remove_album_check = server.removeAlbum(name_album, name_artist);

                } catch (RemoteException e) {
                    remove_album_check = catchRemoteExceptionRemoveAlbum(name_album,name_artist);
                }

                if (remove_album_check) {
                    System.out.println("\nAlbum removed successfully.\n");

                } else {
                    System.out.println("\nError while removing album.");
                    System.out.println("Please try again.\n");
                    removeAlbum();
                }
            }else{
                System.out.println("Album not exist\n");
                removeAlbum();
            }
        }else{
            System.out.println("Artist not exist\n");
            removeAlbum();
        }
    }

    /**
     * menu que permite operar sobre as musicas
     */
    private static void editorMusicMenu() {
        System.out.println("\n--------------------Music Menu----------------------");
        System.out.println("1-Music list");
        System.out.println("2-Details of Music");
        System.out.println("3-Insert Musicm");
        System.out.println("4-Edit Music");
        System.out.println("5-Remove Music");
        System.out.println("6-Download Music");
        System.out.println("7-Upload Music");
        System.out.println("8-Back");
        System.out.print("\nOption: ");

        Scanner keyboard = new Scanner(System.in);
        String option = keyboard.next();

        try {
            switch (Integer.parseInt(option)) {
                case 1:
                    musicList();
                    editorMusicMenu();
                    break;
                case 2:
                    musicDetails();
                    editorMusicMenu();
                    break;
                case 3:
                    insertMusic();
                    editorMusicMenu();
                    break;
                case 4:
                    editMusic();
                    editorMusicMenu();
                    break;
                case 5:
                    removeMusic();
                    editorMusicMenu();
                    break;
                case 6:
                    downloadMusic();
                    editorMusicMenu();
                    break;
                case 7:
                    uploadMusic();
                    editorMusicMenu();
                    break;
                case 8:
                    editorMenu();
                    break;
                default:
                    System.out.println("\n-----------------");
                    System.out.println("Option is invalid");
                    System.out.println("-----------------");
                    editorMenu();
            }

        } catch (NumberFormatException ne) {
            System.out.println("\n-----------------");
            System.out.println("Option is invalid");
            System.out.println("-----------------");
            editorMenu();
        }
    }

    /**
     * funcao que permite inserir musicas
     */
    private static void insertMusic() {
        System.out.println("              DropMusic               ");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("\nName: ");
        String name = keyboard.nextLine();

        System.out.print("\nMusical_Gender: ");
        String musical_gender = keyboard.nextLine();

        System.out.print("\nLyrics: ");
        String lyrics = keyboard.nextLine();

        System.out.print("\nComposer: ");
        String composer = keyboard.nextLine();

        System.out.print("\nRealease_Date: ");
        String realease_date = keyboard.nextLine();

        System.out.print("\nAlbum_name: ");
        String album_name = keyboard.nextLine();

        System.out.print("\nArtist name: ");
        String artist_name = keyboard.nextLine();

        boolean insert_music_check=false;

        try {
            insert_music_check = server.insertMusic(name,musical_gender,lyrics,composer,realease_date,album_name,artist_name);

        } catch (RemoteException e) {
            insert_music_check=catchRemoteExceptionInsertMusic(name,musical_gender,lyrics,composer,realease_date,album_name,artist_name);
        }
        if (insert_music_check) {
            System.out.println("\nNew music created successfully.\n");

        } else {
            System.out.println("\nError while creating new music.");
            System.out.println("Please try again.\n");
            insertMusic();
        }
    }

    /**
     * funcao que permite editar musicas
     */
    private static void editMusic() {
        System.out.println("              Alterar Musica              ");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("\nArtist name: ");
        String artist_name = keyboard.nextLine();

        boolean check_artist = false;
        try{
            check_artist=server.checkArtist(artist_name);
        } catch (RemoteException e) {
            check_artist=catchRemoteExceptionCheckArtist(artist_name);
        }

        if(check_artist){
            System.out.print("\nNname of album: ");
            String album_name = keyboard.nextLine();

            boolean check_album = false;
            try{
                check_album=server.checkAlbum(album_name);
            } catch (RemoteException e) {
                catchRemoteExceptionCheckAlbum(album_name);
            }
            if(check_album){
                System.out.print("\nCurrent name of music: ");
                String current_music = keyboard.nextLine();
                boolean check_music = false;
                try{
                    check_music=server.checkMusic(current_music);
                } catch (RemoteException e) {
                    check_music=catchRemoteExceptionCheckMusic(current_music);
                }

                if(check_music){
                    System.out.print("\nNew music name: ");
                    String new_music_name = keyboard.nextLine();

                    System.out.print("\nNew musical gender: ");
                    String new_gender = keyboard.nextLine();

                    System.out.print("\nNew lyrisc: ");
                    String new_lyrisc = keyboard.nextLine();

                    System.out.print("\nNew composer: ");
                    String new_composer = keyboard.nextLine();

                    System.out.print("\nNew realease date: ");
                    String new_date = keyboard.nextLine();

                    boolean change_music_check=false;

                    try {
                        change_music_check = server.editMusic(current_music,new_music_name,new_gender,new_lyrisc,new_composer,new_date,album_name,artist_name);

                    } catch (RemoteException e) {
                        change_music_check = catchRemoteExceptionEditMusic(current_music,new_music_name,new_gender,new_lyrisc,new_composer,new_date,album_name,artist_name);
                    }
                    if (change_music_check) {
                        System.out.println("\nMusic changed successfully.\n");

                    } else {
                        System.out.println("\nError while changing music.");
                        System.out.println("Please try again.\n");
                        editMusic();
                    }
                }else{
                    System.out.println("Music not exist\n");
                    editMusic();
                }

            }else{
                System.out.println("Album not exist\n");
                editMusic();
            }

        }else{
            System.out.println("Artist not exist\n");
            editMusic();
        }

    }

    /**
     * funcao que permite remover musicas
     */
    private static void removeMusic() {
        System.out.println("              Remover Musica                ");
        Scanner keyboard = new Scanner(System.in);

        System.out.print("\nName of artist: ");
        String name_artist = keyboard.nextLine();
        boolean check_artist = false;
        try{
            check_artist = server.checkArtist(name_artist);
        } catch (RemoteException e) {
            check_artist=catchRemoteExceptionCheckArtist(name_artist);
        }
        if(check_artist){
            System.out.print("\nName of album: ");
            String name_album = keyboard.nextLine();
            boolean check_album=false;
            try{
                check_album=server.checkAlbum(name_album);
            } catch (RemoteException e) {
                check_album=catchRemoteExceptionCheckAlbum(name_album);
            }
            if(check_album){
                System.out.print("\nName of music: ");
                String music_name = keyboard.nextLine();
                boolean check_music=false;
                try {
                    check_music=server.checkMusic(music_name);
                } catch (RemoteException e) {
                    check_music=catchRemoteExceptionCheckMusic(music_name);
                }
                if (check_music){
                    boolean remove_album_check=false;

                    try {
                        remove_album_check = server.removeMusic(music_name,name_album,name_artist);

                    } catch (RemoteException e) {
                        remove_album_check=catchRemoteExceptionRemoveMusic(music_name,name_album,name_artist);
                    }

                    if (remove_album_check) {
                        System.out.println("\nMusic removed successfully.\n");

                    } else {
                        System.out.println("\nError while removing Music.");
                        System.out.println("Please try again.\n");
                        removeMusic();
                    }

                }else{
                    System.out.println("Music not exist");
                    removeMusic();
                }
            }else{
                System.out.println("Album not exist\n");
                removeMusic();
            }

        }else{
            System.out.println("Artist not exist\n");
            removeMusic();
        }

    }

    /**
     * funcao que permite fazer downloads
     */
    private static void downloadMusic(){
        System.out.println("------------Download Music------------");
        System.out.print("Artista da musica: ");
        Scanner keyboard = new Scanner(System.in);
        String artist_name = keyboard.nextLine();
        boolean check_artist =false;
        try{
            check_artist=server.checkArtist(artist_name);
        }catch (RemoteException re){
            check_artist=catchRemoteExceptionCheckArtist(artist_name);
        }

        if(check_artist){
            System.out.print("Album da musica: ");
            String album = keyboard.nextLine();
            boolean check_album=false;
            try{
                check_album=server.checkAlbum(album);
            }catch (RemoteException re){
                check_album=catchRemoteExceptionCheckAlbum(album);
            }
            if(check_album){
                System.out.print("Nome da musica: ");
                String music_name= keyboard.nextLine();
                boolean check_music=false;
                try{
                    check_music=server.checkMusic(music_name);
                }catch(RemoteException te){
                    check_music=catchRemoteExceptionCheckMusic(music_name);
                }

                if(check_music){
                    int server_port=0;
                    try {
                        server_port=server.downloadMusic(music_name);
                    } catch (RemoteException e) {
                        server_port=catchRemoteExceptionDownloadMusic(music_name);
                    }
                    String path = "/Users/pedrochicoria/Desktop/UC/LEI/SD/SD_Project/DropMusic/Downloads/" + music_name + ".mp3";
                    try {
                        Socket socket = new Socket("localhost", server_port);
                        System.out.println("SOCKET: " + socket);


                        byte[] contents = new byte[10000];

                        //Initialize the FileOutputStream to the output file's full path.
                        FileOutputStream fos = new FileOutputStream(path);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        InputStream is = socket.getInputStream();

                        //No of bytes read in one read() call
                        int bytesRead = 0;

                        while ((bytesRead = is.read(contents)) != -1)
                            bos.write(contents, 0, bytesRead);

                        bos.flush();
                        socket.close();
                        System.out.println("File saved successfully!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("Music not exist");
                }
            }else{
                System.out.println("Album not exist");
            }
        }else{
            System.out.println("Artist not exist");
        }
    }

    /**
     * funcao que permite fazer uploads
     */
    private static void uploadMusic(){
        System.out.println("------------Upload Music------------");
        System.out.print("Artista da musica: ");
        Scanner keyboard = new Scanner(System.in);
        String artist_name = keyboard.nextLine();
        boolean check_artist =false;
        try{
            check_artist=server.checkArtist(artist_name);
        }catch (RemoteException re){
            check_artist=catchRemoteExceptionCheckArtist(artist_name);
        }

        if(check_artist){
            System.out.print("Album da musica: ");
            String album = keyboard.nextLine();
            boolean check_album=false;
            try{
                check_album=server.checkAlbum(album);
            }catch (RemoteException re){
                check_album=catchRemoteExceptionCheckAlbum(album);
            }
            if(check_album){
                System.out.print("Nome da musica: ");
                String music_name= keyboard.nextLine();
                boolean check_music=false;
                try{
                    check_music=server.checkMusic(music_name);
                }catch(RemoteException te){
                    check_music=catchRemoteExceptionCheckMusic(music_name);
                }

                if(check_music){
                    int server_port=0;
                    try {
                        server_port=server.uploadMusic(music_name);
                    } catch (RemoteException e) {
                        server_port = catchRemoteExceptionUploadMusic(music_name);
                    }
                    String path = "/Users/pedrochicoria/Desktop/UC/LEI/SD/SD_Project/DropMusic/src/" + music_name + ".mp3";
                    try {
                        System.out.println(server_port);
                        Socket socket = new Socket("localhost", server_port);
                        System.out.println("SOCKET: " + socket);

                        File file = new File(path);
                        FileInputStream fis = new FileInputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(fis);

                        OutputStream os = socket.getOutputStream();

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
                        socket.close();

                        System.out.println("File sent succesfully!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else{
                    System.out.println("Music not exist");
                }
            }else{
                System.out.println("Album not exist");
            }
        }else{
            System.out.println("Artist not exist");
        }
    }

    /**
     * conjunto de funcoes que permitem tratar cada excepção sempre que é invocado um metodo do RMI Server
     * @param username
     * @param password
     * @return
     */
    private static boolean catchRemoteExceptionLogin(String username, String password) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_login = server.checkLogin(username, password);
                System.out.println("Deu lookup");

                return check_login;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private  static boolean catchRemoteExceptionCheckPrivilege(String username) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_privilege = server.checkUserPrivilege(username);
                System.out.println("Deu lookup");

                return check_privilege;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionCheckUser(String username) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_user = server.checkUser(username);
                System.out.println("Deu lookup");

                return check_user;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionCheckArtist(String artist) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_artist = server.checkArtist(artist);
                System.out.println("Deu lookup");

                return check_artist;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionCheckAlbum(String album_name) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_album = server.checkAlbum(album_name);
                System.out.println("Deu lookup");

                return check_album;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionCheckMusic(String music) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_music = server.checkMusic(music);
                System.out.println("Deu lookup");

                return check_music;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionCheckReview(String album_name, String username) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_review = server.checkReview(album_name,username);
                System.out.println("Deu lookup");

                return check_review;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionCheckPlayList(String playlist_name) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_playlist = server.checkPlaylist(playlist_name);
                System.out.println("Deu lookup");

                return check_playlist;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionCheckUserPlayList(String playlist_name,String username) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_playlist = server.checkUserPlaylist(playlist_name,username);
                System.out.println("Deu lookup");

                return check_playlist;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionInsertUser(String username, String password) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_insert = server.insertUser(username,password);
                System.out.println("Deu lookup");

                return check_insert;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionInsertUserPrivilege(String username) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_insert = server.insertUserPrivilege(username);
                System.out.println("Deu lookup");

                return check_insert;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionInsertArtist(String artist_name, String artist_age, String artist_music_style, String artist_biography) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_insert =server.insertArtist(artist_name,artist_age,artist_music_style,artist_biography);
                System.out.println("Deu lookup");

                return check_insert;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionInsertAlbum(String album_name, String album_record_label, String artist_name,String client) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_insert = server.insertAlbum(album_name, album_record_label, artist_name, client);
                System.out.println("Deu lookup");

                return check_insert;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionInsertMusic(String music_name, String music_gender, String music_lyrics, String music_composer, String music_release_date, String album_name, String artist_name) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_insert = server.insertMusic(music_name,music_gender,music_lyrics,music_composer,music_release_date,album_name,artist_name);
                System.out.println("Deu lookup");

                return check_insert;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionInsertReview(String username, String description, String rating, String album_name, String artist_name) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_insert = server.insertReview(username,description,rating,album_name,artist_name);
                System.out.println("Deu lookup");

                return check_insert;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionInsertPlayList(String playlist_name, String playlist_privilege, String creator) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_insert = server.insertPlaylist(playlist_name,playlist_privilege,creator);
                System.out.println("Deu lookup");

                return check_insert;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionInsertMusicPlayList(String playlist_name, String music_name, String album_name, String artist_name, String creator) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_insert = server.insertMusicPlaylist(playlist_name,music_name,album_name,artist_name,creator);
                System.out.println("Deu lookup");

                return check_insert;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionEditUser(String user_to_edit, String username, String password, String privilege) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_edit = server.editUser(user_to_edit,username,password,privilege);
                System.out.println("Deu lookup");

                return check_edit;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionEditArtist(String artist_to_edit, String artist_name, String artist_age, String artist_music_style, String artist_biography) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_edit = server.editArtist(artist_to_edit,artist_name,artist_age,artist_music_style,artist_biography);
                System.out.println("Deu lookup");

                return check_edit;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionEditAlbum(String album_to_edit, String album_name, String album_record_label, String artist_name,String username) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_edit = server.editAlbum(album_to_edit,album_name,album_record_label,artist_name,username);
                System.out.println("Deu lookup");

                return check_edit;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionEditMusic(String music_to_edit, String music_name, String music_gender, String music_lyrics, String music_composer, String music_release_date, String album_name, String artist_name) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_edit = server.editMusic(music_to_edit,music_name,music_gender,music_lyrics,music_composer,music_release_date,album_name,artist_name);
                System.out.println("Deu lookup");

                return check_edit;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionEditReview(String review_to_edit, String username, String description, String rating, String album_name, String artist_name) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_edit = server.editReview(review_to_edit,username,description,rating,album_name,artist_name);
                System.out.println("Deu lookup");

                return check_edit;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionEditPlayList(String playlist_to_edit, String playlist_name, String playlist_privilege, String creator) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_edit = server.editPlaylist(playlist_to_edit,playlist_name,playlist_privilege,creator);
                System.out.println("Deu lookup");

                return check_edit;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    private static boolean catchRemoteExceptionRemoveUser(String username) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_remove = server.removeUser(username);
                System.out.println("Deu lookup");

                return check_remove;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionRemoveArtist(String artist) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_remove = server.removeArtist(artist);
                System.out.println("Deu lookup");

                return check_remove;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionRemoveAlbum(String album_name, String artist_name) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_remove = server.removeAlbum(album_name,artist_name);
                System.out.println("Deu lookup");

                return check_remove;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionRemoveMusic(String music_name, String album_name,String artist_name) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_remove = server.removeMusic(music_name,album_name,artist_name);
                System.out.println("Deu lookup");

                return check_remove;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionRemoveReview(String album_name, String username,String artist_name) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_remove = server.removeReview(album_name,username,artist_name);
                System.out.println("Deu lookup");

                return check_remove;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean catchRemoteExceptionDeletetClient(ClientInterface client) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_remove = server.deleteClient(client);
                System.out.println("Deu lookup");

                return check_remove;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    private static boolean catchRemoteExceptionRemovePlayList(String playlist_name, String username) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                boolean check_remove = server.removePlaylist(playlist_name,username);
                System.out.println("Deu lookup");

                return check_remove;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    private static ArrayList<String> catchRemoteExceptionsearchUser() {
        boolean flag=false;
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                ArrayList<String> users = server.searchUser();
                System.out.println("Deu lookup");
                flag=true;
                return users;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static ArrayList<String> catchRemoteExceptionsearchArtist() {
        boolean flag=false;
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                ArrayList<String> artists = server.searchArtist();
                System.out.println("Deu lookup");
                flag=true;
                return artists;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static ArrayList<String> catchRemoteExceptionsearchAlbuns() {
        boolean flag=false;
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                ArrayList<String> albuns = server.searchAlbum();
                System.out.println("Deu lookup");
                flag=true;
                return albuns;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static ArrayList<String> catchRemoteExceptionsearchAlbumByName(String album_name) {
        boolean flag=false;
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                ArrayList<String> albuns = server.searchAlbumByName(album_name);
                System.out.println("Deu lookup");
                flag=true;
                return albuns;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static ArrayList<String> catchRemoteExceptionsearchAlbumByArtist(String artist_name) {
        boolean flag=false;
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                ArrayList<String> albuns = server.searchAlbumByName(artist_name);
                System.out.println("Deu lookup");
                flag=true;
                return albuns;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static ArrayList<String> catchRemoteExceptionsearchMusics() {
        boolean flag=false;
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                ArrayList<String> musics = server.searchMusic();
                System.out.println("Deu lookup");
                flag=true;
                return musics;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static ArrayList<String> catchRemoteExceptionsearchMusicByAlbum(String album_name,String artist_name) {
        boolean flag=false;
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                ArrayList<String> musics = server.searchMusicByAlbum(album_name,artist_name);
                System.out.println("Deu lookup");
                flag=true;
                return musics;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private static ArrayList<String> catchRemoteExceptionsearchMusicByPlayList(String playlist_name, String creator) {
        boolean flag=false;
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                ArrayList<String> musics = server.searchMusicByPlaylist(playlist_name,creator);
                System.out.println("Deu lookup");
                flag=true;
                return musics;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private static ArrayList<String> catchRemoteExceptionsearchPrivatePlayList(String username) {
        boolean flag=false;
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                ArrayList<String> playlists = server.searchPrivatePlaylist(username);
                System.out.println("Deu lookup");
                flag=true;
                return playlists;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static ArrayList<String> catchRemoteExceptionsearchPublicPlayList() {
        boolean flag=false;
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                ArrayList<String> playlists = server.searchPublicPlaylist();
                System.out.println("Deu lookup");
                flag=true;
                return playlists;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static ArrayList<String> catchRemoteExceptionDetailsUser(String username) {
        boolean flag=false;
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                ArrayList<String> details = server.detailsUser(username);
                System.out.println("Deu lookup");
                flag=true;
                return details;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static ArrayList<String> catchRemoteExceptionDetailsArtist(String artist) {
        boolean flag=false;
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                ArrayList<String> details = server.detailsArtist(artist);
                System.out.println("Deu lookup");
                flag=true;
                return details;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static ArrayList<String> catchRemoteExceptionDetailsAlbum(String album_name,String artist) {
        boolean flag=false;
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                ArrayList<String> details = server.detailsAlbum(album_name,artist);
                System.out.println("Deu lookup");
                flag=true;
                return details;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static ArrayList<String> catchRemoteExceptionDetailsMusic(String music, String album_name,String artist) {
        boolean flag=false;
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                ArrayList<String> details = server.detailsMusic(music,album_name,artist);
                System.out.println("Deu lookup");
                flag=true;
                return details;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private static ArrayList<String> catchRemoteExceptionDetailsReview(String album_name,String artist) {
        boolean flag=false;
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                ArrayList<String> details = server.detailsReview(album_name,artist);
                System.out.println("Deu lookup");
                flag=true;
                return details;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static int catchRemoteExceptionUploadMusic(String music) {
        boolean flag=false;
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                int port = server.uploadMusic(music);
                System.out.println("Deu lookup");
                flag=true;
                return port;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private static int catchRemoteExceptionDownloadMusic(String music) {
        boolean flag=false;
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                lookupRMI();
                int port = server.downloadMusic(music);
                System.out.println("Deu lookup");
                flag=true;
                return port;

            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection");
                    break;
                }
            } catch (NotBoundException e) {
                System.out.println("Error on lookup: " + e);
                break;
            } catch (MalformedURLException m) {
                System.out.println("Error on Malformed: " + m);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private static void lookupRMI() throws RemoteException, NotBoundException, MalformedURLException {
        server = (ServerInterface) Naming.lookup("rmi://localhost:7000/DropMusic");
    }

    //notifications
    public void notifyTextualDescription() {
        System.out.println("\n\nA descrição textual do album foi alterada");
    }

    public void notifyPrivileges() {
        System.out.println("\n\nPassou a ser editor\nTem de reiniciar a sessão");
    }

    public  String getMy_username() {
        return my_username;
    }

    public  void setMy_username(String my_username) {
        RmiClient.my_username = my_username;
    }

    public static void main(String args[]) {
        Scanner keyboard = new Scanner(System.in);
        try {
            server = (ServerInterface) Naming.lookup("rmi://localhost:7000/DropMusic");

            welcomeMenu();
        } catch (NotBoundException nbe) {
            System.out.println("Not bound: " + nbe.getMessage());
        } catch (RemoteException re) {
            System.out.println("RMI: " + re.getMessage());
        } catch (MalformedURLException e) {
            System.out.println("Lookup: " + e.getMessage());
        } finally {
            keyboard.close();
            System.out.println("Bye Bye");
            try {
                server.deleteClient(client);
            } catch (RemoteException e) {
                catchRemoteExceptionDeletetClient(client);
            }
            System.exit(0);
        }

    }
}