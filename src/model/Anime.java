package model;

public class Anime {

    public int id;
    public String title;
    public String genre;
    public double rating;
    public String description;

    public Anime(int id, String title, String genre, double rating, String description) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.description = description;
    }

    @Override
    public String toString() {
        return  id + " | " + title + " | " + genre + " | " + rating;
    }
}