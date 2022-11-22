package gameComponent;

import model.TeamColor;

import javax.swing.*;
import java.awt.*;

public class PlayerStatus extends JComponent {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 600;
    int red_score, black_score;
    TeamColor currentColor;
    public PlayerStatus() {
        setLayout(null);
        setSize(WIDTH, HEIGHT);

        currentColor = null;
        red_score = black_score = 0;
    }

    public TeamColor getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(TeamColor currentColor) {
        this.currentColor = currentColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (currentColor == null) {
            g2.setColor(Color.BLACK);
            g2.drawString("请先选一个棋子", 100, 50);
        } else {
            g2.setColor(currentColor.getColor());
            g2.drawString(String.format("%s色方", currentColor.getName()), 100, 50);
        }
        g2.setColor(Color.GREEN);
        g2.drawString(String.format("RED %d : %d BLACK", red_score, black_score), 100, 200);
    }
}
