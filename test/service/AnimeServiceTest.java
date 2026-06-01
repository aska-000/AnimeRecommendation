package service;

import database.BDAnime;
import model.Anime;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AnimeServiceTest {

    private AnimeService service;

    @BeforeAll
    void setUp() throws Exception {
        service = new AnimeService();
        BDAnime.connectionDB();
        BDAnime.initSchema();
    }

    @AfterAll
    void tearDown() throws Exception {
        BDAnime.closeDB();
    }

    @Test
    void getAllAnime_returnsList() throws Exception {
        ArrayList<Anime> list = service.getAllAnime();
        assertNotNull(list);
    }

    @Test
    void addToFavorites_addsAnime() throws Exception {
        int userId = BDAnime.checkUser("TestUser");
        int animeId = 5;

        service.addToFavorites(userId, animeId);
        ArrayList<Anime> favorites = service.getUserFavorites(userId);

        assertTrue(favorites.stream().anyMatch(a -> a.id == animeId));

        service.removeFromFavorites(userId, animeId);
    }
}