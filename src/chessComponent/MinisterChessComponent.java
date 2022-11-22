package chessComponent;

import model.TeamColor;

public class MinisterChessComponent extends ChessComponent {
    public MinisterChessComponent(TeamColor color, int width) {
        super(width, 5);
        this.teamColor = color;
        name = color == TeamColor.RED ? "相" : "象";
        ID = 2;
    }
}
