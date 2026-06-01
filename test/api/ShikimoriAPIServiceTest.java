package api;

import model.Anime;
import org.junit.jupiter.api.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShikimoriAPIServiceTest {

    private static ShikimoriAPIService service;
    private static Method fetchDescriptionForAnimeMethod;

    @BeforeAll
    static void setUp() throws Exception {
        service = new ShikimoriAPIService();

        fetchDescriptionForAnimeMethod = ShikimoriAPIService.class.getDeclaredMethod("fetchDescriptionForAnime", int.class);
        fetchDescriptionForAnimeMethod.setAccessible(true);
    }


    @Test
    @Order(1)
    void fetchTopAnime_returnsList() throws Exception {
        ArrayList<Anime> list = service.fetchTopAnime(3);
        assertNotNull(list);
        System.out.println("Загружено аниме: " + list.size());
    }

    @Test
    @Order(2)
    void fetchTopAnime_returnsValidAnime() throws Exception {
        ArrayList<Anime> list = service.fetchTopAnime(2);
        if (!list.isEmpty()) {
            Anime anime = list.get(0);
            assertNotNull(anime.title);
            assertNotNull(anime.genre);
            assertNotNull(anime.description);
        }
    }


    @Test
    @Order(3)
    void fetchDescriptionForAnime_returnsDescription() throws Exception {
        String description = (String) fetchDescriptionForAnimeMethod.invoke(service, 16498);
        assertNotNull(description);
        assertFalse(description.isEmpty());
    }

    @Test
    @Order(4)
    void fetchDescriptionForAnime_cleansCharacterTags() throws Exception {
        String description = (String) fetchDescriptionForAnimeMethod.invoke(service, 32281); // Твоё имя
        assertNotNull(description);
        assertFalse(description.contains("[character="));
        assertFalse(description.contains("[/character]"));
    }

    @Test
    @Order(5)
    void fetchDescriptionForAnime_cleansUrlTags() throws Exception {
        String description = (String) fetchDescriptionForAnimeMethod.invoke(service, 38000); // Клинок рассекающий демонов
        assertNotNull(description);
        assertFalse(description.contains("[url="));
        assertFalse(description.contains("[/url]"));
    }

    @Test
    @Order(6)
    void fetchDescriptionForAnime_withInvalidId_returnsDefault() throws Exception {
        String description = (String) fetchDescriptionForAnimeMethod.invoke(service, 999999);
        assertNotNull(description);
        assertEquals("Описание отсутствует", description);
    }


    @Test
    @Order(7)
    void fetchOneGenreForAnime_returnsGenre() throws Exception {
        Method method = ShikimoriAPIService.class.getDeclaredMethod("fetchOneGenreForAnime", int.class);
        method.setAccessible(true);

        String genre = (String) method.invoke(service, 16498);
        assertNotNull(genre);
        assertFalse(genre.isEmpty());
    }

    @Test
    @Order(8)
    void fetchOneGenreForAnime_withInvalidId_returnsDefault() throws Exception {
        Method method = ShikimoriAPIService.class.getDeclaredMethod("fetchOneGenreForAnime", int.class);
        method.setAccessible(true);

        String genre = (String) method.invoke(service, 999999);
        assertNotNull(genre);
        assertEquals("Не указан", genre);
    }
}