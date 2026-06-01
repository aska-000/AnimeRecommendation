package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnimeTest {

    @Test
    void constructor_setsAllFields() {
        Anime anime = new Anime(1, "Наруто", "Экшен", 8.3, "Описание", "images/naruto.jpg");

        assertEquals(1, anime.id);
        assertEquals("Наруто", anime.title);
        assertEquals("Экшен", anime.genre);
        assertEquals(8.3, anime.rating);
        assertEquals("Описание", anime.description);
        assertEquals("images/naruto.jpg", anime.imagePath);
    }

    @Test
    void toString_returnsFormattedString() {
        Anime anime = new Anime(1, "Наруто", "Экшен", 8.3, "Описание", "images/naruto.jpg");
        assertEquals("1 | Наруто | Экшен | 8.3", anime.toString());
    }
}