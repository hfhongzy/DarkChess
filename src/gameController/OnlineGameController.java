package gameController;

import chessComponent.ChessComponent;
import gameComponent.Chessboard;

import java.util.ArrayList;
import java.util.Random;

public class OnlineGameController extends GameController {
    public OnlineGameController() {
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

    }
    @Override
    public void restart() {

    }
}
