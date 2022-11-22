package chessComponent;

import model.TeamColor;

public class ChariotChessComponent extends ChessComponent {
    public ChariotChessComponent(TeamColor color, int width) {
        super(width, 5);
        this.teamColor = color;
        name = color == TeamColor.RED ? "俥" : "車";
        ID = 3;
    }
}
