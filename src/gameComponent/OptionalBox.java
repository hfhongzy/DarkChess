package gameComponent;

import buttonComponent.GameButton;
import view.MainFrame;

import javax.swing.*;

public class OptionalBox extends JComponent {
    static final int WIDTH = 300;
    static final int HEIGHT = 400;
    Chessboard chessboard;
    GameButton undoButton, redoButton, cheatButton;
    GameButton saveButton, loadButton, restartButton;
    public OptionalBox(Chessboard chessboard) {
        setSize(WIDTH, HEIGHT);

        this.chessboard = chessboard;
        undoButton = new GameButton(70, 40, "撤销", false, chessboard::undo);
        undoButton.setLocation(30, 40);
        add(undoButton);
        redoButton = new GameButton(70, 40, "重做", false, chessboard::redo);
        redoButton.setLocation(150, 40);
        add(redoButton);
        cheatButton = new GameButton(70, 40, "透视", true, chessboard::switchCheating);
        cheatButton.setLocation(30, 90);
        add(cheatButton);
        saveButton = new GameButton(70, 40, "存档", true, chessboard::saveChess);
        saveButton.setLocation(30, 140);
        add(saveButton);
        loadButton = new GameButton(70, 40, "读档", true, chessboard::loadChess);
        loadButton.setLocation(150, 140);
        add(loadButton);
        restartButton = new GameButton(70, 40, "重开", true, () -> {
            if (JOptionPane.showConfirmDialog(null, "你真的要重开吗？", null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                chessboard.restart();
            }
        });
        restartButton.setLocation(150, 190);
        add(restartButton);
    
        loadButton = new GameButton(70, 40, "读档", true, chessboard::loadChess);
        loadButton.setLocation(150, 140);
        add(loadButton);
    }
}
