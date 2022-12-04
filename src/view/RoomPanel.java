package view;

import buttonComponent.GameButton;
import labelComponent.GameLabel;
import labelComponent.TextBlock;
import model.PanelType;
import network.Client;
import network.Server;

import javax.swing.*;
import java.awt.*;
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
      System.out.println("您的 IP 为：" + IP + "，请分享给好友！");
//      JOptionPane.showMessageDialog(null, IP, "提示", JOptionPane.PLAIN_MAGE);
      
      Server server = new Server(new String[0]);
      
      
//      System.out.println("Get Host ID" + IP);
//      String [] ip = new String [1];
//      ip[0] = IP;
      
      if(server.getFlag()) {
        JOptionPane.showMessageDialog(null, "成功连接！");
        mainFrame.playGamePanel.chessboard.setServer(server);
        mainFrame.playGamePanel.chessboard.serverStart();
        mainFrame.playGamePanel.chessboard.setIsServer(true);
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
      Client client = new Client(IP);
      
      if(client.getFlag()) {
        JOptionPane.showMessageDialog(null, "成功连接！");
        mainFrame.playGamePanel.chessboard.setClient(client);
        mainFrame.playGamePanel.chessboard.clientStart();
        mainFrame.playGamePanel.chessboard.setIsServer(false);
        mainFrame.showPanel(PanelType.PLAY_GAME_PANEL);
      } else {
        JOptionPane.showMessageDialog(null, "连接失败，请重新连接。");
      }
    });
    JoinButton.setLocation(300, 280);
    add(JoinButton);
  }
}
