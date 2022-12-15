package view;

import buttonComponent.GameButton;
import gameController.OnlineGameController;
import labelComponent.GameLabel;
import labelComponent.TextBlock;
import model.PanelType;
import network.Message;
import network.ServerWait;

import javax.swing.*;
import java.awt.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;


public class WaitPanel extends JPanel {
  public void setServerWait(ServerWait serverWait) {
    this.serverWait = serverWait;
  }
  
  ServerWait serverWait;
  public static String getIP() {
    String res = null;
    try {
      Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
      while (nifs.hasMoreElements()) {
        NetworkInterface nif = nifs.nextElement();
        Enumeration<InetAddress> address = nif.getInetAddresses();
        while (address.hasMoreElements()) {
          InetAddress addr = address.nextElement();
          if (addr instanceof Inet4Address) {
            if(nif.getName().equals("en0")) {
              //JOptionPane.showMessageDialog(null, "OK!");
              res = addr.getHostAddress();
//              return addr.getHostAddress();
            }
          }
//          System.out.println(nif.getName());
//          System.out.println(addr.getHostAddress().toString());
//          System.out.println(addr instanceof Inet6Address);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("Share your IP: " + res);
    return res;
  }
  public WaitPanel(MainFrame mainFrame) {
    setLayout(null);
    GameLabel title = new GameLabel(new TextBlock("您创建的房间", 50));
    title.setLocation(400 - title.getWidth() / 2, 120);
    add(title);
    
    GameLabel title2 = new GameLabel(new TextBlock("您的 IP 为 " + getIP() + "，分享给好友吧！", 20));
    title2.setLocation(400 - title2.getWidth() / 2, 210);
    add(title2);
    
    GameButton returnButton = new GameButton(200, 40, "返回", () -> {
      serverWait.interrupt();
      mainFrame.showPanel(PanelType.ROOM_PANEL);
    });
    returnButton.setLocation(300, 340);
    add(returnButton);
  }
}
