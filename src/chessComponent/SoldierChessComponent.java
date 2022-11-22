package chessComponent;

import model.TeamColor;

public class SoldierChessComponent extends ChessComponent {
    public SoldierChessComponent(TeamColor color, int width) {
        super(width, 1);
        this.teamColor = color;
        name = color == TeamColor.RED ? "兵" : "卒";
        ID = 5;
    }
}
