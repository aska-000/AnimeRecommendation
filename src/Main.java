import database.BDAnime;
import ui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        try {
            BDAnime.connectionDB();
            BDAnime.initSchema();
            BDAnime.syncWithAPI();
            new LoginFrame();
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
            try {
                System.out.println("Использую статические данные...");
                BDAnime.seedData();
                new LoginFrame();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}