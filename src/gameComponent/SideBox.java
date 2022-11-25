package gameComponent;

import chessComponent.*;
import model.TeamColor;

import javax.swing.*;

public class SideBox extends JComponent {
    ChessComponent[] chessList = new ChessComponent[7];
    TeamColor teamColor;
    static final int CHESS_WIDTH = 70;
    static final int LEFT_SPACING = 20;
    static final int TOP_SPACING = 20;
    public SideBox(int width, int height, TeamColor color) { //x 坐标原点与 Box 的 x 宽度, y 高度
        chessList[0] = new GeneralChessComponent(color, CHESS_WIDTH);
        chessList[1] = new AdvisorChessComponent(color, CHESS_WIDTH);
        chessList[2] = new MinisterChessComponent(color, CHESS_WIDTH);
        chessList[3] = new ChariotChessComponent(color, CHESS_WIDTH);
        chessList[4] = new HorseChessComponent(color, CHESS_WIDTH);
        chessList[5] = new SoldierChessComponent(color, CHESS_WIDTH);
        chessList[6] = new CannonChessComponent(color, CHESS_WIDTH);
        for (int i = 0; i < 7; i++) {
            chessList[i].setCount(true);
            chessList[i].setReversal(false);
            chessList[i].setLocation(LEFT_SPACING, TOP_SPACING + i * CHESS_WIDTH);
            add(chessList[i]);
        }
        /*
        teamColor = color;
        eatenList = new ArrayList<SideBoxChessComponent>();
        int mid = WIDTH / 2 - Chessboard.CHESS_WIDTH / 2;
        for(int i = 0; i < 7; i ++) {
            SideBoxChessComponent chess = new SideBoxChessComponent(color, Chessboard.CHESS_WIDTH);
            chess.setLocation(mid, Chessboard.TOP_SPACING_LENGTH + i * Chessboard.CHESS_WIDTH);
            chess.setReversal(false);
            eatenList.add(chess);
            add(chess);
        }
        */
        setSize(width, height);
        setVisible(true);
    }
    public void addEatenChess(ChessComponent chess) {
        int ID = chess.getID();
        chessList[ID].addCount(1);
        chessList[ID].repaint();
    }
    public void removeEatenChess(ChessComponent chess) {
        int ID = chess.getID();
        chessList[ID].addCount(-1);
        chessList[ID].repaint();
    }
}