public class Anime {
    int id;
    String title;
    String genre;
    double rating;

    public Anime(int id, String title, String genre, double rating) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return String.format("[%d] %-18s | Жанр: %-12s | Рейтинг: %.1f", id, title, genre, rating);
    }
}