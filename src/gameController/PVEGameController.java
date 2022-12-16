package gameController;

import ai.AIPlayer;
import chessComponent.ChessComponent;
import gameComponent.Chessboard;

import java.util.ArrayList;

public class PVEGameController extends GameController {
    boolean myTurn;
    int mode;
    public PVEGameController(int mode) {
        super();
        this.mode = mode;
    }
    @Override
    public void loadGame() {
        if (chessList == null)
            chessList = Chessboard.getRandomChess();
        chessboard.putChessOnBoard(new ArrayList<>(chessList));
        myTurn = true; // myTurn = step.size() % 2 == 0;
//      todo : chessboard.moveSteps(step);
    }
    @Override
    public void onClick(ChessComponent chess) {
        /*
        if (chessboard.isEnded()) return;
        if (myTurn) {
            myTurn = false;
            AIPlayer aiPlayer = new AIPlayer(this, 5);
            aiPlayer.start();
        }
        */
//        /*
        if (chessboard.isEnded() || !myTurn) return;
        if (chessboard.click(chess)) {
            myTurn = false;
            if (!chessboard.isEnded()) {
                AIPlayer aiPlayer = new AIPlayer(this, 1);
                aiPlayer.start();
            }
        }
//         */
    }
    @Override
    public void restart() {
        chessList = null;
        myTurn = true;
    }
    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }
}
