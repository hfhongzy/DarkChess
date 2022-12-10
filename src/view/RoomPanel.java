package view;

import buttonComponent.GameButton;
import gameController.OnlineGameController;
import model.PanelType;

import javax.swing.*;


public class RoomPanel extends JPanel {
  
  public RoomPanel(MainFrame mainFrame) {
    setLayout(null);
    GameButton CreateButton = new GameButton(200, 40, "创建房间", () -> {
      OnlineGameController onlineGameController = new OnlineGameController(true);
      onlineGameController.setMode(true, true, true, true);
      mainFrame.playGamePanel.setGameController(onlineGameController);
      onlineGameController.addControllerToNet();
      mainFrame.playGamePanel.start();
      mainFrame.showPanel(PanelType.PLAY_GAME_PANEL);
    });
    CreateButton.setLocation(300, 200);
    add(CreateButton);
    
    GameButton JoinButton = new GameButton(200, 40, "加入房间", () -> {
      OnlineGameController onlineGameController = new OnlineGameController(false);
      onlineGameController.setMode(false, false, false, false);
      mainFrame.playGamePanel.setGameController(onlineGameController);
      onlineGameController.addControllerToNet();
      mainFrame.playGamePanel.start();
      mainFrame.showPanel(PanelType.PLAY_GAME_PANEL);
    });
    JoinButton.setLocation(300, 280);
    add(JoinButton);
  }
}
