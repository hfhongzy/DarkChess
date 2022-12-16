package ai;

import chessComponent.ChessComponent;
import chessComponent.GeneralChessComponent;
import gameComponent.Chessboard;
import gameController.PVEGameController;
import model.ChessStep;

import java.util.ArrayList;
import java.util.Comparator;
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
    // 透视人只因
    class Status {
        static final int EATEN = 127;
//        static final int[] score = new int[7];
        byte[][] chessData = new byte[8][4]; // self * type * 2 + flip
        ArrayList<ChessStep> chessSteps;
        int score; // 该状态的得分
        void setStartStatus() {
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 4; j++) {
                    ChessComponent chess = chessboard.getChessComponent(i, j);
                    int type = chess.getID();
                    int team = chessboard.playerStatus.getCurrentColor() == chess.getTeamColor() ? 1 : -1;
                    int flip = chess.isReversal() ? 1 : 0;
                    if (chess.isEaten())
                        chessData[i][j] = EATEN;
                    else
                        chessData[i][j] = (byte)(team * type * 2 + flip);
                }
            chessSteps = new ArrayList<>();
        }
    }
    @Override
    public void run() {
        long begin_time = System.currentTimeMillis();
        /*
        PriorityQueue<Status> Qa, Qb;
        Qa = new PriorityQueue<>(new Comparator<Status>() {
            @Override
            public int compare(Status o1, Status o2) {
                return 0;
            }
        })
         */
//        for (int i = 1; i <= )
        MCTNode root = new MCTNode(chessboard, null, null, 1, 1);
        System.out.println(root.toString());
        root.generate();
        while (true) {
            long current_time = System.currentTimeMillis();
            if (current_time - begin_time >= think_time * 1000L)
                break;
            
            for(int test = 0; test < 100; test ++) {
                MCTNode.dfs(root);
            }
        }
        
        MCTNode next = root.best2();
        chessboard.moveChess(next.chessStep.toString());
        // move
        /*
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
        
         */
        pveGameController.setMyTurn(true);
    }
}
