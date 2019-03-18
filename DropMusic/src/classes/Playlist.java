package classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Playlist implements Serializable {
    private String name;
    private String creator;
    private boolean privilege;
    private ArrayList<Music> musics;


    Playlist(){}

    public Playlist(String name,String creator, boolean privilege) {
        this.name = name;
        this.creator = creator;
        this.musics = new ArrayList<>();
        this.privilege = privilege;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Music> getMusics() {
        return musics;
    }

    public void addMusics(Music music) {
        this.musics.add(music);
    }

    public Boolean getPrivilege() {
        return privilege;
    }

    public void setPrivilege(boolean privilege) {
        this.privilege = privilege;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
