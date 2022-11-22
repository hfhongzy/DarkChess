package chessComponent;

import model.TeamColor;

public class GeneralChessComponent extends ChessComponent {
    public GeneralChessComponent(TeamColor color, int width) {
        super(width, 30);
        this.teamColor = color;
        name = color == TeamColor.RED ? "帥" : "將";
        ID = 0;
    }
}
