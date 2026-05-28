import database.BDAnime;
import ui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        try {
            BDAnime.connectionDB();
            BDAnime.initSchema();
            BDAnime.seedData();
            new LoginFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


