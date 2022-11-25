package chessComponent;

import model.TeamColor;

import java.awt.*;

public class EatenChessComponent extends ChessComponent{
  int count;
  public EatenChessComponent(TeamColor color, int width) {
    super(width/2, 0);
    this.teamColor = color;
    count = 0;
    System.out.println("Wow");
  }
  
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    System.out.println("Hi!");
  }
}
