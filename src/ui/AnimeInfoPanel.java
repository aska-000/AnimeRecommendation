package ui;

import database.BDAnime;
import model.Anime;
import styles.ModernScrollBarUI;
import styles.UIStyles;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class AnimeInfoPanel extends JPanel {
    public AnimeInfoPanel(int userId, int animeId, boolean fromFavorites) {
        setLayout(new BorderLayout());
        setBackground(UIStyles.BACKGROUND);

        try {
            Anime anime = BDAnime.getAnimeById(animeId);
            BDAnime.markAsWatched(userId, animeId);

            JPanel bottomPanel = new JPanel();
            bottomPanel.setBackground(UIStyles.BACKGROUND);
            bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 25, 0));

            JButton back = UIStyles.createButton("Назад");
            back.addActionListener(e -> {
                String type = fromFavorites ? "favorites" : "all";
                MainFrame.switchPanel(new AnimeListPanel(userId, type));
            });

            if (!fromFavorites) {
                JButton fav = UIStyles.createButton("В избранное");
                fav.addActionListener(e -> {
                    try {
                        BDAnime.manageFavorite(userId, animeId, true);
                        JOptionPane.showMessageDialog(this, "Добавлено в избранное");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                bottomPanel.add(back);
                bottomPanel.add(fav);
            } else {
                JButton removeFav = UIStyles.createButton("Удалить из избранного");
                removeFav.setBackground(new Color(180, 60, 60));
                removeFav.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        removeFav.setBackground(new Color(200, 70, 70));
                    }
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        removeFav.setBackground(new Color(180, 60, 60));
                    }
                });
                removeFav.addActionListener(e -> {
                    try {
                        BDAnime.manageFavorite(userId, animeId, false);
                        JOptionPane.showMessageDialog(AnimeInfoPanel.this, "Удалено из избранного");
                        MainFrame.switchPanel(new AnimeListPanel(userId, "favorites"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                bottomPanel.add(back);
                bottomPanel.add(removeFav);
            }

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBackground(UIStyles.BACKGROUND);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 20, 50));

            JLabel title = new JLabel(anime.title);
            title.setForeground(Color.WHITE);
            title.setFont(new Font("SansSerif", Font.BOLD, 34));
            title.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel genre = new JLabel("Жанр: " + anime.genre);
            genre.setForeground(Color.LIGHT_GRAY);
            genre.setFont(new Font("SansSerif", Font.PLAIN, 16));
            genre.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel rating = new JLabel("★ " + anime.rating);
            rating.setForeground(UIStyles.PURPLE);
            rating.setFont(new Font("SansSerif", Font.BOLD, 22));
            rating.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Этот блок уже должен быть в вашем AnimeInfoPanel.java
// Найдите и убедитесь, что код выглядит так:

            JPanel imagePanel = new JPanel();
            imagePanel.setBackground(UIStyles.BACKGROUND);
            imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            try {
                String imagePath = anime.imagePath;
                System.out.println("Загружаю картинку: " + imagePath);
                ImageIcon imageIcon = new ImageIcon(new URL(imagePath));
                Image image = imageIcon.getImage();
                Image scaledImage = image.getScaledInstance(300, 400, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imagePanel.add(imageLabel);
            } catch (Exception e) {
                System.out.println("Ошибка загрузки картинки: " + e.getMessage());
                JLabel noImage = new JLabel("Нет изображения");
                noImage.setForeground(Color.GRAY);
                noImage.setFont(new Font("SansSerif", Font.PLAIN, 14));
                imagePanel.add(noImage);
            }

            JTextArea desc = new JTextArea(anime.description);
            desc.setLineWrap(true);
            desc.setWrapStyleWord(true);
            desc.setEditable(false);
            desc.setBackground(UIStyles.PANEL);
            desc.setForeground(Color.WHITE);
            desc.setFont(new Font("SansSerif", Font.PLAIN, 16));
            desc.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JScrollPane descScroll = new JScrollPane(desc);
            descScroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));
            descScroll.setBackground(UIStyles.PANEL);
            descScroll.setMaximumSize(new Dimension(800, 200));
            descScroll.setAlignmentX(Component.CENTER_ALIGNMENT);

            mainPanel.add(title);
            mainPanel.add(Box.createVerticalStrut(15));
            mainPanel.add(genre);
            mainPanel.add(Box.createVerticalStrut(15));
            mainPanel.add(rating);
            mainPanel.add(Box.createVerticalStrut(20));
            mainPanel.add(imagePanel);
            mainPanel.add(Box.createVerticalStrut(20));
            mainPanel.add(descScroll);

            JScrollPane scrollPane = new JScrollPane(mainPanel);
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.getVerticalScrollBar().setBackground(UIStyles.BACKGROUND);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            add(scrollPane, BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}