package styles;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UIStyles {
    public static final Color BACKGROUND = new Color(18, 18, 18);
    public static final Color PANEL = new Color(30, 30, 30);
    public static final Color PURPLE = new Color(139, 92, 246);
    public static final Color PURPLE_HOVER = new Color(168, 85, 247);
    public static final Color SIDEBAR = new Color(12, 12, 12);
    public static final Color TEXT = Color.WHITE;
    public static final Color BUTTON_GRAY = new Color(60, 60, 60);

    public static Font titleFont() {
        return new Font("SansSerif", Font.BOLD, 28);
    }

    public static Font normalFont() {
        return new Font("SansSerif", Font.PLAIN, 14);
    }

    public static Font smallFont() {
        return new Font("SansSerif", Font.PLAIN, 12);
    }

    public static JButton createButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(getBackground().darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(PURPLE);
                } else {
                    g2.setColor(getBackground());
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(BUTTON_GRAY);
        button.setForeground(TEXT);
        button.setFont(smallFont());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(220, 40));
        button.setOpaque(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PURPLE);
                button.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_GRAY);
                button.repaint();
            }
        });
        return button;
    }

    public static void styleTextField(JTextField field) {
        field.setBackground(new Color(45, 45, 45));
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        field.setFont(normalFont());
    }

    public static void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(new Color(45, 45, 45));
        comboBox.setForeground(Color.WHITE);
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (isSelected) {
                    setBackground(UIStyles.PURPLE);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(new Color(45, 45, 45));
                    setForeground(Color.WHITE);
                }

                if (index == -1) {
                    setBackground(new Color(45, 45, 45));
                    setForeground(Color.WHITE);
                }

                return this;
            }
        });

        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼");
                button.setBackground(new Color(45, 45, 45));
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.setFont(new Font("Arial", Font.PLAIN, 10));
                return button;
            }

            @Override
            protected void configureEditor() {
                super.configureEditor();
                if (editor != null) {
                    editor.setBackground(new Color(45, 45, 45));
                    editor.setForeground(Color.WHITE);
                }
            }
        });
    }

    public static JButton createPurpleButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(getBackground().darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(PURPLE);
                } else {
                    g2.setColor(getBackground());
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(BUTTON_GRAY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setMaximumSize(new Dimension(250, 45));
        button.setPreferredSize(new Dimension(250, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PURPLE);
                button.repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_GRAY);
                button.repaint();
            }
        });
        return button;
    }

    public static JButton createSidebarButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(getBackground().darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(PURPLE);
                } else {
                    g2.setColor(getBackground());
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(BUTTON_GRAY);
        button.setForeground(Color.WHITE);
        button.setFont(smallFont());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(240, 45));
        button.setPreferredSize(new Dimension(240, 45));
        button.setMinimumSize(new Dimension(240, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setOpaque(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(PURPLE);
                button.repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(BUTTON_GRAY);
                button.repaint();
            }
        });
        return button;
    }
}