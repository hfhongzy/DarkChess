package view;

import buttonComponent.GameButton;
import model.PanelType;
import network.Client;
import network.Server;

import javax.swing.*;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RoomPanel extends JPanel {
  
  public RoomPanel(MainFrame mainFrame) {
    setLayout(null);
    GameButton CreateButton = new GameButton(200, 40, "创建房间", true, () -> {
      String IP = Server.getIP();
      JOptionPane.showMessageDialog(null, "您的 IP 为：" + IP + "，请分享给好友后点击确认！");
      Server server = new Server(new String[0]);
      
      
//      System.out.println("Get Host ID" + IP);
//      String [] ip = new String [1];
//      ip[0] = IP;
      
      if(server.getFlag()) {
        JOptionPane.showMessageDialog(null, "成功连接！");
        mainFrame.playGamePanel.chessboard.setServer(server);
        mainFrame.playGamePanel.chessboard.serverStart();
        mainFrame.showPanel(PanelType.PLAY_GAME_PANEL);
      } else {
        JOptionPane.showMessageDialog(null, "连接失败，请重新链接。");
      }
    });
    CreateButton.setLocation(300, 200);
    add(CreateButton);
    
    GameButton JoinButton = new GameButton(200, 40, "加入房间", true, () -> {
      String[] IP = new String [1];
      IP[0] = JOptionPane.showInputDialog("请输入房主的 IP 地址");
      System.out.println("Get Host ID" + IP[0]);
      Client client = new Client(IP);
      if(client.getFlag()) {
        JOptionPane.showMessageDialog(null, "成功连接！");
        mainFrame.playGamePanel.chessboard.setClient(client);
        mainFrame.showPanel(PanelType.PLAY_GAME_PANEL);
      } else {
        JOptionPane.showMessageDialog(null, "连接失败，请重新链接。");
      }
    });
    JoinButton.setLocation(300, 280);
    add(JoinButton);
  }
}
