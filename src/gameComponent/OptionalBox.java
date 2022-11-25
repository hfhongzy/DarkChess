package gameComponent;

import ButtonComponent.GameButton;

import javax.swing.*;

public class OptionalBox extends JComponent {
    static final int WIDTH = 300;
    static final int HEIGHT = 400;
    public OptionalBox(Chessboard chessboard) {
        setSize(WIDTH, HEIGHT);
        GameButton btn1 = new GameButton(70, 40, "撤销", true, chessboard::undo);
        btn1.setLocation(30, 40);
        add(btn1);
        GameButton btn2 = new GameButton(70, 40, "重做", true, chessboard::redo);
        btn2.setLocation(150, 40);
        add(btn2);
        GameButton btn3 = new GameButton(70, 40, "欺骗", true, chessboard::switchCheating);
        btn3.setLocation(30, 90);
        add(btn3);
    }
}
