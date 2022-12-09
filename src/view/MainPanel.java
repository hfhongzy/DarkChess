package view;

import buttonComponent.GameButton;
import labelComponent.GameLabel;
import labelComponent.TextBlock;
import model.PanelType;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    public MainPanel(MainFrame mainFrame) {
        setLayout(null);

        GameLabel title = new GameLabel(new TextBlock("翻棋", 50), new TextBlock("v1.0", Color.RED, 30));
        title.setLocation(400 - title.getWidth() / 2, 80);
        add(title);

        GameButton singlePlayerButton = new GameButton(200, 40, "单人模式", () -> {});
        singlePlayerButton.setLocation(300, 200);
        add(singlePlayerButton);

        GameButton doublePlayerButton = new GameButton(200, 40, "双人模式", () -> mainFrame.showPanel(PanelType.DOUBLE_PLAYER_PANEL));
        doublePlayerButton.setLocation(300, 280);
        add(doublePlayerButton);

        GameButton aboutButton = new GameButton(200, 40, "关于我们", () -> {
            JOptionPane.showMessageDialog(null, "制作人：wa-evil, hongzy\n版本号：v1.0", null, JOptionPane.INFORMATION_MESSAGE);
        });
        aboutButton.setLocation(300, 360);
        add(aboutButton);

        GameButton exitButton = new GameButton(200, 40, "退出游戏", () -> {
            if (JOptionPane.showConfirmDialog(null, "你真的要退出吗？", null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                System.exit(0);
        });
        exitButton.setLocation(300, 440);
        add(exitButton);
    }
}
