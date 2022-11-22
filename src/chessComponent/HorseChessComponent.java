package chessComponent;

import model.TeamColor;

public class HorseChessComponent extends ChessComponent {
    public HorseChessComponent(TeamColor color, int width) {
        super(width, 5);
        this.teamColor = color;
        name = color == TeamColor.RED ? "傌" : "馬";
        ID = 4;
    }
}
