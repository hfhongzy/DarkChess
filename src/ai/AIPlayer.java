package ai;

import chessComponent.ChessComponent;
import gameComponent.Chessboard;
import gameController.PVEGameController;

import java.util.PriorityQueue;

public class AIPlayer extends Thread {
    PVEGameController pveGameController;
    Chessboard chessboard;
    int think_time;
    public AIPlayer(PVEGameController pveGameController, int think_time) {
        this.pveGameController = pveGameController;
        chessboard = pveGameController.getChessboard();
        this.think_time = think_time;
    }
    /*
    class Status {
        int[][] chessboard = new int[8][4];
        int step;
    }
     */
    @Override
    public void run() {
        long begin_time = System.currentTimeMillis();
//        PriorityQueue<Status> Q, old_Q;
//        for (int i = 1; i <= )
        while (true) {
            long current_time = System.currentTimeMillis();
            if (current_time - begin_time >= think_time * 1000L)
                break;
            // todo
        }
        // move
        for (int i = 0; i < 8; i++) {
            boolean flag = false;
            for (int j = 0; j < 4; j++) {
                ChessComponent chess = chessboard.getChessComponent(i, j);
                if (!chess.isEaten() && chess.isReversal()) {
                    chessboard.flip(chess);
                    flag = true;
                    break;
                }
            }
            if (flag) break;
        }
        pveGameController.setMyTurn(true);
    }
}
