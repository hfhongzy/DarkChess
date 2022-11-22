package panel;

import gameComponent.*;
import controller.GameController;

import javax.swing.*;

public class PlayGamePanel extends JPanel {
    Chessboard chessboard;
    PlayerStatus playerStatus;
    public PlayGamePanel() {
        setLayout(null);

        playerStatus = new PlayerStatus();
        playerStatus.setLocation(500, 0);
        add(playerStatus);
        chessboard = new Chessboard(playerStatus);
        add(chessboard);
    }
}
