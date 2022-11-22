package chessComponent;

import model.TeamColor;

public class CannonChessComponent extends ChessComponent {
    public CannonChessComponent(TeamColor color, int width) {
        super(width, 5);
        this.teamColor = color;
        name = color == TeamColor.RED ? "炮" : "砲";
        ID = 5;
    }
}
