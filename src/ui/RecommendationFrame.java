package ui;

import database.BDAnime;
import model.Anime;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RecommendationFrame extends JFrame {
    private JList<Anime> animeList;
    private JButton buttonOpen;
    private JButton buttonFavorite;
    private JButton buttonBack;
    private DefaultListModel<Anime> model;
    private int userId;

    public RecommendationFrame(int userId) {
        this.userId = userId;
        setTitle("Персональные рекомендации");
        setSize(500, 400);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        model = new DefaultListModel<>();
        animeList = new JList<>(model);

        JScrollPane scrollPane = new JScrollPane(animeList);
        JPanel buttonPanel = new JPanel();

        buttonOpen = new JButton("Открыть");
        buttonFavorite = new JButton("В избранное");
        buttonBack = new JButton("Назад");

        buttonPanel.add(buttonOpen);
        buttonPanel.add(buttonFavorite);
        buttonPanel.add(buttonBack);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        add(panel);
        loadRecommendations();

        buttonBack.addActionListener(e -> {
            dispose();
        });

        buttonFavorite.addActionListener(e -> {
            Anime selected = animeList.getSelectedValue();
            if (selected == null) {
                return;
            }
            try {
                BDAnime.manageFavorite(userId, selected.id, true);
                JOptionPane.showMessageDialog(
                        null,
                        "Добавлено в избранное"
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        buttonOpen.addActionListener(e -> {
            Anime selected = animeList.getSelectedValue();
            if (selected == null) {
                return;
            }
            new AnimeInfoFrame(userId, selected.id, true);
        });
        setVisible(true);
    }

    private void loadRecommendations() {
        try {
            ArrayList<Anime> list = BDAnime.getRecommendations(userId);
            model.clear();
            for (Anime anime : list) {
                model.addElement(anime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}