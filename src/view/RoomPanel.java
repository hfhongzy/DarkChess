package view;

import buttonComponent.GameButton;
import gameController.OnlineGameController;
import model.PanelType;
import network.Message;
import network.ServerWait;

import javax.swing.*;


public class RoomPanel extends JPanel {
  
  public RoomPanel(MainFrame mainFrame) {
    setLayout(null);
    GameButton CreateButton = new GameButton(200, 40, "创建房间", () -> {
      ServerWait serverWait = new ServerWait(mainFrame, mainFrame.playGamePanel);
      serverWait.start();
      mainFrame.waitPanel.setServerWait(serverWait);
      mainFrame.showPanel("WAIT_PANEL");
    });
    CreateButton.setLocation(300, 200);
    add(CreateButton);
    
    GameButton JoinButton = new GameButton(200, 40, "加入房间", () -> {
      OnlineGameController onlineGameController = new OnlineGameController(false);
      onlineGameController.construct();
      onlineGameController.setMode(false, false, false, false);
      mainFrame.playGamePanel.setGameController(onlineGameController);
      onlineGameController.addControllerToNet();
  
      if(!onlineGameController.isConnected()) {
        System.out.println("Connect fail.");
        Message.show("连接失败。");
      } else {
        mainFrame.playGamePanel.start();
        mainFrame.showPanel(PanelType.PLAY_GAME_PANEL);
      }
    });
    JoinButton.setLocation(300, 280);
    add(JoinButton);
  
    GameButton returnButton = new GameButton(200, 40, "返回", () -> {
      mainFrame.showPanel(PanelType.MAIN_PANEL);
    });
    returnButton.setLocation(300, 360);
    add(returnButton);
  }
}
