package gameComponent;

import labelComponent.GameLabel;
import labelComponent.TextBlock;
import model.TeamColor;

import javax.swing.*;
import java.awt.*;

public class PlayerStatus extends JComponent {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;
    int red_score, black_score;
    TeamColor currentColor;
    GameLabel messageLabel, scoreLabel;
    public PlayerStatus() {
        setLayout(null);
        setSize(WIDTH, HEIGHT);

        messageLabel = new GameLabel();
        scoreLabel = new GameLabel();
        add(messageLabel);
        add(scoreLabel);
    }
    public void start() {
        red_score = black_score = 0;
        currentColor = null;
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
        if (currentColor == null)
            messageLabel.setText(new TextBlock("翻转一个棋子决定红黑方", Color.GRAY, 20));
        else
            messageLabel.setText(new TextBlock("轮到 ", Color.GRAY, 20), new TextBlock(currentColor.getName() + "色方", currentColor.getColor(), 30));
        scoreLabel.setText(new TextBlock("红色方 " + red_score, Color.RED, 30),
                           new TextBlock(" : ", Color.GRAY, 25),
                           new TextBlock(black_score + " 黑色方", Color.BLACK, 30));
        messageLabel.setLocation(150 - messageLabel.getWidth() / 2, 80 - messageLabel.getHeight() / 2);
        scoreLabel.setLocation(150 - scoreLabel.getWidth() / 2, 150 - scoreLabel.getHeight() / 2);
    }
}
