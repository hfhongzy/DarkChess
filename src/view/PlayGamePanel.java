package view;

import gameComponent.*;

import javax.swing.*;

public class PlayGamePanel extends JPanel {
    Chessboard chessboard;
    PlayerStatus playerStatus;
    OptionalBox optionalBox;
    public void setMode(int mode) {
        chessboard.setMode(mode);
    }
    public PlayGamePanel(MainFrame mainFrame) {
        setLayout(null);
        playerStatus = new PlayerStatus();
        playerStatus.setLocation(500, 0);
        add(playerStatus);
        chessboard = new Chessboard(playerStatus);
        add(chessboard);
        optionalBox = new OptionalBox(mainFrame, chessboard);
        optionalBox.setLocation(500, 200);
        add(optionalBox);
        chessboard.setOptionalBox(optionalBox);
    }
}
