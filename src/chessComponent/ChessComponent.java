package chessComponent;

import model.TeamColor;

import javax.swing.*;
import java.awt.*;
//import java.awt.*;

public abstract class ChessComponent extends JComponent {
    boolean isReversal;
    boolean isSelected;
    boolean isReachable;
    boolean isEaten;
    int X, Y;
    TeamColor teamColor;
    private final int spacingLength, deltaWidth;
    private static final int SELECTED_SPACING_LENGTH = 3;
    private static final Font CHESS_FONT = new Font("Rockwell", Font.BOLD, 36);
    protected String name;
    int ID;
    final int score;

    public int getID() {
        return ID;
    }

    public int getScore() {
        return score;
    }

    public boolean isEaten() {
        return isEaten;
    }

    public void setEaten(boolean eaten) {
        isEaten = eaten;
    }

    public boolean isReversal() {
        return isReversal;
    }

    public void setReversal(boolean reversal) {
        isReversal = reversal;
    }

    public TeamColor getTeamColor() {
        return teamColor;
    }

    ChessComponent(int width, int score) {
        isReversal = true;
        isEaten = false;
        isReachable = false;
        spacingLength = width / 10;
        deltaWidth = spacingLength / 2;
        this.score = score;
        setSize(width, width);
        setVisible(true);
    }

    public int X() { return X; }
    public int Y() { return Y; }
    public void setX(int x) {
        X = x;
    }

    public void setY(int y) {
        Y = y;
    }

    public boolean isReachable() {
        return isReachable;
    }

    public void setReachable(boolean reachable) {
        isReachable = reachable;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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
            g2.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
        }
        if (!isEaten) {
            g2.setColor(Color.ORANGE);
            g2.fillOval(spacingLength, spacingLength, getWidth() - 2 * spacingLength, getHeight() - 2 * spacingLength);
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(Color.BLACK);
            g2.drawOval(spacingLength, spacingLength, getWidth() - 2 * spacingLength, getHeight() - 2 * spacingLength);
            g2.setStroke(new BasicStroke(1f));
            if (!isReversal) {
                g2.setColor(teamColor.getColor());
                g2.drawOval(spacingLength + deltaWidth, spacingLength + deltaWidth, getWidth() - 2 * (spacingLength + deltaWidth), getHeight() - 2 * (spacingLength + deltaWidth));
                g2.setColor(teamColor.getColor());
                g2.setFont(CHESS_FONT);
                g2.drawString(name, getWidth() / 4, getHeight() * 2 / 3);
            }
            if (isSelected) {
                g2.setColor(Color.GREEN);
                g2.setStroke(new BasicStroke(4f));
                g2.drawOval(SELECTED_SPACING_LENGTH, SELECTED_SPACING_LENGTH, getWidth() - 2 * SELECTED_SPACING_LENGTH, getHeight() - 2 * SELECTED_SPACING_LENGTH);
            }
        }
    }
}
