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
        stat.execute("CREATE TABLE IF NOT EXISTS anime (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, genre TEXT, rating REAL);");
        stat.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE);");
        stat.execute("CREATE TABLE IF NOT EXISTS activity (" +
                "user_id INTEGER, anime_id INTEGER, is_fav BOOLEAN DEFAULT 0, watched BOOLEAN DEFAULT 0, " +
                "PRIMARY KEY(user_id, anime_id));");
    }

    public static void seedData() throws SQLException {

        stat.execute("DELETE FROM anime;");
        stat.execute("DELETE FROM sqlite_sequence WHERE name='anime';");

        String sql = "INSERT INTO anime (title, genre, rating) VALUES (?, ?, ?)";
        String[][] items = {
                {"Наруто", "Шонен", "8.3"}, {"Блич", "Шонен", "8.2"}, {"Твое имя", "Романтика", "8.9"},
                {"Монстр", "Триллер", "8.7"}, {"Семья шпиона", "Комедия", "8.5"}, {"Атака титанов", "Экшен", "9.0"},
                {"Ван-Пис", "Приключения", "8.8"}, {"Магическая битва", "Экшен", "8.7"},
                {"Класс убийц", "Комедия", "8.0"}, {"Тетрадь смерти", "Триллер", "8.6"}
        };
        PreparedStatement ps = conn.prepareStatement(sql);
        for (String[] a : items) {
            ps.setString(1, a[0]); ps.setString(2, a[1]); ps.setDouble(3, Double.parseDouble(a[2]));
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

    public static void showAllAnime() throws SQLException {
        System.out.println("\nОбщие рекомендации (ТОП-10)");
        rs = stat.executeQuery("SELECT * FROM anime LIMIT 10");
        while(rs.next()) {
            System.out.println(new Anime(rs.getInt("id"), rs.getString("title"), rs.getString("genre"), rs.getDouble("rating")));
        }
    }

    public static void getPersonalRecommendations(int userId) throws SQLException {
        list.clear();
        String sql = "SELECT * FROM anime WHERE genre IN (" +
                "SELECT genre FROM anime a JOIN activity act ON a.id = act.anime_id " +
                "WHERE act.user_id = ? AND act.watched = 1) " +
                "AND id NOT IN (SELECT anime_id FROM activity WHERE user_id = ? AND watched = 1) LIMIT 10";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setInt(2, userId);
        rs = ps.executeQuery();

        while(rs.next()) {
            list.add(new Anime(rs.getInt("id"), rs.getString("title"), rs.getString("genre"), rs.getDouble("rating")));
        }

        if (list.isEmpty()) {
            System.out.println("\n[!] Персональный список пока пуст. Посмотрите что-нибудь из общего списка!");
        } else {
            System.out.println("\nПерсональные рекомендации");
            for (Anime a : list) System.out.println(a.toString());
        }
    }

    public static void showFavorites(int userId) throws SQLException {
        String sql = "SELECT a.* FROM anime a JOIN activity act ON a.id = act.anime_id WHERE act.user_id = ? AND act.is_fav = 1";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet favRs = ps.executeQuery();
        System.out.println("\nИзбранное");
        boolean empty = true;
        while(favRs.next()) {
            empty = false;
            System.out.println(new Anime(favRs.getInt("id"), favRs.getString("title"), favRs.getString("genre"), favRs.getDouble("rating")));
        }
        if (empty) System.out.println("Список пуст.");
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
}