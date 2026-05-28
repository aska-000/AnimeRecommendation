package styles;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class ModernScrollBarUI extends BasicScrollBarUI {
    @Override
    protected void configureScrollBarColors() {
        thumbColor = UIStyles.PURPLE;
    }
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }
    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }
    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        return button;
    }
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(UIStyles.BACKGROUND);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(UIStyles.PURPLE);
        g2.fillRoundRect(thumbBounds.x + 4, thumbBounds.y, thumbBounds.width - 8, thumbBounds.height, 10, 10);
    }
}