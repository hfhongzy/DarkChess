package network;

import labelComponent.GameLabel;
import labelComponent.TextBlock;

import javax.swing.*;
import java.awt.*;

public class Message extends Thread{
  Thread t;
  String message;
  Message(String message) {
    this.message = message;
  }
  public void start() {
    t = new Thread(this,"mes");
    t.start();
  }
  public void run() {
    JOptionPane.showMessageDialog(null, "haha!");
    /*
    JFrame d = new JFrame();
    d.setTitle("Title!");
    d.setLocationRelativeTo(null); // 窗口居中
    d.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    d.setResizable(false);
    d.setVisible(true);
    d.setSize(500, 200);
    //todo: 美化
    GameLabel label = new GameLabel(new TextBlock(message, 30));
    d.add(label);
   
     */
  }
  public static void show(String message) {
//    System.out.println(message);
    Message a = new Message(message);
    a.start();
  }
}