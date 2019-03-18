package rmi;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerInterface extends Remote {
    //Remote methods that allow the communication between the RmiClient and the RmiServer
    void testConnectionRMI() throws RemoteException;

    //Check

    /**
     * funcao que envia pedido de login ao multicast
     * @param username
     * @param password
     * @return
     * @throws RemoteException
     */
    boolean checkLogin(String username, String password) throws RemoteException;

    /**
     * funcao que envia pedidio de verificacao de privilegio
     * @param username
     * @return
     * @throws RemoteException
     */
    boolean checkUserPrivilege(String username) throws RemoteException;

    /**
     * funcao que envia pedidio de verificacao de username
     * @param username
     * @return
     * @throws RemoteException
     */
    boolean checkUser(String username) throws RemoteException;

    /**
     * funcao que envia pedidio de verificacao de artista
     * @param artist_name
     * @return
     * @throws RemoteException
     */
    boolean checkArtist(String artist_name) throws RemoteException;

    /**
     * funcao que envia pedidio de verificacao de album
     * @param album_name
     * @return
     * @throws RemoteException
     */
    boolean checkAlbum(String album_name) throws RemoteException;

    /**
     * funcao que envia pedidio de verificacao de musica
     * @param music_name
     * @return
     * @throws RemoteException
     */
    boolean checkMusic(String music_name) throws RemoteException;

    /**
     * funcao que envia pedidio de verificacao de critica
     * @param album_name
     * @param username
     * @return
     * @throws RemoteException
     */
    boolean checkReview(String album_name, String username) throws RemoteException;

    /**
     * funcao que envia pedidio de verificacao de playlist
     * @param playlist_name
     * @return
     * @throws RemoteException
     */
    boolean checkPlaylist(String playlist_name) throws RemoteException;

    /**
     * funcao que envia pedidio de verificacao de playlist do user
     * @param playlist_name
     * @param username
     * @return
     * @throws RemoteException
     */
    boolean checkUserPlaylist(String playlist_name, String username) throws RemoteException;

    //Insert

    /**
     * funcao que envia pedidio de insercao de user
     * @param username
     * @param password
     * @param client
     * @return
     * @throws RemoteException
     */
    boolean insertUser(String username, String password) throws RemoteException;

    /**
     * funcao que envia pedidio de insercao de privilegio
     * @param username
     * @return
     * @throws RemoteException
     * @throws InterruptedException
     */
    boolean insertUserPrivilege(String username) throws RemoteException, InterruptedException;

    /**
     * funcao que envia pedidio de insercao de artista
     * @param artist_name
     * @param artist_age
     * @param artist_music_style
     * @param artist_biography
     * @return
     * @throws RemoteException
     */
    boolean insertArtist(String artist_name, String artist_age, String artist_music_style, String artist_biography) throws RemoteException;

    /**
     * funcao que envia pedidio de insercao de album
     * @param album_name
     * @param album_record_label
     * @param artist_name
     * @param client
     * @return
     * @throws RemoteException
     */
    boolean insertAlbum(String album_name, String album_record_label, String artist_name,String client) throws RemoteException;

    /**
     * funcao que envia pedidio de insercao de musica
     * @param music_name
     * @param music_gender
     * @param music_lyrics
     * @param music_composer
     * @param music_release_date
     * @param album_name
     * @param artist_name
     * @return
     * @throws RemoteException
     */
    boolean insertMusic(String music_name, String music_gender, String music_lyrics, String music_composer, String music_release_date, String album_name, String artist_name) throws RemoteException;

    /**
     * funcao que envia pedidio de insercao de critica
     * @param username
     * @param description
     * @param rating
     * @param album_name
     * @param artist_name
     * @return
     * @throws RemoteException
     */
    boolean insertReview(String username, String description, String rating, String album_name, String artist_name) throws RemoteException;

    /**
     * funcao que envia pedidio de insercao de playlist
     * @param playlist_name
     * @param playlist_privilege
     * @param creator
     * @return
     * @throws RemoteException
     */
    boolean insertPlaylist(String playlist_name, String playlist_privilege, String creator) throws RemoteException;

    /**
     * funcao que envia pedidio de insercao de musicas à playlist
     * @param playlist_name
     * @param music_name
     * @param album_name
     * @param artist_name
     * @param creator
     * @return
     * @throws RemoteException
     */
    boolean insertMusicPlaylist(String playlist_name, String music_name, String album_name, String artist_name, String creator) throws RemoteException;

    //Edit

    /**
     * funcao que envia pedidio de altercao de user
     * @param user_to_edit
     * @param username
     * @param password
     * @param privilege
     * @return
     * @throws RemoteException
     */
    boolean editUser(String user_to_edit, String username, String password, String privilege) throws RemoteException;

    /**
     * funcao que envia pedidio de altercao de artista
     * @param artist_to_edit
     * @param artist_name
     * @param artist_age
     * @param artist_music_style
     * @param artist_biography
     * @return
     * @throws RemoteException
     */
    boolean editArtist(String artist_to_edit, String artist_name, String artist_age, String artist_music_style, String artist_biography) throws RemoteException;

    /**
     * funcao que envia pedidio de altercao de album
     * @param album_to_edit
     * @param album_name
     * @param album_record_label
     * @param artist_name
     * @param username
     * @return
     * @throws RemoteException
     */
    boolean editAlbum(String album_to_edit, String album_name, String album_record_label, String artist_name,String username) throws RemoteException;

    /**
     * funcao que envia pedidio de altercao de musica
     * @param music_to_edit
     * @param music_name
     * @param music_gender
     * @param music_lyrics
     * @param music_composer
     * @param music_release_date
     * @param album_name
     * @param artist_name
     * @return
     * @throws RemoteException
     */
    boolean editMusic(String music_to_edit, String music_name, String music_gender, String music_lyrics, String music_composer, String music_release_date, String album_name, String artist_name) throws RemoteException;

    /**
     * funcao que envia pedidio de altercao de critica
     * @param review_to_edit
     * @param username
     * @param description
     * @param rating
     * @param album_name
     * @param artist_name
     * @return
     * @throws RemoteException
     */
    boolean editReview(String review_to_edit, String username, String description, String rating, String album_name, String artist_name) throws RemoteException;

    /**
     * funcao que envia pedidio de altercao de playlist
     * @param playlist_to_edit
     * @param playlist_name
     * @param playlist_privilege
     * @param creator
     * @return
     * @throws RemoteException
     */
    boolean editPlaylist(String playlist_to_edit, String playlist_name, String playlist_privilege, String creator) throws RemoteException;

    //Remove

    /**
     * funcao que envia pedidio de remocao de user
     * @param username
     * @return
     * @throws RemoteException
     */
    boolean removeUser(String username) throws RemoteException;

    /**
     * funcao que envia pedidio de remocao de artista
     * @param artist_name
     * @return
     * @throws RemoteException
     */
    boolean removeArtist(String artist_name) throws RemoteException;

    /**
     * funcao que envia pedidio de remocao de album
     * @param album_name
     * @param artist_name
     * @return
     * @throws RemoteException
     */
    boolean removeAlbum(String album_name, String artist_name) throws RemoteException;

    /**
     * funcao que envia pedidio de remocao de musica
     * @param music_name
     * @param album_name
     * @param artist_name
     * @return
     * @throws RemoteException
     */
    boolean removeMusic(String music_name, String album_name, String artist_name) throws RemoteException;

    /**
     * funcao que envia pedidio de remocao de critica
     * @param album_name
     * @param username
     * @param artist_name
     * @return
     * @throws RemoteException
     */
    boolean removeReview(String album_name, String username, String artist_name) throws RemoteException;

    /**
     * funcao que envia pedidio de remocao de playlist
     * @param playlist_name
     * @param username
     * @return
     * @throws RemoteException
     */
    boolean removePlaylist(String playlist_name, String username) throws RemoteException;

    //Search

    /**
     * funcao que envia pedido de procura de users
     * @return
     * @throws RemoteException
     */
    ArrayList<String> searchUser() throws RemoteException;

    /**
     * funcao que envia pedido de procura de artistas
     * @return
     * @throws RemoteException
     */
    ArrayList<String> searchArtist() throws RemoteException;

    /**
     * funcao que envia pedido de procura de albuns
     * @return
     * @throws RemoteException
     */
    ArrayList<String> searchAlbum() throws RemoteException;

    /**
     * funcao que envia pedido de procura de albuns por nome de album
     * @param album_name
     * @return
     * @throws RemoteException
     */
    ArrayList<String> searchAlbumByName(String album_name) throws RemoteException;

    /**
     * funcao que envia pedido de procura de albuns por nome de artista
     * @param artist_name
     * @return
     * @throws RemoteException
     */
    ArrayList<String> searchAlbumByArtist(String artist_name) throws RemoteException;

    /**
     * funcao que envia pedido de procura de musicas
     * @return
     * @throws RemoteException
     */
    ArrayList<String> searchMusic() throws RemoteException;

    /**
     * funcao que envia pedido de procura de musicas por nome de album
     * @param album_name
     * @param artist_name
     * @return
     * @throws RemoteException
     */
    ArrayList<String> searchMusicByAlbum(String album_name, String artist_name) throws RemoteException;

    /**
     * funcao que envia pedido de procura de musicas por playlist
     * @param playlist_name
     * @param creator
     * @return
     * @throws RemoteException
     */
    ArrayList<String> searchMusicByPlaylist(String playlist_name, String creator) throws RemoteException;

    /**
     * funcao que envia pedido de procura de playlists privadas
     * @param username
     * @return
     * @throws RemoteException
     */
    ArrayList<String> searchPrivatePlaylist(String username) throws RemoteException;

    /**
     * funcao que envia pedido de procura de playlists publicas
     * @return
     * @throws RemoteException
     */
    ArrayList<String> searchPublicPlaylist() throws RemoteException;

    //Details

    /**
     * funcao que envia pedido de consulta de detalhes de user
     * @param username
     * @return
     * @throws RemoteException
     */
    ArrayList<String> detailsUser(String username) throws RemoteException;

    /**
     * funcao que envia pedido de consulta de detalhes de artista
     * @param artist_name
     * @return
     * @throws RemoteException
     */
    ArrayList<String> detailsArtist(String artist_name) throws RemoteException;

    /**
     * funcao que envia pedido de consulta de detalhes de album
     * @param album_name
     * @param artist_name
     * @return
     * @throws RemoteException
     */
    ArrayList<String> detailsAlbum(String album_name, String artist_name) throws RemoteException;

    /**
     * funcao que envia pedido de consulta de detalhes de musicas
     * @param music_name
     * @param album_name
     * @param artist_name
     * @return
     * @throws RemoteException
     */
    ArrayList<String> detailsMusic(String music_name, String album_name, String artist_name) throws RemoteException;

    /**
     * funcao que envia pedido de consulta de detalhes de criticas
     * @param album_name
     * @param artist_name
     * @return
     * @throws RemoteException
     */
    ArrayList<String> detailsReview(String album_name, String artist_name) throws RemoteException;

    /**
     * funcao que envia pedido de upload
     * @param music
     * @return
     * @throws RemoteException
     */
    int uploadMusic(String music) throws RemoteException;

    /**
     * funcao que envia pedido de download
     * @param music
     * @return
     * @throws RemoteException
     */
    int downloadMusic(String music) throws RemoteException;

    /**
     * funcao que remove um cliente da lista
     * @param client
     * @return
     * @throws RemoteException
     */
    boolean deleteClient(ClientInterface client) throws RemoteException;

}
