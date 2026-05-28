package ui;

import styles.UIStyles;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public static JPanel contentPanel;
    public static int currentUserId;
    public static String currentUsername;

    public MainFrame(int userId, String username) {
        currentUserId = userId;
        currentUsername = username;

        setTitle("AnimeRecs");
        setSize(1600, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIStyles.BACKGROUND);
        add(contentPanel, BorderLayout.CENTER);

        switchPanel(new HomePanel(username));
        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(280, getHeight()));
        panel.setBackground(UIStyles.SIDEBAR);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JLabel animeLabel = new JLabel("Anime");
        animeLabel.setForeground(Color.WHITE);
        animeLabel.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel recsLabel = new JLabel("Recs");
        recsLabel.setForeground(UIStyles.PURPLE);
        recsLabel.setFont(new Font("SansSerif", Font.BOLD, 28));

        titlePanel.add(animeLabel);
        titlePanel.add(recsLabel);

        JLabel logoSub = new JLabel("твои аниме - твои рекомендации");
        logoSub.setForeground(Color.GRAY);
        logoSub.setFont(new Font("SansSerif", Font.PLAIN, 10));
        logoSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(titlePanel);
        logoPanel.add(logoSub);

        JLabel greeting = new JLabel("Привет, " + currentUsername + "!");
        greeting.setForeground(UIStyles.PURPLE);
        greeting.setFont(new Font("SansSerif", Font.BOLD, 14));
        greeting.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(30));
        panel.add(logoPanel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(greeting);
        panel.add(Box.createVerticalStrut(30));

        panel.add(Box.createVerticalGlue());

        JButton all = UIStyles.createSidebarButton("Все аниме");
        JButton rec = UIStyles.createSidebarButton("Рекомендации");
        JButton fav = UIStyles.createSidebarButton("Избранное");
        JButton switchUser = UIStyles.createSidebarButton("Сменить пользователя");
        JButton logout = UIStyles.createSidebarButton("Выйти");

        all.addActionListener(e -> switchPanel(new AnimeListPanel(currentUserId, "all")));
        rec.addActionListener(e -> switchPanel(new AnimeListPanel(currentUserId, "recommendations")));
        fav.addActionListener(e -> switchPanel(new AnimeListPanel(currentUserId, "favorites")));
        switchUser.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
        logout.addActionListener(e -> {
            System.exit(0);
        });

        panel.add(all);
        panel.add(Box.createVerticalStrut(10));
        panel.add(rec);
        panel.add(Box.createVerticalStrut(10));
        panel.add(fav);
        panel.add(Box.createVerticalStrut(10));
        panel.add(switchUser);
        panel.add(Box.createVerticalStrut(10));
        panel.add(logout);

        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalStrut(50));

        return panel;
    }

    public static void switchPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}