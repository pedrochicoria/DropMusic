package classes;

import java.io.Serializable;

public class Music implements Serializable {
    private String music_name;
    private String musical_gender;
    private String lyrics;
    private String composer;
    private String realease_date;

    Music(){}

    public Music(String music_name, String musical_gender, String lyrics, String composer, String realease_date) {
        this.music_name = music_name;
        this.musical_gender = musical_gender;
        this.lyrics = lyrics;
        this.composer = composer;
        this.realease_date = realease_date;
    }

    public String getMusic_name() {
        return music_name;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
    }

    public String getMusical_gender() {
        return musical_gender;
    }

    public void setMusical_gender(String musical_gender) {
        this.musical_gender = musical_gender;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrisc(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getRealease_date() {
        return realease_date;
    }

    public void setRealease_date(String realese_date) {
        this.realease_date = realese_date;
    }
}
