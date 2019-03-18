package classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {

    private String album_name;
    private String record_label;
    private ArrayList<Review> reviews;
    private ArrayList<Music> musics;
    private ArrayList<String> usersOfAlbun;

    Album(){}

    public Album(String album_name, String record_label) {
        this.album_name = album_name;
        this.record_label = record_label;
        this.reviews = new ArrayList<>();
        this.musics = new ArrayList<>();
        this.usersOfAlbun = new ArrayList<>();
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getRecord_label() {
        return record_label;
    }

    public void setRecord_label(String record_label) {
        this.record_label = record_label;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void addReviews(Review review) {
        this.reviews.add(review);
    }

    public ArrayList<Music> getMusics() {
        return musics;
    }

    public void addMusics(Music music) {
        this.musics.add(music);
    }


    public ArrayList<String> getUsersOfAlbun() {
        return usersOfAlbun;
    }

    public void setUsersOfAlbun(ArrayList<String> usersOfAlbun) {
        this.usersOfAlbun = usersOfAlbun;
    }
}
