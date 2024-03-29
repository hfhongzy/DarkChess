package buttonComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameButton extends JComponent {
    final int WIDTH, HEIGHT;
    
    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    String name;
    private boolean isEnter;
    private boolean isPressed;
    boolean isWorking;
    private static final Font BUTTON_FONT = new Font("宋体", Font.PLAIN, 18);
    private static final Font BUTTON_BOLD_FONT = new Font("宋体", Font.BOLD, 18);
    public GameButton(int width, int height, String name, GameButtonEvent gameButtonEvent) {
        WIDTH = width;
        HEIGHT = height;
        isEnter = false;
        isPressed = false;
        this.name = name;
        isWorking = true;
        setSize(WIDTH, HEIGHT);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                if (isWorking)
                    repaint();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                if (isWorking) {
                    int X = e.getX(), Y = e.getY();
                    if (gameButtonEvent != null && X >= 0 && X <= getWidth() && Y >= 0 && Y <= getHeight())
                        gameButtonEvent.onClick();
                    repaint();
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                isEnter = true;
                if (isWorking) {
                    repaint();
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                isEnter = false;
                if (isWorking) {
                    repaint();
                }
            }
        });
    }

    public void setWorking(boolean working) {
        isWorking = working;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (isWorking) {
            g2.setColor(new Color(0x377e7f));
            g2.fillRect(2, 2, WIDTH - 2, HEIGHT - 2);
        }
        else {
            g2.setColor(Color.GRAY);
            g2.fillRect(1, 1, WIDTH - 1, HEIGHT - 1);
        }
        g2.setColor(isWorking && isPressed ? Color.LIGHT_GRAY : Color.WHITE);
        int delta = isWorking && isPressed ? 1 : 0;
        if (isWorking) {
            g2.fillRect(delta, delta, WIDTH - 2, HEIGHT - 2);
        }
        Font font = isWorking && isEnter ? BUTTON_BOLD_FONT : BUTTON_FONT;
        g2.setFont(font);
        FontMetrics fontMetrics = getFontMetrics(font);
        int deltaWidth = -fontMetrics.stringWidth(name) / 2;
        int deltaHeight = fontMetrics.getAscent() - fontMetrics.getHeight() / 2;
        g2.setColor(isWorking ? new Color(0x377e7f) : Color.BLACK);
        g2.drawString(name, WIDTH / 2 + deltaWidth + delta, HEIGHT / 2 + deltaHeight + delta);
    }
}
