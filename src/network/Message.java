package network;

import labelComponent.GameLabel;
import labelComponent.TextBlock;

import javax.swing.*;

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
    JDialog d = new JDialog();
    d.setTitle("Title!");
    d.setLocationRelativeTo(null); // 窗口居中
    d.setResizable(false);
    d.setModal(false);
    d.setVisible(true);
    d.setSize(500, 200);
    //todo: 美化
    GameLabel label = new GameLabel(new TextBlock(message, 30));
    d.add(label);
  }
  public static void show(String message) {
    Message a = new Message(message);
    a.start();
  }
}