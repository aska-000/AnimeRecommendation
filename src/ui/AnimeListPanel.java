package ui;

import database.BDAnime;
import model.Anime;
import styles.UIStyles;
import styles.ModernScrollBarUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AnimeListPanel extends JPanel {
    private JPanel listPanel;
    private ArrayList<Anime> allAnimeList;
    private int currentUserId;
    private String currentType;
    private JTextField searchField;
    private JComboBox<String> genreFilter;

    public AnimeListPanel(int userId, String type) {
        this.currentUserId = userId;
        this.currentType = type;
        setLayout(new BorderLayout());
        setBackground(UIStyles.BACKGROUND);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(UIStyles.BACKGROUND);
        topPanel.setLayout(new BorderLayout());

        JLabel title = new JLabel();
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        topPanel.add(title, BorderLayout.WEST);

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(UIStyles.BACKGROUND);
        searchPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 20));

        JLabel searchLabel = new JLabel("🔍");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));

        searchField = new JTextField("поиск", 15);
        searchField.setBackground(new Color(45, 45, 45));
        searchField.setForeground(Color.GRAY);
        searchField.setCaretColor(Color.WHITE);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("поиск")) {
                    searchField.setText("");
                    searchField.setForeground(Color.WHITE);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("поиск");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        JLabel genreLabel = new JLabel("Жанр:");
        genreLabel.setForeground(Color.WHITE);
        genreLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        genreFilter = new JComboBox<>();
        UIStyles.styleComboBox(genreFilter);
        genreFilter.setPreferredSize(new Dimension(130, 35));
        genreFilter.addItem("Все жанры");

        JButton searchButton = UIStyles.createButton("Найти");
        searchButton.setPreferredSize(new Dimension(100, 38));
        searchButton.addActionListener(e -> filterAnime());

        searchField.addActionListener(e -> filterAnime());
        genreFilter.addActionListener(e -> filterAnime());

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(genreLabel);
        searchPanel.add(genreFilter);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(searchButton);

        topPanel.add(searchPanel, BorderLayout.EAST);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(UIStyles.BACKGROUND);

        try {
            switch (type) {
                case "all":
                    title.setText("Все аниме");
                    allAnimeList = BDAnime.getAllAnime();
                    break;
                case "favorites":
                    title.setText("Избранное");
                    allAnimeList = BDAnime.getFavorites(userId);
                    break;
                case "recommendations":
                    title.setText("Рекомендации");
                    allAnimeList = BDAnime.getRecommendations(userId);
                    break;
            }

            Set<String> uniqueGenres = new HashSet<>();
            for (Anime anime : allAnimeList) {
                uniqueGenres.add(anime.genre);
            }
            for (String genre : uniqueGenres) {
                genreFilter.addItem(genre);
            }

            displayAnime(allAnimeList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(topPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private void filterAnime() {
        String query = searchField.getText().toLowerCase().trim();
        String selectedGenre = (String) genreFilter.getSelectedItem();

        ArrayList<Anime> filtered = new ArrayList<>(allAnimeList);

        if (!(query.isEmpty() || query.equals("поиск"))) {
            filtered = (ArrayList<Anime>) filtered.stream()
                    .filter(anime -> anime.title.toLowerCase().contains(query))
                    .collect(Collectors.toList());
        }

        if (selectedGenre != null && !selectedGenre.equals("Все жанры")) {
            filtered = (ArrayList<Anime>) filtered.stream()
                    .filter(anime -> anime.genre.equals(selectedGenre))
                    .collect(Collectors.toList());
        }

        displayAnime(filtered);
    }

    private void displayAnime(ArrayList<Anime> list) {
        listPanel.removeAll();

        if (list.isEmpty()) {
            listPanel.setLayout(new GridBagLayout());
            JLabel empty = new JLabel("Список пуст");
            empty.setForeground(Color.GRAY);
            empty.setFont(UIStyles.normalFont());
            listPanel.add(empty);
        } else {
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
            for (Anime anime : list) {
                AnimeCardPanel card = new AnimeCardPanel(anime);
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                card.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        MainFrame.switchPanel(new AnimeInfoPanel(currentUserId, anime.id, currentType.equals("favorites")));
                    }
                });
                listPanel.add(card);
                listPanel.add(Box.createVerticalStrut(15));
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }
}