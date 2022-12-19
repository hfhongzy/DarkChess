package gameController;

import chessComponent.ChessComponent;
import gameComponent.Chessboard;
import model.TeamColor;
import network.Client;
import network.Server;

import java.util.ArrayList;

public class OnlineGameController extends GameController {
    Server server;
    Client client;
    private boolean myTurn;
    final private boolean isServer;
    public void changeTurn() {
        myTurn = !myTurn;
        System.out.println("myturn : "  + String.valueOf(myTurn));
    }
    public void interrupt() {
        if(isServer) {
            System.out.println("Let me interrupt server!");
            server.interrupt();
        }
    }
    public OnlineGameController(boolean isServer) {
        super();
        this.isServer = isServer;
    }
    public void construct() {
        if (isServer) {
            server = new Server();
            server.construct();
            myTurn = true;
        } else {
            client = new Client();
            myTurn = false;
        }
    }
    public void addControllerToNet() { //新
        if(isServer)
            server.setOnlineGameController(this);
        else
            client.setOnlineGameController(this);
    }
    @Override
    public void loadGame() {
        if(isServer) {
            server.start();
            chessList = Chessboard.getRandomChess();
            chessboard.putChessOnBoard(new ArrayList<>(chessList));
            for(int i = 0; i < 8; i ++) {
                StringBuilder s = new StringBuilder();
                for (int j = 0; j < 4; j++) {
                    s.append(chessboard.getChessComponent(i, j).getTeamColor() == TeamColor.BLACK ? "B" : "R").
                        append(chessboard.getChessComponent(i, j).getID());
                    if(j < 3)
                        s.append(" ");
                }
                server.send(s.toString());
            }
        } else {
            client.start();
            chessList = new ArrayList<>();
            Chessboard.addChessRowInit();
            for(int x = 0; x < 8; x ++) {
                String s = client.read();
                System.out.println("read (outside) " + s);
                Chessboard.addChessRow(s, chessList);
            }
            chessboard.putChessOnBoard(new ArrayList<>(chessList));
            client.listening = true;
        }
    }
    @Override
    public void onClick(ChessComponent chess) {
        if(chessboard.isEnded() || !myTurn) return;
        if(chessboard.click(chess)) {
            if(isServer)
                server.send(chessboard.getLastStep());
            else
                client.send(chessboard.getLastStep());
            myTurn = false;
            System.out.println("click");
            if(isServer)
                server.listening = true;
            else
                client.listening = true;
        }
    }
    @Override
    public void restart() {
        chessList = null;
    }
    public boolean isConnected() {
        return isServer ? server.getFlag() : client.getFlag();
    }
}
