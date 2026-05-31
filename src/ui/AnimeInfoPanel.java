package ui;

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
            Anime anime = MainFrame.service.getAnimeById(animeId);
            if (anime == null) {
                JLabel error = new JLabel("Аниме не найдено");
                error.setForeground(Color.WHITE);
                add(error);
                return;
            }

            MainFrame.service.markAsWatched(userId, animeId);

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
                        MainFrame.service.addToFavorites(userId, animeId);
                        JOptionPane.showMessageDialog(AnimeInfoPanel.this, "Добавлено в избранное");
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
                        MainFrame.service.removeFromFavorites(userId, animeId);
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

            JLabel genre = new JLabel("Жанр: " + (anime.genre != null ? anime.genre : "Не указан"));
            genre.setForeground(Color.LIGHT_GRAY);
            genre.setFont(new Font("SansSerif", Font.PLAIN, 16));
            genre.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel rating = new JLabel("★ " + anime.rating);
            rating.setForeground(UIStyles.PURPLE);
            rating.setFont(new Font("SansSerif", Font.BOLD, 22));
            rating.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel imagePanel = new JPanel();
            imagePanel.setBackground(UIStyles.BACKGROUND);
            imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            String imagePath = anime.imagePath;
            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    ImageIcon imageIcon;
                    if (imagePath.startsWith("http")) {
                        imageIcon = new ImageIcon(new URL(imagePath));
                    } else {
                        imageIcon = new ImageIcon(imagePath);
                    }
                    Image image = imageIcon.getImage();
                    if (image != null) {
                        Image scaledImage = image.getScaledInstance(300, 400, Image.SCALE_SMOOTH);
                        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        imagePanel.add(imageLabel);
                    } else {
                        addNoImageLabel(imagePanel);
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка загрузки картинки: " + e.getMessage());
                    addNoImageLabel(imagePanel);
                }
            } else {
                addNoImageLabel(imagePanel);
            }

            JTextArea desc = new JTextArea();
            desc.setLineWrap(true);
            desc.setWrapStyleWord(true);
            desc.setEditable(false);
            desc.setBackground(UIStyles.PANEL);
            desc.setForeground(Color.WHITE);
            desc.setFont(new Font("SansSerif", Font.PLAIN, 16));
            desc.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            String descriptionText = anime.description;
            if (descriptionText == null || descriptionText.isEmpty()) {
                descriptionText = "Описание отсутствует";
            }
            desc.setText(descriptionText);

            JScrollPane descScroll = new JScrollPane(desc);
            descScroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));
            descScroll.setBackground(UIStyles.PANEL);
            descScroll.setMaximumSize(new Dimension(800, 300));
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
            JLabel error = new JLabel("Ошибка загрузки: " + e.getMessage());
            error.setForeground(Color.RED);
            add(error);
        }
    }

    private void addNoImageLabel(JPanel panel) {
        JLabel noImage = new JLabel("Нет изображения");
        noImage.setForeground(Color.GRAY);
        noImage.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(noImage);
    }
}