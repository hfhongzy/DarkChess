package network;

import gameComponent.Chessboard;
import gameController.OnlineGameController;
import model.PanelType;

import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Queue;

public class Server extends Thread{
  
  public void setOnlineGameController(OnlineGameController onlineGameController) {
    this.onlineGameController = onlineGameController;
  }
  OnlineGameController onlineGameController;
  static final int DEFAULT_PORT = 1969;
  
  static final String HANDSHAKE = "LX_AK_IOI!";
  
  static final char MESSAGE = '0';

  static final char CLOSE = '1';
  int port;
  
  ServerSocket listener;
  Socket connection;
  
  BufferedReader incoming;
  PrintWriter outgoing;
  String messageOut;
  volatile String messageIn = "";
  
  BufferedReader userInput;
  
  Thread thread;
  // lines of input from the user.
  boolean flag;
  volatile public boolean listening = false;
  public boolean getFlag() {
    return flag;
  }
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
    return res;
  }
  public Server() {
    Message.show("Share your IP: " + getIP());
    flag = false;
    port = DEFAULT_PORT;
    String IP = Server.getIP();
  
    try {
      listener = new ServerSocket(port);
      System.out.println("Listening on port " + listener.getLocalPort());
      connection = listener.accept();
      listener.close();
      incoming = new BufferedReader(
          new InputStreamReader(connection.getInputStream()) );
      outgoing = new PrintWriter(connection.getOutputStream());
      outgoing.println(HANDSHAKE);  // Send handshake to client.
      outgoing.flush();
      messageIn = incoming.readLine();  // Receive handshake from client.
      if (! HANDSHAKE.equals(messageIn) ) {
        throw new Exception("Connected program is not a DarkChess!");
      }
      System.out.println("Connected.  Waiting for the first message.");
      messageIn = "";
      flag = true;
    }
    catch (Exception e) {
      System.out.println("An error occurred while opening connection.");
      System.out.println(e.toString());
      return;
    }
  
    if(getFlag()) {
      Message.show( "成功连接！");
    } else {
      Message.show( "连接失败，请重新链接。");
    }
  }
  public void start() {
    if(thread == null) {
      thread = new Thread(this, "ServerThread");
      thread.start();
    }
  }
  volatile boolean isWorking = false; //while 循环在执行，吧 messageIn 锁死
  public void run() {
    while(true) {
      try {
//        Thread.sleep(200); //节约资源
        if(!listening) continue;
        isWorking = true;
        messageIn = incoming.readLine();
        if(messageIn.length() == 0)
          continue;
        listening = false;
        if (messageIn.charAt(0) == CLOSE) {
          System.out.println("Connection closed at other end.");
          connection.close();
          return ;
        }
       
        messageIn = messageIn.substring(1);
        System.out.println("RECEIVED:  " + messageIn);
        if(!Character.isAlphabetic(messageIn.charAt(messageIn.length() - 2))) {
          onlineGameController.getChessboard().moveChess(messageIn);
          onlineGameController.changeTurn();
        }
        isWorking = false;
      }
      catch (Exception e) {
        System.out.println("Sorry, an error has occurred.");
        System.out.println(e.toString());
        System.exit(1);
      }
    }
  }
  
  public String read() {
    listening = true;
    while(messageIn.length() == 0 || isWorking);
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
  
} //end class CLChatServer