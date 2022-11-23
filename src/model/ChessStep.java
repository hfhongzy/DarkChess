package model;

import chessComponent.ChessComponent;

public class ChessStep {
  int type;
  ChessComponent chess1, chess2;
  
  public ChessStep(int type, ChessComponent chess) {
    this.type = type;
    this.chess1 = chess;
  }
  public ChessStep(int type, ChessComponent chess1, ChessComponent chess2) {
    this.type = type;
    this.chess1 = chess1;
    this.chess2 = chess2;
  }
  
  public int getType() {
    return type;
  }
  
  public ChessComponent getChess1() {
    return chess1;
  }
  
  public ChessComponent getChess2() {
    return chess2;
  }
}
