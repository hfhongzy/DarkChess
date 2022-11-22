package chessComponent;

import model.TeamColor;

public class AdvisorChessComponent extends ChessComponent {
    public AdvisorChessComponent(TeamColor color, int width) {
        super(width, 10);
        this.teamColor = color;
        name = color == TeamColor.RED ? "仕" : "士";
        ID = 1;
    }
}
