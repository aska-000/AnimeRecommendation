import database.BDAnime;
import ui.MainFrame;

public class Main {
    public static void main(String[] args) {
        try {
            BDAnime.connectionDB();
            BDAnime.initSchema();
            BDAnime.seedData();
            new MainFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

