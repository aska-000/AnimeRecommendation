package api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.Anime;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ShikimoriAPIService {
    private static final String BASE_URL = "https://shikimori.one/api";
    private final OkHttpClient client;
    private final Gson gson;

    public ShikimoriAPIService() {
        this.client = createOkHttpClient();
        this.gson = new Gson();
    }

    private OkHttpClient createOkHttpClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .retryOnConnectionFailure(true)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
        }
    }

    public ArrayList<Anime> fetchTopAnime(int limit) throws IOException, InterruptedException {
        ArrayList<Anime> allAnime = new ArrayList<>();
        int page = 1;
        int fetched = 0;

        while (fetched < limit && page <= 10) {
            String url = BASE_URL + "/animes?page=" + page + "&limit=" + Math.min(50, limit - fetched) + "&order=popularity";

            System.out.println("Загружаю страницу " + page + "...");

            Request request = new Request.Builder()
                    .url(url)
                    .header("User-Agent", "AnimeRecsApp/1.0")
                    .header("Accept", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.code() == 429) {
                    System.out.println("Лимит запросов, жду 10 секунд...");
                    Thread.sleep(10000);
                    continue;
                }

                if (!response.isSuccessful()) {
                    System.out.println("Ошибка HTTP: " + response.code());
                    page++;
                    Thread.sleep(2000);
                    continue;
                }

                String jsonData = response.body().string();
                JsonArray dataArray = gson.fromJson(jsonData, JsonArray.class);

                if (dataArray == null || dataArray.size() == 0) {
                    System.out.println("Нет данных на странице " + page);
                    page++;
                    Thread.sleep(1000);
                    continue;
                }

                for (int i = 0; i < dataArray.size() && fetched < limit; i++) {
                    JsonObject animeJson = dataArray.get(i).getAsJsonObject();

                    int id = animeJson.get("id").getAsInt();

                    String title = "";
                    if (animeJson.has("russian") && !animeJson.get("russian").isJsonNull()) {
                        title = animeJson.get("russian").getAsString();
                    }
                    if (title == null || title.isEmpty()) {
                        if (animeJson.has("name") && !animeJson.get("name").isJsonNull()) {
                            title = animeJson.get("name").getAsString();
                        } else {
                            title = "Без названия";
                        }
                    }

                    double rating = 0.0;
                    if (animeJson.has("score") && !animeJson.get("score").isJsonNull()) {
                        rating = animeJson.get("score").getAsDouble();
                    }

                    String imageUrl = "https://shikimori.one/system/animes/original/" + id + ".jpg";

                    String genre = fetchOneGenreForAnime(id);
                    String description = fetchDescriptionForAnime(id);

                    Anime anime = new Anime(id, title, genre, rating, description, imageUrl);
                    allAnime.add(anime);
                    fetched++;

                    String shortDesc = description.length() > 50 ? description.substring(0, 50) + "..." : description;
                    System.out.println("Загружено: " + id + " | " + title + " | Жанр: " + genre + " | Описание: " + shortDesc);

                    Thread.sleep(1000);
                }

                page++;
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println("Ошибка при загрузке страницы " + page + ": " + e.getMessage());
                page++;
                Thread.sleep(3000);
            }
        }

        System.out.println("Загружено аниме из Shikimori: " + allAnime.size());
        return allAnime;
    }

    private String fetchOneGenreForAnime(int animeId) {
        String url = BASE_URL + "/animes/" + animeId;

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "AnimeRecsApp/1.0")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 429) {
                Thread.sleep(5000);
                return fetchOneGenreForAnime(animeId);
            }

            if (!response.isSuccessful()) {
                return "Не указан";
            }

            String jsonData = response.body().string();
            JsonObject animeJson = gson.fromJson(jsonData, JsonObject.class);

            if (animeJson.has("genres") && !animeJson.get("genres").isJsonNull()) {
                JsonArray genresArray = animeJson.getAsJsonArray("genres");
                if (genresArray.size() > 0) {
                    JsonObject genreObj = genresArray.get(0).getAsJsonObject();
                    if (genreObj.has("russian") && !genreObj.get("russian").isJsonNull()) {
                        String genreName = genreObj.get("russian").getAsString();
                        if (genreName != null && !genreName.isEmpty()) {
                            return genreName;
                        }
                    }
                    if (genreObj.has("name") && !genreObj.get("name").isJsonNull()) {
                        String genreName = genreObj.get("name").getAsString();
                        if (genreName != null && !genreName.isEmpty()) {
                            return genreName;
                        }
                    }
                }
            }

            return "Не указан";
        } catch (Exception e) {
            return "Не указан";
        }
    }

    private String fetchDescriptionForAnime(int animeId) {
        String url = BASE_URL + "/animes/" + animeId;

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "AnimeRecsApp/1.0")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 429) {
                Thread.sleep(5000);
                return fetchDescriptionForAnime(animeId);
            }

            if (!response.isSuccessful()) {
                return "Описание отсутствует";
            }

            String jsonData = response.body().string();
            JsonObject animeJson = gson.fromJson(jsonData, JsonObject.class);

            if (animeJson.has("description") && !animeJson.get("description").isJsonNull()) {
                String description = animeJson.get("description").getAsString();
                if (description != null && !description.isEmpty()) {
                    description = description.replaceAll("\\[character=\\d+\\]", "");
                    description = description.replaceAll("\\[/character\\]", "");
                    description = description.replaceAll("\\[anime=\\d+\\]", "");
                    description = description.replaceAll("\\[/anime\\]", "");
                    description = description.replaceAll("\\[url=https?://[^\\]]+\\]", "");
                    description = description.replaceAll("\\[/url\\]", "");
                    description = description.replaceAll("\\[b\\]", "");
                    description = description.replaceAll("\\[/b\\]", "");
                    description = description.replaceAll("\\[i\\]", "");
                    description = description.replaceAll("\\[/i\\]", "");

                    description = description.replaceAll("\\[\\[(.*?)\\]\\]", "$1");

                    description = description.replaceAll("\\[(.*?)\\]", "$1");

                    description = description.replaceAll("<[^>]*>", "");

                    description = description.replaceAll("&nbsp;", " ");
                    description = description.replaceAll("&quot;", "\"");
                    description = description.replaceAll("&amp;", "&");
                    description = description.replaceAll("&lt;", "<");
                    description = description.replaceAll("&gt;", ">");

                    description = description.replaceAll("[\\u3040-\\u309F\\u30A0-\\u30FF\\u4E00-\\u9FFF]", "");

                    description = description.replaceAll("\\s*[（(][^)]*[）)]\\s*", " ");

                    description = description.replaceAll("\\n{3,}", "\n\n");

                    description = description.replaceAll("(?m)^\\s+", "");
                    description = description.replaceAll("(?m)\\s+$", "");

                    description = description.replaceAll("\\s{2,}", " ");

                    description = description.replaceAll("url=", "");

                    description = description.replaceAll("person=", "");

                    description = description.trim();

                    if (description.isEmpty()) {
                        return "Описание отсутствует";
                    }
                    return description;
                }
            }

            return "Описание отсутствует";
        } catch (Exception e) {
            return "Описание отсутствует";
        }
    }
}