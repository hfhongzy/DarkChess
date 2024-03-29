package view;

import buttonComponent.GameButton;
import gameController.OnlineGameController;
import gameController.PVPGameController;
import labelComponent.GameLabel;
import labelComponent.TextBlock;
import model.PanelType;

import javax.swing.*;

public class DoublePlayerPanel extends JPanel {

    public DoublePlayerPanel(MainFrame mainFrame) {
        setLayout(null);

        GameLabel title = new GameLabel(new TextBlock("双人模式", 50));
        title.setLocation(400 - title.getWidth() / 2, 80);
        add(title);

        GameButton standaloneModeButton = new GameButton(200, 40, "热座模式", () -> {
            PVPGameController pvpGameController = new PVPGameController();
            pvpGameController.setMode(true, true, true, true);
            mainFrame.playGamePanel.setGameController(pvpGameController);
            mainFrame.playGamePanel.start();
            mainFrame.showPanel(PanelType.PLAY_GAME_PANEL);
        });
        standaloneModeButton.setLocation(300, 200);
        add(standaloneModeButton);
        
        GameButton LANModeButton = new GameButton(200, 40, "局域网联机", () -> {
            mainFrame.showPanel(PanelType.ROOM_PANEL);
        });
        LANModeButton.setLocation(300, 280);
        add(LANModeButton);

        GameButton onlineModeButton = new GameButton(200, 40, "在线匹配", null);
        onlineModeButton.setWorking(false);
        onlineModeButton.setLocation(300, 360);
        add(onlineModeButton);

        GameButton returnButton = new GameButton(200, 40, "返回", () -> mainFrame.showPanel(PanelType.MAIN_PANEL));
        returnButton.setLocation(300, 440);
        add(returnButton);
    }
}
