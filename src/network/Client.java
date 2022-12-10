package network;

import gameComponent.Chessboard;
import gameController.GameController;
import gameController.OnlineGameController;
import model.TeamColor;

import javax.swing.*;
import java.net.*;
import java.io.*;

public class Client extends Thread {
  
  public void setOnlineGameController(OnlineGameController onlineGameController) {
    this.onlineGameController = onlineGameController;
  }
  
  //每次 new 一个新的，仅用于传输操作，不操作棋盘
  OnlineGameController onlineGameController;
  
  
  static final int DEFAULT_PORT = 1969;
  static final String HANDSHAKE = "LX_AK_IOI!";
  static final char MESSAGE = '0';
  static final char CLOSE = '1';
  
  String computer;
  int port;
  private Socket connection;
  private BufferedReader incoming;  // Stream for receiving data from server.
  private PrintWriter outgoing;     // Stream for sending data to server.
  private String messageOut;        // A message to be sent to the server.
  volatile private String messageIn = "";         // A message received from the server.
  
  BufferedReader userInput; // A wrapper for System.in, for reading
  
  boolean flag;
  public boolean getFlag() {
    return flag;
  }
  private String serverIP;
  
  public Client() {
//    serverIP = JOptionPane.showInputDialog("请输入房主的 IP 地址").trim();
    serverIP = "10.13.212.153";
    flag = false;
    port = DEFAULT_PORT;
    try {
      System.out.println("Connecting to " + serverIP + " on port " + port);
      connection = new Socket(serverIP, port);
      incoming = new BufferedReader(
          new InputStreamReader(connection.getInputStream()) );
      outgoing = new PrintWriter(connection.getOutputStream());
      outgoing.println(HANDSHAKE);  // Send handshake to client.
      outgoing.flush();
      messageIn = incoming.readLine();  // Receive handshake from client.
      if (!messageIn.equals(HANDSHAKE) ) {
        throw new IOException("Connected program is not Darkchess!");
      }
      System.out.println("Connected.  Enter your first message.");
      messageIn = "";
      flag = true;
    }
    catch (Exception e) {
      System.out.println("An error occurred while opening connection.");
      System.out.println(e.toString());
      return;
    }
    if(getFlag()) {
      Message.show("成功连接！");
    } else {
      Message.show("连接失败，请重新连接。");
    }
  }
  volatile public boolean listening = false;
  volatile boolean isWorking = false;
  Thread thread;
  public void start() {
    thread = new Thread(this, "ClientMessage");
    thread.start();
  }
  public void run() {
    while(true) {
      try {
//        Thread.sleep(50); //节约资源
        if(!listening) {
//          System.out.println("Not listening");
          continue;
        }
        isWorking = true;
        messageIn = incoming.readLine();
        if (messageIn.length() == 0)
          continue;
        listening = false;
        
        if (messageIn.charAt(0) == CLOSE) {
          System.out.println("Connection closed at other end.");
          connection.close();
          messageIn = "quit";
          return ;
        }
        messageIn = messageIn.substring(1);
        
        System.out.println("RECEIVED:  " + messageIn);
        System.out.println("length" + String.valueOf(messageIn.length()));
        if(!Character.isAlphabetic(messageIn.charAt(messageIn.length() - 2))) {
          onlineGameController.getChessboard().moveChess(messageIn);
          if(onlineGameController.getMyColor() == null)
            onlineGameController.setMyColor(
                onlineGameController.getChessboard().playerStatus.getCurrentColor()
            );
          onlineGameController.changeTurn();
        }
        isWorking = false;
      } catch (Exception e) {
        System.out.println("Sorry, an error has occurred.");
        System.out.println(e.toString());
        System.exit(1);
      }
    }
  }
  public String read() {
    System.out.println("reading!");
    listening = true;
    while(messageIn.length() == 0 || isWorking) {
//      System.out.println("waiting.");
    }
    System.out.println("wow, " + messageIn);
    String ret = messageIn;
    messageIn = "";
    return ret;
  }
  public void send(String s) {
    System.out.println("Sending " + s);
    outgoing.println(MESSAGE + s);
    outgoing.flush();
  }
  public void quit() {
    try {
      System.out.println("Connection closed.");
      outgoing.println(CLOSE);
      outgoing.flush();
      connection.close();
    } catch (Exception e) {
      System.out.println("Sorry, an error has occurred.  Connection lost.");
      System.out.println(e.toString());
      System.exit(1);
    }
  }
}