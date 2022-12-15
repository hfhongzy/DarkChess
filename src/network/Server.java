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

public class Server extends Network{
  public boolean isListen() {
    return listen;
  }
  
  private boolean listen;
  ServerSocket listener;
  
  @Override
  public void interrupt() {
    try {
      listener.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    super.interrupt();
  }
  
  public void construct() {
//    String IP = Server.getIP();
//    System.out.println("Share your IP: " + IP);
    
    flag = false;
    listen = false;
    port = DEFAULT_PORT;
    
    try {
      listen = true;
      listener = new ServerSocket(port, 1);
      
      System.out.println("Listening on port " + listener.getLocalPort());
      try {
        connection = listener.accept();
      } catch (Exception e) {
        System.out.println("Interrupted.");
        return ;
      }
      listener.close();
      listen = false;
      incoming = new BufferedReader(
          new InputStreamReader( connection.getInputStream()) );
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
    
    /*
    if(getFlag()) {
      Message.show( "成功连接！");
    } else {
      Message.show( "连接失败，请重新链接。");
    }
    
     */
   
    
  }
  public void start() {
    if(thread == null) {
      thread = new Thread(this, "ServerThread");
      thread.start();
    }
  }
  public void run() {
    while(true) {
      try {
//        Thread.sleep(50); //节约资源
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