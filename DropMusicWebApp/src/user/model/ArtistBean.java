package user.model;

import classes.Album;
import classes.Artist;
import model.UserBean;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ArtistBean extends UserBean {
    private String name;
    private String newname;
    private String age;
    private String musicstyle;
    private String albumname;
    private String biography;

    public ArtistBean() {
       super();
    }

    public boolean getCreateArtist(){
        boolean check=false;
        try{
            check = server.insertArtist(name,age,musicstyle,biography);
            return check;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return check;
    }

    public boolean getEditArtist(){
        boolean check=false;
        try{
            check = server.editArtist(name,newname,age,musicstyle,biography);
            return check;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return check;
    }

    public ArrayList<String> getListArtist(){
        try{
            return server.searchArtist();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Artist getDetailsOfArtist(){
        try{
            ArrayList<String> details = server.detailsArtist(name);
            Artist artist = new Artist(details.get(0),Integer.parseInt(details.get(1)),details.get(2),details.get(3));
            return artist;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Album getDetailsOfAlbum(){
        Album album=null;
        try{
            ArrayList<String> details = server.detailsAlbum(albumname,name);
            album= new Album(details.get(0),details.get(1));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return album;
    }


    public ArrayList<String> getAlbumsbyArtist(){
        try{
            return server.searchAlbumByArtist(name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean getDeleteArtist(){
        boolean check=false;
        try{
            check = server.removeArtist(name);
            return check;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return check;
    }
    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getNewname() {
        return newname;
    }

    public void setNewname(String newname) {
        this.newname = newname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMusicstyle() {
        return musicstyle;
    }

    public void setMusicstyle(String musicstyle) {
        this.musicstyle = musicstyle;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
