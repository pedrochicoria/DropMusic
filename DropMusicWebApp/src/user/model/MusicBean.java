package user.model;

import classes.Music;
import model.UserBean;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class MusicBean extends UserBean {
    private String artist;
    private String album;
    private String musicname;
    private String newname;
    private String musicalgender;
    private String lyrisc;
    private String composer;
    private String releasedate;

    public MusicBean() {
        super();
    }

    public boolean getCreateMusic(){
        boolean check=false;
        try{
            check = server.insertMusic(musicname,musicalgender,lyrisc,composer,releasedate,album,artist);
            return check;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return check;
    }

    public boolean getEditMusic(){
        boolean check=false;
        try{
            check = server.editMusic(musicname,newname,musicalgender,lyrisc,composer,releasedate,album,artist);
            return check;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return check;
    }

    public boolean getDeleteMusic(){
        boolean check=false;
        try{
            check = server.removeMusic(musicname,album,artist);
            return check;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return check;
    }

    public ArrayList<String> getListMusics(){
        try{
            return server.searchMusic();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Music getDetailsOfMusic(){
        Music music =null;
        try{
            ArrayList<String> details = server.detailsMusic(musicname,album,artist);
            music=new Music(details.get(0),details.get(1),details.get(2),details.get(3),details.get(4));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return music;
    }



    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getMusicname() {
        return musicname;
    }

    public void setMusicname(String musicname) {
        this.musicname = musicname;
    }

    public String getNewname() {
        return newname;
    }

    public void setNewname(String newname) {
        this.newname = newname;
    }

    public String getMusicalgender() {
        return musicalgender;
    }

    public void setMusicalgender(String musicalgender) {
        this.musicalgender = musicalgender;
    }

    public String getLyrisc() {
        return lyrisc;
    }

    public void setLyrisc(String lyrisc) {
        this.lyrisc = lyrisc;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }
}
