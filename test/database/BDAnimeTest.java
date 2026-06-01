package database;

import model.Anime;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BDAnimeTest {

    @BeforeAll
    void setUp() throws Exception {
        BDAnime.connectionDB();
        BDAnime.initSchema();
    }

    @AfterAll
    void tearDown() throws Exception {
        BDAnime.closeDB();
    }

    @Test
    void seedData_loads20Anime() throws Exception {
        BDAnime.seedData();
        ArrayList<Anime> list = BDAnime.getAllAnime();
        assertEquals(20, list.size());
    }

    @Test
    void getAllAnime_returnsList() throws Exception {
        ArrayList<Anime> list = BDAnime.getAllAnime();
        assertNotNull(list);
    }

    @Test
    void getAnimeById_returnsCorrectAnime() throws Exception {
        Anime anime = BDAnime.getAnimeById(1);
        assertNotNull(anime);
        assertEquals("Наруто", anime.title);
    }
}