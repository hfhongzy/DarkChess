package view;

import ButtonComponent.GameButton;
import model.PanelType;

import javax.swing.*;

public class MainPanel extends JPanel {
    MainFrame mainFrame;
    public MainPanel(MainFrame mainFrame) {
        setLayout(null);
        this.mainFrame = mainFrame;
        GameButton btn1 = new GameButton(100, 40, "aaa", true, () -> mainFrame.showPanel(PanelType.PLAY_GAME_PANEL));
        GameButton btn2 = new GameButton(100, 40, "bbb", false, null);
        btn1.setLocation(300, 100);
        btn2.setLocation(300, 400);
        add(btn1);
        add(btn2);
    }
}
