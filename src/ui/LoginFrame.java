package ui;

import database.BDAnime;
import styles.UIStyles;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("AnimeRecs");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setBackground(UIStyles.BACKGROUND);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel titleRow = new JPanel();
        titleRow.setOpaque(false);
        titleRow.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JLabel animeLabel = new JLabel("Anime");
        animeLabel.setForeground(Color.WHITE);
        animeLabel.setFont(new Font("SansSerif", Font.BOLD, 40));

        JLabel recsLabel = new JLabel("Recs");
        recsLabel.setForeground(UIStyles.PURPLE);
        recsLabel.setFont(new Font("SansSerif", Font.BOLD, 40));

        titleRow.add(animeLabel);
        titleRow.add(recsLabel);

        JLabel subtitle = new JLabel("твои аниме - твои рекомендации");
        subtitle.setForeground(Color.LIGHT_GRAY);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(titleRow);
        titlePanel.add(subtitle);

        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(300, 45));
        UIStyles.styleTextField(field);

        JButton login = UIStyles.createPurpleButton("Войти");
        login.setAlignmentX(Component.CENTER_ALIGNMENT);
        login.addActionListener(e -> {
            try {
                String name = field.getText();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Введите имя пользователя");
                    return;
                }
                int userId = BDAnime.checkUser(name);
                new MainFrame(userId, name);
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        panel.add(Box.createVerticalGlue());
        panel.add(titlePanel);
        panel.add(Box.createVerticalStrut(40));
        panel.add(field);
        panel.add(Box.createVerticalStrut(30));
        panel.add(login);
        panel.add(Box.createVerticalGlue());

        add(panel);
        setVisible(true);
    }
}