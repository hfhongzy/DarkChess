package gameController;

import chessComponent.ChessComponent;
import gameComponent.Chessboard;

import java.util.ArrayList;

public class PVPGameController extends GameController {
    public PVPGameController() {
        super();
    }
    @Override
    public void loadGame() {
        if (chessList == null)
            chessList = Chessboard.getRandomChess();
        chessboard.putChessOnBoard(new ArrayList<>(chessList));
//      todo : chessboard.moveSteps(step);
    }
    @Override
    public void onClick(ChessComponent chess) {
        if(chessboard.isEnded()) return;
        chessboard.click(chess);
    }
    @Override
    public void restart() {
        chessList = null;
    }
}
