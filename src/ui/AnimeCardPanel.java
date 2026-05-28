package ui;

import model.Anime;
import styles.UIStyles;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AnimeCardPanel extends JPanel {
    public AnimeCardPanel(Anime anime) {
        setLayout(new BorderLayout());
        setBackground(UIStyles.PANEL);
        setBorder(new EmptyBorder(25, 25, 25, 25));
        setMaximumSize(new Dimension(800, 120));
        setPreferredSize(new Dimension(800, 120));

        JLabel title = new JLabel(anime.title);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel genre = new JLabel("Жанр: " + anime.genre);
        genre.setForeground(Color.LIGHT_GRAY);
        genre.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel rating = new JLabel("★ " + anime.rating);
        rating.setForeground(UIStyles.PURPLE);
        rating.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.WEST);
        top.add(rating, BorderLayout.EAST);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createVerticalStrut(15));
        center.add(genre);

        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
        g2.dispose();
        super.paintComponent(g);
    }
}