package gameController;

import chessComponent.ChessComponent;
import gameComponent.Chessboard;

import java.util.ArrayList;

public abstract class GameController {
    Chessboard chessboard;
    ArrayList<ChessComponent> chessList; // 初始棋盘，如果null表示随机生成一个
    public boolean undo_redo, cheat, restart, load;
    public abstract void loadGame();
    public abstract void onClick(ChessComponent chess);
    public abstract void restart();
    public GameController() {
        setMode(true, true, true, true);
    }
    
    public Chessboard getChessboard() {
        return chessboard;
    }
    
    public void setMode(boolean undo_redo, boolean cheat, boolean restart, boolean load) {
        this.undo_redo = undo_redo;
        this.cheat = cheat;
        this.restart = restart;
        this.load = load;
    }
    public void setChessboard(Chessboard chessboard) {
        this.chessboard = chessboard;
    }
    public boolean load() {
        return true;
    }
    public void save() {

    }
}
