package ButtonComponent;

import javax.swing.*;
import java.awt.*;

public class GameButton extends JComponent {
    final int WIDTH, HEIGHT;
    String name;
    private static final Font BUTTON_FONT = new Font("宋体", Font.PLAIN, 18);
    public GameButton(int width, int height, String name) {
        WIDTH = width;
        HEIGHT = height;
        this.name = name;

        setSize(WIDTH, HEIGHT);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0x377e7f));
        g2.fillRect(2, 2, WIDTH - 2, HEIGHT - 2);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, WIDTH - 2, HEIGHT - 2);
        g2.setFont(BUTTON_FONT);
        FontMetrics fontMetrics = getFontMetrics(BUTTON_FONT);
        int deltaWidth = -fontMetrics.stringWidth(name) / 2;
        int deltaHeight = fontMetrics.getAscent() - fontMetrics.getHeight() / 2;
        g2.setColor(new Color(0x377e7f));
        g2.drawString(name, WIDTH / 2 + deltaWidth, HEIGHT / 2 + deltaHeight);
    }
}
