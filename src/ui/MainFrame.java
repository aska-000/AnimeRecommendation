package ui;

import database.BDAnime;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MainFrame extends JFrame {
    private JTextField fieldName;
    private JButton buttonLogin;
    private JButton buttonAllAnime;
    private JButton buttonRecommendations;
    private JButton buttonFavorites;
    private JButton buttonChangeUser;

    private int currentUserId;

    public MainFrame() {
        setTitle("Система рекомендаций аниме");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1, 10, 10));
        JLabel title = new JLabel("Система рекомендаций аниме");
        title.setHorizontalAlignment(SwingConstants.CENTER);

        fieldName = new JTextField();
        buttonLogin = new JButton("Войти");
        buttonAllAnime = new JButton("Общие рекомендации");
        buttonRecommendations = new JButton("Персональные рекомендации");
        buttonFavorites = new JButton("Избранное");
        buttonChangeUser = new JButton("Сменить пользователя");
        buttonAllAnime.setEnabled(false);
        buttonRecommendations.setEnabled(false);
        buttonFavorites.setEnabled(false);
        buttonChangeUser.setEnabled(false);

        panel.add(title);
        panel.add(fieldName);
        panel.add(buttonLogin);
        panel.add(buttonAllAnime);
        panel.add(buttonRecommendations);
        panel.add(buttonFavorites);
        panel.add(buttonChangeUser);
        add(panel);

        buttonLogin.addActionListener(e -> {
            String name = fieldName.getText();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Введите имя");
                return;
            }

            try {
                currentUserId = BDAnime.checkUser(name);
                JOptionPane.showMessageDialog(
                        null,
                        "Добро пожаловать, " + name
                );

                buttonAllAnime.setEnabled(true);
                buttonRecommendations.setEnabled(true);
                buttonFavorites.setEnabled(true);
                buttonChangeUser.setEnabled(true);

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });


        buttonAllAnime.addActionListener(e -> {
            new AllAnimeFrame(currentUserId);
        });

        buttonChangeUser.addActionListener(e -> {
            currentUserId = 0;
            fieldName.setText("");
            buttonAllAnime.setEnabled(false);
            buttonRecommendations.setEnabled(false);
            buttonFavorites.setEnabled(false);
            buttonChangeUser.setEnabled(false);
            JOptionPane.showMessageDialog(
                    null,
                    "Пользователь сброшен"
            );
        });
        setVisible(true);

        buttonFavorites.addActionListener(e -> {
            new FavoriteFrame(currentUserId);
        });

        buttonRecommendations.addActionListener(e -> {
            new RecommendationFrame(currentUserId);
        });
    }
}