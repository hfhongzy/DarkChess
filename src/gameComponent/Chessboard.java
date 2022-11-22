package gameComponent;

import chessComponent.*;
import controller.GameController;
import model.TeamColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

public class Chessboard extends JComponent {
    ChessComponent[][] chessComponents = new ChessComponent[8][4];
    SideBoxComponent leftSide, rightSide; // 展示被吃的棋子
    ChessComponent first; // = null : 未选中 != null : 选中一个未被eaten的棋子
    private static final int CHESS_WIDTH = 70;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 600;
    private static final int SIDEBOX_WIDTH = 110;
    private static final int TOP_SPACING_LENGTH = 20;
    PlayerStatus playerStatus;
    public Chessboard(PlayerStatus playerStatus) {
        setLayout(null);
        setSize(WIDTH, HEIGHT);
        this.playerStatus = playerStatus;

        initChessOnBoard();
        putChessOnBoard();
    }

    public void initChessOnBoard() {
        // todo : 应该会把这个变成接口，读档/新开局两不误
        ArrayList<ChessComponent> chess = new ArrayList<>(); // 生成棋子列表
        for (TeamColor color : TeamColor.values()) {
            chess.add(new GeneralChessComponent(color, CHESS_WIDTH));
            for (int i = 0; i < 2; i++) {
                chess.add(new AdvisorChessComponent(color, CHESS_WIDTH));
                chess.add(new MinisterChessComponent(color, CHESS_WIDTH));
                chess.add(new HorseChessComponent(color, CHESS_WIDTH));
                chess.add(new ChariotChessComponent(color, CHESS_WIDTH));
                chess.add(new CannonChessComponent(color, CHESS_WIDTH));
            }
            for (int i = 0; i < 5; i++)
                chess.add(new SoldierChessComponent(color, CHESS_WIDTH));
        }
        Collections.shuffle(chess); // 随机打乱棋子
        for (int i = 0; i < chess.size(); i++) { // 放到棋盘上
            int x = i % 8, y = i / 8;
            ChessComponent chessComponent = chess.get(i);
            chessComponent.setX(x);
            chessComponent.setY(y);
            chessComponent.setLocation(SIDEBOX_WIDTH + y * CHESS_WIDTH, TOP_SPACING_LENGTH + x * CHESS_WIDTH);
            chessComponent.addMouseListener(new MouseAdapter() {
                boolean isPressed = false;
                boolean isOnChess(int X, int Y) {
                    return 2 * Math.sqrt((X - CHESS_WIDTH / 2.0) * (X - CHESS_WIDTH / 2.0) + (Y - CHESS_WIDTH / 2.0) * (Y - CHESS_WIDTH / 2.0)) <= CHESS_WIDTH;
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (isPressed && isOnChess(e.getX(), e.getY()))
                        onClick(chessComponent);
                    isPressed = false;
                }
                @Override
                public void mousePressed(MouseEvent e) {
                    if (isOnChess(e.getX(), e.getY()))
                        isPressed = true;
                }
            });
            chessComponents[x][y] = chessComponent;
        }
    }
    public void putChessOnBoard() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 4; j++)
                add(chessComponents[i][j]);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // todo : 绘制一个棋盘

        g.setColor(Color.GREEN);
        g.drawRect(0, 0, WIDTH, HEIGHT);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1f));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        for (int i = 1; i < 8; i++)
            g2.drawLine(SIDEBOX_WIDTH, TOP_SPACING_LENGTH + i * CHESS_WIDTH, SIDEBOX_WIDTH + 4 * CHESS_WIDTH, TOP_SPACING_LENGTH + i * CHESS_WIDTH);
        for (int i = 1; i < 4; i++)
            g2.drawLine(SIDEBOX_WIDTH + i * CHESS_WIDTH, TOP_SPACING_LENGTH, SIDEBOX_WIDTH + i * CHESS_WIDTH, TOP_SPACING_LENGTH + 8 * CHESS_WIDTH);

        g2.setStroke(new BasicStroke(2f));
        g2.drawRect(SIDEBOX_WIDTH, TOP_SPACING_LENGTH, 4 * CHESS_WIDTH, 8 * CHESS_WIDTH);
    }
    /*
    boolean checkMoveTo(boolean isCannon, ChessComponent chess) {
        return chess.isEaten() || (isCannon || !chess.isReversal()) && chess.getTeamColor() != playerStatus.currentColor;
    }
    */
    boolean checkMoveTo(ChessComponent from, ChessComponent to) {
        if (from instanceof CannonChessComponent) {

        }
        else {

        }
        return true;
    }
    void setReachableChess(ChessComponent chess, boolean status) { // 将 chess 可到达的棋子设置成 status, 并且重新绘制
        /*
        ArrayList<ChessComponent> chessArr = new ArrayList<>();
        int X = chess.X(), Y = chess.Y();
        if (chess instanceof CannonChessComponent) {
            for (int i = X - 1; i >= 0; i--)
                if (checkMoveTo(chess, chessComponents[i][Y])) {
                    chessArr.add(chessComponents[i][Y]);
                    break;
                }
            for (int i = X + 1; i < 8; i++)
                if (checkMoveTo(chess, chessComponents[i][Y])) {
                    chessArr.add(chessComponents[i][Y]);
                    break;
                }
            for (int j = Y - 1; j >= 0; j--)
                if (checkMoveTo(chess, chessComponents[X][j])) {
                    chessArr.add(chessComponents[X][j]);
                    break;
                }
            for (int j = Y + 1; j < 4; j++)
                if (checkMoveTo(chess, chessComponents[X][j])) {
                    chessArr.add(chessComponents[X][j]);
                    break;
                }
        }
        else {
            if (X - 1 >= 0 && checkMoveTo(chess, chessComponents[X - 1][Y]))
                chessArr.add(chessComponents[X - 1][Y]);
            if (X + 1 < 8 && checkMoveTo(chess, chessComponents[X + 1][Y]))
                chessArr.add(chessComponents[X + 1][Y]);
            if (Y - 1 >= 0 && checkMoveTo(chess, chessComponents[X][Y - 1]))
                chessArr.add(chessComponents[X][Y - 1]);
            if (Y + 1 < 4 && checkMoveTo(chess, chessComponents[X][Y + 1]))
                chessArr.add(chessComponents[X][Y + 1]);
        }
        for (int i = 0; i < chessArr.size(); i++) {
            ChessComponent item = chessArr.get(i);
            item.setReachable(status);
            item.repaint();
        }
         */
    }
    boolean checkFirst(ChessComponent chess) { // 判断 chess 是否可被选中
        return !chess.isEaten() && chess.getTeamColor() == playerStatus.getCurrentColor();
    }
    void onClick(ChessComponent chess) {
        if (first == null) {
            if (chess.isReversal()) {
                flip(chess);
                chess.repaint();
            } else if (checkFirst(chess)) {
                setReachableChess(chess, true);
                first = chess;
                first.setSelected(true);
                first.repaint();
            }
        } else {
            if (first == chess) {
                setReachableChess(first, false);
                first.setSelected(false);
                first.repaint();
                first = null;
            }
            else if (chess.isReachable()) {
                setReachableChess(first, false);
                first.setSelected(false);
                if (chess.isEaten()) move(first, chess);
                else capture(first, chess);
                first.repaint(); chess.repaint();
                first = null;
            }
            else if (chess.isReversal()) {
                setReachableChess(first, false);
                first.setSelected(false);
                flip(chess);
                first.repaint(); chess.repaint();
                first = null;
            }
            else if (checkFirst(chess)) {
                setReachableChess(first, false);
                first.setSelected(false);
                setReachableChess(chess, true);
                chess.setSelected(true);
                first.repaint(); chess.repaint();
                first = chess;
            }
        }
    }
    void exchangePlayer() {
        playerStatus.setCurrentColor(playerStatus.getCurrentColor() == TeamColor.RED ? TeamColor.BLACK : TeamColor.RED);
        playerStatus.repaint();
    }
    void move(ChessComponent chess1, ChessComponent chess2) {
        // todo : 将 chess1 移动到 chess2
        exchangePlayer();
    }
    void capture(ChessComponent chess1, ChessComponent chess2) {
        // todo : eat content
        move(chess1, chess2);
    }
    void flip(ChessComponent chess) {
        chess.setReversal(false);
        if (playerStatus.getCurrentColor() == null) // 第一次翻转
            playerStatus.setCurrentColor(chess.getTeamColor());
        exchangePlayer();
    }
}
