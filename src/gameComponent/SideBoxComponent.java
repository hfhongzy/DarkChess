package gameComponent;

import chessComponent.ChessComponent;
import chessComponent.EatenChessComponent;
import model.TeamColor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SideBoxComponent extends JComponent {
  final ArrayList<EatenChessComponent> eatenList;
  final TeamColor teamColor;
  final int WIDTH, HEIGHT;
  public SideBoxComponent(int WIDTH, int HEIGHT, TeamColor color) { //x 坐标原点与 Box 的 x 宽度, y 高度
    teamColor = color;
    eatenList = new ArrayList<EatenChessComponent>();
    int mid = WIDTH / 2 - Chessboard.CHESS_WIDTH / 2;
    for(int i = 0; i < 7; i ++) {
      EatenChessComponent chess = new EatenChessComponent(color, Chessboard.CHESS_WIDTH);
      chess.setLocation(mid, Chessboard.TOP_SPACING_LENGTH + i * Chessboard.CHESS_WIDTH);
      chess.setReversal(false);
      eatenList.add(chess);
      add(chess);
    }
    this.WIDTH = WIDTH;
    this.HEIGHT = HEIGHT;
    setSize(WIDTH, HEIGHT);
    setVisible(true);
  }
}