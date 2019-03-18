package classes;

import java.io.Serializable;

public class Review implements Serializable {
    private String username;
    private String text;
    private int rating;

    Review(){}

    public Review(String username, String text, int rating) {
        this.username = username;
        this.text = text;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
