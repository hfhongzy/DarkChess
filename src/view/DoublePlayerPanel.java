package view;

import buttonComponent.GameButton;
import labelComponent.GameLabel;
import labelComponent.TextBlock;
import model.PanelType;

import javax.swing.*;
import java.awt.*;

public class DoublePlayerPanel extends JPanel {

    public DoublePlayerPanel(MainFrame mainFrame) {
        setLayout(null);

        GameLabel title = new GameLabel(new TextBlock("双人模式", 50));
        title.setLocation(400 - title.getWidth() / 2, 80);
        add(title);

        GameButton standaloneModeButton = new GameButton(200, 40, "热座模式", true, () -> {
            mainFrame.playGamePanel.setMode(1);
            mainFrame.showPanel(PanelType.PLAY_GAME_PANEL);
        });
        standaloneModeButton.setLocation(300, 200);
        add(standaloneModeButton);
        
        GameButton LANModeButton = new GameButton(200, 40, "局域网联机", true, () -> {
            mainFrame.playGamePanel.setMode(2);
            mainFrame.showPanel(PanelType.PLAY_GAME_PANEL);
        });
        LANModeButton.setLocation(300, 280);
        add(LANModeButton);

        GameButton onlineModeButton = new GameButton(200, 40, "在线匹配", false, null);
        onlineModeButton.setLocation(300, 360);
        add(onlineModeButton);

        GameButton returnButton = new GameButton(200, 40, "返回", true, () -> mainFrame.showPanel(PanelType.MAIN_PANEL));
        returnButton.setLocation(300, 440);
        add(returnButton);
    }
}
