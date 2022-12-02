package view;

import buttonComponent.GameButton;
import labelComponent.GameLabel;
import labelComponent.TextBlock;
import radioButtonComponent.GameRadioButton;
import model.PanelType;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    MainFrame mainFrame;
    public MainPanel(MainFrame mainFrame) {
        setLayout(null);
        this.mainFrame = mainFrame;
        GameButton btn1 = new GameButton(100, 40, "aaa", true, () -> {
//            mainFrame.playGamePanel.xxx
            mainFrame.showPanel(PanelType.PLAY_GAME_PANEL);
        });
        GameButton btn2 = new GameButton(100, 40, "___", false, null);
        btn1.setLocation(300, 100);
        btn2.setLocation(500, 100);
        add(btn1);
        add(btn2);
        GameLabel label = new GameLabel(new TextBlock("L", 40), new TextBlock("iu_AK", Color.RED, 20));
        label.setLocation(10, 10);
        add(label);

        GameRadioButton rb = new GameRadioButton(100, 50, "aaa", false, null);
        rb.setLocation(450, 50);
        add(rb);
    }
}
