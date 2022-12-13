package network;

import gameController.OnlineGameController;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Network extends Thread{
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
  protected Socket connection;
  protected BufferedReader incoming;  // Stream for receiving data from server.
  protected PrintWriter outgoing;     // Stream for sending data to server.
  protected String messageOut;        // A message to be sent to the server.
  volatile protected String messageIn = "";         // A message received from the server.
  
  BufferedReader userInput; // A wrapper for System.in, for reading
  boolean flag;
  public boolean getFlag() {
    return flag;
  }
  public boolean isConnected() {
    return connection.isConnected();
  }
  Thread thread;
  volatile public boolean listening = false;
  volatile boolean isWorking = false;
  public String read() {
    System.out.println("reading!");
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
}
