package chessComponent;

import model.TeamColor;

import javax.swing.*;
import java.awt.*;
//import java.awt.*;

public abstract class ChessComponent extends JComponent {
    private boolean isReversal;
    private boolean isSelected;
    private boolean isReachable;
    private boolean isEaten;
    private boolean isCheating;
    private boolean isCount;
    int count;
    int X, Y;
    protected TeamColor teamColor;
    private final int width, spacingLength, deltaWidth;
    private static final int SELECTED_SPACING_LENGTH = 3;
    private static final int COUNT_SIZE = 20;
    private static final Font CHESS_FONT = new Font("Rockwell", Font.BOLD, 36);
    private static final Font COUNT_FONT = new Font("宋体", Font.PLAIN, 14);
    protected String name;
    int ID;
    final int score;
    
    public void clearCount() {
        count = 0;
    }
    
    public int getID() {
        return ID;
    }

    public int getScore() {
        return score;
    }

    public boolean isEaten() {
        return isEaten;
    }
    public boolean isReversal() {
        return isReversal;
    }
    public boolean isReachable() {
        return isReachable;
    }
    public TeamColor getTeamColor() {
        return teamColor;
    }
    public int X() { return X; }
    public int Y() { return Y; }
    public void setEaten(boolean eaten) {
        isEaten = eaten;
    }
    public void setReversal(boolean reversal) {
        isReversal = reversal;
    }
    public void setReachable(boolean reachable) {
        isReachable = reachable;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setCount(boolean count) {
        isCount = count;
    }
    public void addCount(int delta) {
        count += delta;
    }

    public void setX(int x) {
        X = x;
    }
    public void setY(int y) {
        Y = y;
    }
    ChessComponent(int width, int score) {
        isReversal = true;
        isEaten = false;
        isReachable = false;
        isCheating = false;
        count = 0;
        spacingLength = width / 10;
        deltaWidth = spacingLength / 2;
        this.score = score;
        isCount = false;
        this.width = width;
        setSize(width, width);
        setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(1f));

        if (isReachable) {
            if (isEaten)
                g2.setColor(new Color(191, 255, 191));
            else
                g2.setColor(new Color(223,63,63));
            g2.fillRect(1, 1, width - 1, width - 1);
        }
        if (!isEaten) {
            boolean flag = isCheating && isReversal || isCount && count == 0;
            int alpha = flag ? 100 : 255; //Cheating 模式下的透明度
            g2.setColor(flag ? new Color(0xf4df91) : Color.ORANGE); // Cheating 模式下的棋子淡化
            g2.fillOval(spacingLength, spacingLength, width - 2 * spacingLength, width - 2 * spacingLength);
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(Color.BLACK);
            g2.drawOval(spacingLength, spacingLength, width - 2 * spacingLength, width - 2 * spacingLength);
            g2.setStroke(new BasicStroke(1f));
            if (!isReversal || isCheating) {
                Color myColor = teamColor.getColor();
                g2.setColor(myColor);
                g2.drawOval(spacingLength + deltaWidth, spacingLength + deltaWidth, width - 2 * (spacingLength + deltaWidth), width - 2 * (spacingLength + deltaWidth));
                Color textColor = new Color(myColor.getRed(), myColor.getGreen(), myColor.getBlue(), alpha);
                g2.setColor(textColor);
                g2.setFont(CHESS_FONT);
                g2.drawString(name, width / 4, width * 2 / 3);
            }
            if (isSelected) {
                g2.setColor(Color.GREEN);
                g2.setStroke(new BasicStroke(4f));
                g2.drawOval(SELECTED_SPACING_LENGTH, SELECTED_SPACING_LENGTH, width - 2 * SELECTED_SPACING_LENGTH, width - 2 * SELECTED_SPACING_LENGTH);
            }
        }
        if (isCount && count > 0) {
            g2.setColor(Color.RED);
            g2.fillOval(width - COUNT_SIZE - 5, 5, COUNT_SIZE, COUNT_SIZE);
            g2.setColor(Color.BLACK);
            g2.setFont(COUNT_FONT);
            g2.drawOval(width - COUNT_SIZE - 5, 5, COUNT_SIZE, COUNT_SIZE);
            g2.drawString(String.valueOf(count),width - COUNT_SIZE + 1, COUNT_SIZE);
        }
    }
    public void switchCheating() {
        isCheating = !isCheating;
    }
}
