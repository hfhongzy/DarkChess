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
    private boolean myTurn, isServer;
    
    public TeamColor getMyColor() {
        return myColor;
    }
    
    public void setMyColor(TeamColor myColor) {
        this.myColor = myColor;
    }
    
    private TeamColor myColor = null;
    public void changeTurn() {
        myTurn = !myTurn;
    }
    public OnlineGameController(boolean isServer) {
        super();
        this.isServer = isServer;
        if(isServer) {
            server = new Server();
            myTurn = true;
        }  else {
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
            for(int x = 0; x < 8; x ++) {
                String s = client.read();
                System.out.println("read (outside) " + s);
                Chessboard.addChessRow(s, chessList);
            }
            chessboard.putChessOnBoard(new ArrayList<>(chessList));
            client.listening = true;
        }
//      todo : chessboard.moveSteps(step);
    }
    @Override
    public void onClick(ChessComponent chess) {
        if(chessboard.isEnded() || !myTurn) return;
        chessboard.click(chess);
        if(myColor == null) {
          myColor = chessboard.playerStatus.getCurrentColor();
          if(isServer) {
            server.send(chessboard.getLastStep());
          } else {
            client.send(chessboard.getLastStep());
          }
          if(isServer)
            server.listening = true;
          else
            client.listening = true;
        } else if(myColor != chessboard.playerStatus.getCurrentColor()) {
            if(isServer)
                server.send(chessboard.getLastStep());
            else
                client.send(chessboard.getLastStep());
            myTurn = false;
            System.out.println("click");
            //chess.move( server.read(); )
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
}
