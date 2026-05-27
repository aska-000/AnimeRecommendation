package database;

import model.Anime;
import java.sql.*;
import java.util.ArrayList;

public class BDAnime {
    public static Connection conn;
    public static Statement stat;
    public static ResultSet rs;
    private static ArrayList<Anime> list = new ArrayList<>();


    public static void connectionDB() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:anime_data.db");
        System.out.println("БД подключена!");
    }

    public static void initSchema() throws SQLException {
        stat = conn.createStatement();
        stat.execute("CREATE TABLE IF NOT EXISTS anime (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, genre TEXT, rating REAL, description TEXT);");
        stat.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE);");
        stat.execute("CREATE TABLE IF NOT EXISTS activity (" +
                "user_id INTEGER, anime_id INTEGER, is_fav BOOLEAN DEFAULT 0, watched BOOLEAN DEFAULT 0, " +
                "PRIMARY KEY(user_id, anime_id));");
    }

    public static void seedData() throws SQLException {

        stat.execute("DELETE FROM anime;");
        stat.execute("DELETE FROM sqlite_sequence WHERE name='anime';");

        String sql = "INSERT INTO anime (title, genre, rating, description) VALUES (?, ?, ?, ?)";
        String[][] items = {
                {"Наруто", "Шонен", "8.3", "История ниндзя Наруто Узумаки, мечтающего стать Хокаге."},
                {"Блич", "Шонен", "8.2", "Школьник Ичиго получает силы проводника душ."},
                {"Твое имя", "Романтика", "8.9", "Парень и девушка mysteriously меняются телами."},
                {"Монстр", "Триллер", "8.7", "Доктор пытается остановить опасного серийного убийцу."},
                {"Семья шпиона", "Комедия", "8.5", "Шпион создает фальшивую семью ради миссии."},
                {"Атака титанов", "Экшен", "9.0", "Человечество борется против гигантских титанов."},
                {"Ван-Пис", "Приключения", "8.8", "Луффи ищет легендарное сокровище One Piece."},
                {"Магическая битва", "Экшен", "8.7", "Юдзи Итадори становится сосудом проклятия."},
                {"Класс убийц", "Комедия", "8.0", "Ученики должны убить своего учителя."},
                {"Тетрадь смерти", "Триллер", "8.6", "Тетрадь позволяет убивать людей по имени."},
                {"Доктор Стоун", "Приключения", "8.4", "Наука помогает восстановить цивилизацию."},
                {"Клинок рассекающий демонов", "Экшен", "8.8", "Танджиро становится охотником на демонов."},
                {"Форма голоса", "Драма", "8.9", "История дружбы и искупления школьников."},
                {"Токийский гуль", "Триллер", "8.1", "Парень становится наполовину гулем."},
                {"Врата Штейна", "Фантастика", "9.1", "Эксперименты со временем приводят к последствиям."},
                {"Хантер х Хантер", "Приключения", "9.0", "Гон отправляется искать своего отца."},
                {"Код Гиас", "Меха", "8.9", "Лелуш получает силу абсолютного приказа."},
                {"Торадора", "Романтика", "8.2", "Романтическая история двух школьников."},
                {"Черный клевер", "Фэнтези", "8.0", "Аста мечтает стать королем магов."},
                {"Сага о Винланде", "История", "8.8", "История викингов и мести Торфинна."}
        };
        PreparedStatement ps = conn.prepareStatement(sql);
        for (String[] a : items) {
            ps.setString(1, a[0]); ps.setString(2, a[1]); ps.setDouble(3, Double.parseDouble(a[2])); ps.setString(4, a[3]);
            ps.executeUpdate();
        }
    }

    public static int checkUser(String name) throws SQLException {
        String insertSql = "INSERT OR IGNORE INTO users (username) VALUES (?)";
        PreparedStatement psIns = conn.prepareStatement(insertSql);
        psIns.setString(1, name);
        psIns.executeUpdate();

        String selectSql = "SELECT id FROM users WHERE username = ?";
        PreparedStatement psSel = conn.prepareStatement(selectSql);
        psSel.setString(1, name);
        ResultSet userRs = psSel.executeQuery();
        return userRs.next() ? userRs.getInt("id") : 1;
    }

    public static void manageFavorite(int userId, int animeId, boolean add) throws SQLException {
        String sql = "INSERT INTO activity (user_id, anime_id, is_fav) VALUES (?, ?, ?) " +
                "ON CONFLICT(user_id, anime_id) DO UPDATE SET is_fav = excluded.is_fav";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId); ps.setInt(2, animeId); ps.setInt(3, add ? 1 : 0);
        ps.executeUpdate();
    }

    public static void markAsWatched(int userId, int animeId) throws SQLException {
        String sql = "INSERT INTO activity (user_id, anime_id, watched) VALUES (?, ?, 1) " +
                "ON CONFLICT(user_id, anime_id) DO UPDATE SET watched = 1";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId); ps.setInt(2, animeId);
        ps.executeUpdate();
    }

    public static void closeDB() throws SQLException {
        if (rs != null) rs.close();
        if (stat != null) stat.close();
        if (conn != null) conn.close();
    }

    public static ArrayList<Anime> getAllAnime() throws SQLException {
        ArrayList<Anime> list = new ArrayList<>();
        rs = stat.executeQuery("SELECT * FROM anime LIMIT 20");

        while (rs.next()) {
            list.add(new Anime(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getDouble("rating"),
                    rs.getString("description")
            ));
        }
        return list;
    }

    public static Anime getAnimeById(int id) throws SQLException {
        String sql = "SELECT * FROM anime WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Anime(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getDouble("rating"),
                    rs.getString("description")
            );
        }
        return null;
    }

    public static ArrayList<Anime> getFavorites(int userId) throws SQLException {
        ArrayList<Anime> list = new ArrayList<>();
        String sql = "SELECT a.* FROM anime a " +
                "JOIN activity act ON a.id = act.anime_id " +
                "WHERE act.user_id = ? AND act.is_fav = 1";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new Anime(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getDouble("rating"),
                    rs.getString("description")
            ));
        }
        return list;
    }

    public static ArrayList<Anime> getRecommendations(int userId) throws SQLException {
        ArrayList<Anime> list = new ArrayList<>();
        String sql = "SELECT * FROM anime WHERE genre IN (" +
                "SELECT genre FROM anime a " +
                "JOIN activity act ON a.id = act.anime_id " +
                "WHERE act.user_id = ? AND act.watched = 1) " +
                "AND id NOT IN (" +
                "SELECT anime_id FROM activity " +
                "WHERE user_id = ? AND watched = 1) " +
                "LIMIT 20";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setInt(2, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new Anime(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getDouble("rating"),
                    rs.getString("description")
            ));
        }
        return list;
    }
}
