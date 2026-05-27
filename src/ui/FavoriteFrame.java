package ui;

import database.BDAnime;
import model.Anime;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FavoriteFrame extends JFrame {
    private JList<Anime> animeList;
    private JButton buttonOpen;
    private JButton buttonDelete;
    private JButton buttonBack;
    private DefaultListModel<Anime> model;
    private int userId;

    public FavoriteFrame(int userId) {
        this.userId = userId;
        setTitle("Избранное");
        setSize(500, 400);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        model = new DefaultListModel<>();
        animeList = new JList<>(model);

        JScrollPane scrollPane = new JScrollPane(animeList);
        JPanel buttonPanel = new JPanel();

        buttonOpen = new JButton("Открыть");
        buttonDelete = new JButton("Удалить");
        buttonBack = new JButton("Назад");

        buttonPanel.add(buttonOpen);
        buttonPanel.add(buttonDelete);
        buttonPanel.add(buttonBack);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        add(panel);
        loadFavorites();

        buttonBack.addActionListener(e -> {
            dispose();
        });

        buttonDelete.addActionListener(e -> {
            Anime selected = animeList.getSelectedValue();

            if (selected == null) {
                return;
            }
            try {
                BDAnime.manageFavorite(userId, selected.id, false);
                loadFavorites();
                JOptionPane.showMessageDialog(
                        null,
                        "Удалено из избранного"
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
            new AnimeInfoFrame(userId, selected.id, false);
        });
        setVisible(true);
    }


    private void loadFavorites() {
        try {
            ArrayList<Anime> list = BDAnime.getFavorites(userId);
            model.clear();
            for (Anime anime : list) {
                model.addElement(anime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
