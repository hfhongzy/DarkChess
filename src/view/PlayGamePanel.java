package view;

import gameComponent.*;

import javax.swing.*;

public class PlayGamePanel extends JPanel {
    Chessboard chessboard;
    PlayerStatus playerStatus;
    OptionalBox optionalBox;
    public PlayGamePanel() {
        setLayout(null);

        playerStatus = new PlayerStatus();
        playerStatus.setLocation(500, 0);
        add(playerStatus);
        chessboard = new Chessboard(playerStatus);
        add(chessboard);
        optionalBox = new OptionalBox(chessboard);
        optionalBox.setLocation(500, 200);
        add(optionalBox);
    }
}
