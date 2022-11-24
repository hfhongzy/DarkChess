package ButtonComponent;

import javax.swing.*;
import java.awt.*;

public class gameButton extends JComponent {
    final int WIDTH, HEIGHT;
    String name;
    public gameButton(int width, int height, String name) {
        WIDTH = width;
        HEIGHT = height;
        this.name = name;

        setSize(WIDTH, HEIGHT);
    }

    void paintFrame(Graphics2D g) {
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0x377e7f));
        g2.fillRect(2, 2, WIDTH - 2, HEIGHT - 2);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, WIDTH - 2, HEIGHT - 2);
        /*
        g2.setColor(Color.GREEN);
        g2.fill3DRect(0, 0, WIDTH - 2, HEIGHT - 5, false);
         */
    }
}
