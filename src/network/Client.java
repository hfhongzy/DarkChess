package network;

import gameComponent.Chessboard;
import gameController.GameController;
import gameController.OnlineGameController;
import model.TeamColor;

import javax.swing.*;
import java.net.*;
import java.io.*;

public class Client extends Network {
  
  private String serverIP;
  
  public Client() {
    serverIP = JOptionPane.showInputDialog("请输入房主的 IP 地址").trim();
//    serverIP = "10.13.212.153";
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
  }
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
          /*
          if(onlineGameController.getMyColor() == null)
            onlineGameController.setMyColor(
                onlineGameController.getChessboard().playerStatus.getCurrentColor()
            );
            
           */
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