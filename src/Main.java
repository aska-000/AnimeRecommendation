import database.BDAnime;
import ui.MainFrame;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            BDAnime.connectionDB();
            BDAnime.initSchema();
            BDAnime.seedData();

            // Если захотите запустить ваше графическое окно, раскомментируйте строку ниже:
            // new MainFrame();

            System.out.println("Система рекомендаций Аниме");
            System.out.print("Авторизация. Введите имя пользователя: ");
            String name = sc.nextLine();

            int currentUserId = BDAnime.checkUser(name);
            System.out.println("Добро пожаловать, " + name);

            while (true) {
                System.out.println("\nГЛАВНОЕ МЕНЮ");
                System.out.println("1. Общие рекомендации (Топ-10)");
                System.out.println("2. Персональные рекомендации");
                System.out.println("3. Начать просмотр");
                System.out.println("4. Избранное");
                System.out.println("0. Выход");

                int choice = sc.nextInt();

                if (choice == 1) {
                    BDAnime.getAllAnime();
                } else if (choice == 2) {
                    BDAnime.getRecommendations(currentUserId);
                } else if (choice == 3) {
                    System.out.print("Введите ID аниме для просмотра: ");
                    BDAnime.markAsWatched(currentUserId, sc.nextInt());
                    System.out.println("Просмотр зафиксирован. Рекомендации обновлены.");
                } else if (choice == 4) {

                    while (true) {
                        System.out.println("\nИзбранное");
                        System.out.println("1. Посмотреть мой список");
                        System.out.println("2. Добавить аниме по ID");
                        System.out.println("3. Удалить аниме по ID");
                        System.out.println("0. Назад");

                        int favChoice = sc.nextInt();
                        if (favChoice == 1) {
                            BDAnime.getFavorites(currentUserId);
                        } else if (favChoice == 2) {
                            System.out.print("Введите ID для добавления: ");
                            BDAnime.manageFavorite(currentUserId, sc.nextInt(), true);
                        } else if (favChoice == 3) {
                            System.out.print("Введите ID для удаления: ");
                            BDAnime.manageFavorite(currentUserId, sc.nextInt(), false);
                        } else if (favChoice == 0) break;
                    }
                } else if (choice == 0) break;
            }
            BDAnime.closeDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}