package radioButtonComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameRadioButton extends JComponent {
    final int WIDTH, HEIGHT;
    String name;
    boolean isSelected;
    private boolean isEnter;
    private boolean isPressed;
    private static final Font BUTTON_FONT = new Font("宋体", Font.PLAIN, 18);
    private static final Font BUTTON_BOLD_FONT = new Font("宋体", Font.BOLD, 18);
    public GameRadioButton(int width, int height, String name, boolean selected, GameRadioButtonEvent gameRadioButtonEvent) {
        WIDTH = width;
        HEIGHT = height;
        isSelected = selected;
        this.name = name;
        setSize(width, height);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                int X = e.getX(), Y = e.getY();
                if (X >= 0 && X <= getWidth() && Y >= 0 && Y <= getHeight()) {
                    isSelected = !isSelected;
                    if (gameRadioButtonEvent != null) {
                        if (isSelected)
                            gameRadioButtonEvent.selectClick();
                        else
                            gameRadioButtonEvent.unselectClick();
                    }
                }
                repaint();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                isEnter = true;
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                isEnter = false;
                repaint();
            }
        });
    }
    public boolean isSelected() {
        return isSelected;
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0x377e7f));
        g2.fillRect(2, 2, WIDTH - 2, HEIGHT - 2);
        g2.setColor(isPressed ? Color.LIGHT_GRAY : Color.WHITE);
        int delta = isPressed ? 1 : 0;
        g2.fillRect(delta, delta, WIDTH - 2, HEIGHT - 2);
        g2.fillRect(delta, delta, WIDTH - 2, HEIGHT - 2);
        Font font = isEnter ? BUTTON_BOLD_FONT : BUTTON_FONT;
        g2.setFont(font);
        FontMetrics fontMetrics = getFontMetrics(font);
        int deltaWidth = -fontMetrics.stringWidth(name) / 2;
        int deltaHeight = fontMetrics.getAscent() - fontMetrics.getHeight() / 2;
        g2.setColor(new Color(0x377e7f));
        g2.drawString(name, WIDTH / 2 + deltaWidth + delta, HEIGHT / 2 + deltaHeight + delta);
    }
}
