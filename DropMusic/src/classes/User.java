package classes;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String username;
    private String password;
    private boolean privilege;
    private ArrayList<Playlist> playlists;

    User(){}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.privilege = false;
        this.playlists = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Boolean privilege) {
        this.privilege = privilege;
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public void addPlaylists(Playlist playlist) {
        this.playlists.add(playlist);
    }
}
