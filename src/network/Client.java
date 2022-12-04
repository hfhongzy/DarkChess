package network;

import javax.swing.*;
import java.net.*;
import java.io.*;

public class Client {
  //每次 new 一个新的，仅用于传输操作，不操作棋盘
  /**
   * 默认端口号
   */
  static final int DEFAULT_PORT = 1969;
  
  /**
   * 握手 String
   */
  static final String HANDSHAKE = "LX_AK_IOI!";
  
  /**
   * This character is prepended to every message that is sent.
   */
  static final char MESSAGE = '0';
  
  /**
   * This character is sent to the connected program when the user quits.
   */
  static final char CLOSE = '1';
  
  String computer;  // The computer where the server is running,
  // as specified on the command line.  It can
  // be either an IP number or a domain name.
  
  int port;   // The port on which the server listens.
  
  
  private Socket connection;      // For communication with the server.
  
  private BufferedReader incoming;  // Stream for receiving data from server.
  private PrintWriter outgoing;     // Stream for sending data to server.
  private String messageOut;        // A message to be sent to the server.
  private String messageIn;         // A message received from the server.
  
  BufferedReader userInput; // A wrapper for System.in, for reading
  
  boolean flag;
  public boolean getFlag() {
    return flag;
  }
  public Client(String[] args) {
    // lines of input from the user.
    flag = false;
      /* First, get the computer from the command line.
         Get the port from the command line, if one is specified,
         or use the default port if none is specified. */
    
    if (args.length == 0) {
      System.out.println("Usage:  java SimpleClient <computer-name> [<port>]");
      return;
    }
    
    computer = args[0];
    
    if (args.length == 1)
      port = DEFAULT_PORT;
    else {
      try {
        port= Integer.parseInt(args[1]);
        if (port <= 0 || port > 65535)
          throw new NumberFormatException();
      }
      catch (NumberFormatException e) {
        System.out.println("Illegal port number, " + args[1]);
        return;
      }
    }

      /* Open a connection to the server.  Create streams for
         communication and exchange the handshake. */
    
    try {
      System.out.println("Connecting to " + computer + " on port " + port);
      connection = new Socket(computer, port);
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
      flag = true;
    }
    catch (Exception e) {
      System.out.println("An error occurred while opening connection.");
      System.out.println(e.toString());
      return;
    }

      /* Exchange messages with the other end of the connection until one side or
         the other closes the connection.  This client program sends the first message.
         After that,  messages alternate strictly back and forth. */
    
  }  // end main()
  public String read() {
    try {
      while (true) {
        messageIn = incoming.readLine();
        if (messageIn.length() > 0) {
          if (messageIn.charAt(0) == CLOSE) {
            System.out.println("Connection closed at other end.");
            connection.close();
            break;
          }
          messageIn = messageIn.substring(1);
        }
        System.out.println("RECEIVED:  " + messageIn);
        return messageIn;
      }
    }
    catch (Exception e) {
      System.out.println("Sorry, an error has occurred.  Connection lost.");
      System.out.println(e.toString());
      System.exit(1);
    }
    return null;
  }
  public void send(String s) {
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