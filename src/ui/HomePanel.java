package ui;

import styles.UIStyles;
import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    public HomePanel(String username) {
        setLayout(new BorderLayout());
        setBackground(UIStyles.BACKGROUND);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(UIStyles.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        JPanel bannerPanel = new JPanel();
        bannerPanel.setLayout(new BorderLayout());
        bannerPanel.setBackground(UIStyles.BACKGROUND);

        try {
            ImageIcon bannerIcon = new ImageIcon("images/banner.jpg");
            Image bannerImage = bannerIcon.getImage();
            Image scaledBanner = bannerImage.getScaledInstance(1200, 200, Image.SCALE_SMOOTH);
            JLabel bannerLabel = new JLabel(new ImageIcon(scaledBanner));
            bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);
            bannerPanel.add(bannerLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel bannerFallback = new JLabel("AnimeRecs");
            bannerFallback.setForeground(Color.WHITE);
            bannerFallback.setFont(new Font("SansSerif", Font.BOLD, 36));
            bannerFallback.setHorizontalAlignment(SwingConstants.CENTER);
            bannerFallback.setBackground(UIStyles.PURPLE);
            bannerFallback.setOpaque(true);
            bannerFallback.setPreferredSize(new Dimension(1200, 200));
            bannerPanel.add(bannerFallback, BorderLayout.CENTER);
        }

        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(UIStyles.PANEL);
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        welcomePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel greetingLabel = new JLabel("Привет, " + username + "!");
        greetingLabel.setForeground(UIStyles.PURPLE);
        greetingLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        greetingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcomeText = new JLabel("Добро пожаловать в AnimeRecs!");
        welcomeText.setForeground(Color.WHITE);
        welcomeText.setFont(new Font("SansSerif", Font.BOLD, 22));
        welcomeText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel description = new JLabel("Здесь ты найдешь лучшие аниме, сможешь добавлять их в избранное");
        description.setForeground(Color.LIGHT_GRAY);
        description.setFont(new Font("SansSerif", Font.PLAIN, 16));
        description.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel description2 = new JLabel("и получать персональные рекомендации на основе просмотренных аниме!");
        description2.setForeground(Color.LIGHT_GRAY);
        description2.setFont(new Font("SansSerif", Font.PLAIN, 16));
        description2.setAlignmentX(Component.CENTER_ALIGNMENT);

        welcomePanel.add(greetingLabel);
        welcomePanel.add(Box.createVerticalStrut(20));
        welcomePanel.add(welcomeText);
        welcomePanel.add(Box.createVerticalStrut(15));
        welcomePanel.add(description);
        welcomePanel.add(Box.createVerticalStrut(5));
        welcomePanel.add(description2);

        mainPanel.add(bannerPanel, BorderLayout.NORTH);
        mainPanel.add(welcomePanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }
}