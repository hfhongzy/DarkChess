package model;

import chessComponent.ChessComponent;

public class ChessStep {
    public int type;
    public int x1, y1, x2, y2;

    public ChessStep(int type, int x1, int y1) {
        this.type = type;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = this.y2 = 0;
    }
    
    public ChessStep(int type, int x1, int y1, int x2, int y2) {
        this.type = type;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    
    public int getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return type + " " + x1 + " " + y1 + " " + x2 + " " + y2;
    }
}
