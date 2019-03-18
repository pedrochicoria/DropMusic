package user.model;

import classes.Album;
import classes.Music;
import model.UserBean;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class AlbumBean extends UserBean {
    private String username;
    private String artist;
    private String albumname;
    private String newalbum;
    private String recordlabel;
    private String music;
    private String description;
    private String rating;

    public AlbumBean() {
        super();
    }

    public boolean getCreateAlbum() {
        boolean check = false;
        try {
            check = server.insertAlbum(albumname, recordlabel, artist);
            return check;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return check;
    }

    public boolean getEditAlbum() {
        boolean check = false;
        try {
            check = server.editAlbum(albumname, newalbum, recordlabel, artist);
            return check;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return check;
    }

    public boolean getDeleteAlbum() {
        boolean check = false;
        try {
            check = server.removeAlbum(albumname, artist);
            return check;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return check;
    }

    public ArrayList<ArrayList<String>> getListAlbums() {
        try {
            ArrayList<ArrayList<String>> albumms = new ArrayList<>();
            ArrayList<String> albums = server.searchAlbum();

            for(int i = 0; i<albums.size();i++){
                ArrayList<String> a = new ArrayList<>();
                a.add(albums.get(i));
                a.add(server.getAlbumRating(albums.get(i)));
                albumms.add(a);
            }
            return albumms;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Album getDetailsOfAlbum() {
        Album album = null;
        try {
            ArrayList<String> details = server.detailsAlbum(albumname, artist);
            album = new Album(details.get(0), details.get(1));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return album;
    }

    public ArrayList<String> getMusicsbyAlbum() {
        try {
            return server.searchMusicByAlbum(albumname, artist);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Music getDetailsOfMusic() {
        Music music1 = null;
        try {
            ArrayList<String> details = server.detailsMusic(music, albumname, artist);
            music1 = new Music(details.get(0), details.get(1), details.get(2), details.get(3), details.get(4));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return music1;
    }

    public boolean getWriteReview() {
        try {
            return server.insertReview(username, description, rating, albumname, artist);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }


    public ArrayList<ArrayList<String>> getAlbumReviews() {
        try {
            ArrayList<ArrayList<String>> reviews = new ArrayList<>();
            ArrayList<String> aux = server.detailsReview(albumname, artist);


            for (int j = 0; j < aux.size(); j += 3) {
                ArrayList<String> review = new ArrayList<>();

                review.add(aux.get(j));
                review.add(aux.get(j + 1));
                review.add(aux.get(j + 2));
                reviews.add(review);

            }


            return reviews;

        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
        return null;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumname() {
        return albumname;
    }

    public String getNewalbum() {
        return newalbum;
    }

    public void setNewalbum(String newalbum) {
        this.newalbum = newalbum;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public String getRecordlabel() {
        return recordlabel;
    }

    public void setRecordlabel(String recordlabel) {
        this.recordlabel = recordlabel;
    }

}
