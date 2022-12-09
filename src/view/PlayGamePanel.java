package view;

import gameComponent.*;
import gameController.GameController;
import model.PanelType;

import javax.swing.*;

public class PlayGamePanel extends JPanel {
    MainFrame mainFrame;
    Chessboard chessboard;
    PlayerStatus playerStatus;
    OptionalBox optionalBox;
    GameController gameController;
    public PlayGamePanel(MainFrame mainFrame) {
        setLayout(null);

        this.mainFrame = mainFrame;

        playerStatus = new PlayerStatus();
        playerStatus.setLocation(500, 0);
        chessboard = new Chessboard(playerStatus);
        optionalBox = new OptionalBox(this, chessboard);
        optionalBox.setLocation(500, 200);
        chessboard.setOptionalBox(optionalBox);

        add(chessboard);
        add(playerStatus);
        add(optionalBox);
    }
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        gameController.setChessboard(chessboard);
    }
    public void start() {
        chessboard.start(gameController);
        optionalBox.start(gameController.undo_redo, gameController.cheat, gameController.restart, gameController.load);
        playerStatus.start();
    }
    public void restart() {
        gameController.restart();
        start();
    }
    public void returnToMain() {
        mainFrame.showPanel(PanelType.MAIN_PANEL);
    }
    public void load() {
        if (gameController.load())
            start();
    }
    public void save() {
        gameController.save();
    }
}
