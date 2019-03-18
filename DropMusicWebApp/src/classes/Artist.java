package classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Artist implements Serializable {
    private String artist_name;
    private int age;
    private String music_style;
    private String biography;
    private ArrayList<Album> albums;
    private ArrayList<String> usernamesToNotificate;

    Artist(){}

    public Artist(String artist_name, int age, String music_style, String biography) {
        this.artist_name = artist_name;
        this.age = age;
        this.music_style = music_style;
        this.biography = biography;
        this.albums = new ArrayList<>();
        this.usernamesToNotificate = new ArrayList<>();
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMusic_style() {
        return music_style;
    }

    public void setMusic_style(String music_style) {
        this.music_style = music_style;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void addAlbums(Album album) {
        this.albums.add(album);
    }

    public ArrayList<String> getUsernamesToNotificate() {
        return usernamesToNotificate;
    }

    public void setUsernamesToNotificate(ArrayList<String> usernamesToNotificate) {
        this.usernamesToNotificate = usernamesToNotificate;
    }
}
