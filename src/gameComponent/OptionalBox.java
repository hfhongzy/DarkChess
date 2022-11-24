package gameComponent;

import ButtonComponent.GameButton;

import javax.swing.*;

public class OptionalBox extends JComponent {
    static final int WIDTH = 300;
    static final int HEIGHT = 400;
    public OptionalBox() {
        setSize(WIDTH, HEIGHT);
        GameButton btn1 = new GameButton(70, 40, "test", true, () -> {});
        btn1.setLocation(30, 40);
        add(btn1);
        GameButton btn2 = new GameButton(70, 100, "你好", true, () -> {});
        btn2.setLocation(150, 40);
        add(btn2);
    }
}
