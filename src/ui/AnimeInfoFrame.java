package ui;

import database.BDAnime;
import model.Anime;

import javax.swing.*;
import java.awt.*;

public class AnimeInfoFrame extends JFrame {
    private int userId;
    private int animeId;

    public AnimeInfoFrame(int userId, int animeId, boolean showFavoriteButton) {
        this.userId = userId;
        this.animeId = animeId;

        setTitle("Информация об аниме");
        setSize(400, 300);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));
        JLabel labelTitle = new JLabel();
        JLabel labelGenre = new JLabel();
        JLabel labelRating = new JLabel();
        JTextArea areaDescription = new JTextArea();

        areaDescription.setLineWrap(true);
        areaDescription.setWrapStyleWord(true);
        areaDescription.setEditable(false);

        JButton buttonFavorite = new JButton("Добавить в избранное");
        buttonFavorite.setVisible(showFavoriteButton);
        JButton buttonBack = new JButton("Назад");

        panel.add(labelTitle);
        panel.add(labelGenre);
        panel.add(labelRating);
        panel.add(new JScrollPane(areaDescription));
        panel.add(buttonFavorite);
        panel.add(buttonBack);

        add(panel);

        try {
            BDAnime.markAsWatched(userId, animeId);
            Anime anime = BDAnime.getAnimeById(animeId);
            labelTitle.setText(anime.title);
            labelGenre.setText("Жанр: " + anime.genre);
            labelRating.setText("Рейтинг: " + anime.rating);
            areaDescription.setText(anime.description);
        } catch (Exception e) {
            e.printStackTrace();
        }

        buttonFavorite.addActionListener(e -> {
            try {
                BDAnime.manageFavorite(userId, animeId, true);
                JOptionPane.showMessageDialog(
                        null,
                        "Добавлено в избранное"
                );

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        buttonBack.addActionListener(e -> {
            dispose();
        });
        setVisible(true);
    }
}