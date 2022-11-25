package gameComponent;

import ButtonComponent.GameButton;

import javax.swing.*;

public class OptionalBox extends JComponent {
    static final int WIDTH = 300;
    static final int HEIGHT = 400;
    Chessboard chessboard;
    GameButton undoButton, redoButton, cheatButton;
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
    }
}
