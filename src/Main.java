import service.AnimeService;
import ui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        try {
            AnimeService service = new AnimeService();
            service.initDatabase();
            new LoginFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}