package network;

import gameController.OnlineGameController;
import model.PanelType;
import view.MainFrame;
import view.PlayGamePanel;

public class ServerWait extends Thread{
  Thread thread;
  PlayGamePanel playGamePanel;
  MainFrame mainFrame;
  public ServerWait(MainFrame mainFrame, PlayGamePanel playGamePanel) {
    this.mainFrame = mainFrame;
    this.playGamePanel = playGamePanel;
  }
  public void start() {
    //Message.show("连接中！");
    if(thread == null) {
      thread = new Thread(this, "ServerThread");
      thread.start();
    }
  }
  OnlineGameController onlineGameController;
  
  @Override
  public void interrupt() {
    onlineGameController.interrupt();
  }
  
  public void run() {
    onlineGameController = new OnlineGameController(true);
    onlineGameController.construct();
    onlineGameController.setMode(false, false, true, false);
    playGamePanel.setGameController(onlineGameController);
    onlineGameController.addControllerToNet();
    if(onlineGameController.isConnected()) {
      playGamePanel.start();
      mainFrame.showPanel(PanelType.PLAY_GAME_PANEL);
    }
  }
}
