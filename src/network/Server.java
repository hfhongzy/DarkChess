package network;

import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.Enumeration;

/**
 * This program is one end of a simple command-line interface chat program.
 * It acts as a server which waits for a connection from the CLChatClient
 * program.  The port on which the server listens can be specified as a
 * command-line argument.  If it is not, then the port specified by the
 * constant DEFAULT_PORT is used.  Note that if a port number of zero is
 * specified, then the server will listen on any available port.
 * This program only supports one connection.  As soon as a connection is
 * opened, the listening socket is closed down.  The two ends of the connection
 * each send a HANDSHAKE string to the other, so that both ends can verify
 * that the program on the other end is of the right type.  Then the connected
 * programs alternate sending messages to each other.  The client always sends
 * the first message.  The user on either end can close the connection by
 * entering the string "quit" when prompted for a message.  Note that the first
 * character of any string sent over the connection must be 0 or 1; this
 * character is interpreted as a command.
 */
public class Server {
  
  /**
   * Port to listen on, if none is specified on the command line.
   */
  static final int DEFAULT_PORT = 1728;
  
  /**
   * Handshake string. Each end of the connection sends this  string to the
   * other just after the connection is opened.  This is done to confirm that
   * the program on the other side of the connection is a CLChat program.
   */
  static final String HANDSHAKE = "CLChat";
  
  /**
   * This character is prepended to every message that is sent.
   */
  static final char MESSAGE = '0';
  
  /**
   * This character is sent to the connected program when the user quits.
   */
  static final char CLOSE = '1';
  int port;   // The port on which the server listens.
  
  ServerSocket listener;  // Listens for a connection request.
  Socket connection;      // For communication with the client.
  
  BufferedReader incoming;  // Stream for receiving data from client.
  PrintWriter outgoing;     // Stream for sending data to client.
  String messageOut;        // A message to be sent to the client.
  String messageIn;         // A message received from the client.
  
  BufferedReader userInput; // A wrapper for System.in, for reading
  // lines of input from the user.
  boolean flag;
  public boolean getFlag() {
    return flag;
  }
  public static String getIP() {
    try {
      Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
      while (nifs.hasMoreElements()) {
        NetworkInterface nif = nifs.nextElement();
        Enumeration<InetAddress> address = nif.getInetAddresses();
        while (address.hasMoreElements()) {
          InetAddress addr = address.nextElement();
          if (addr instanceof Inet4Address) {
            if(nif.getName().equals("en0")) {
              JOptionPane.showMessageDialog(null, "OK!");
              return addr.getHostAddress();
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  public Server(String[] args) {
    flag = false;
    if (args.length == 0)
      port = DEFAULT_PORT;
    else {
      try {
        port= Integer.parseInt(args[0]);
        if (port < 0 || port > 65535)
          throw new NumberFormatException();
      }
      catch (NumberFormatException e) {
        System.out.println("Illegal port number, " + args[0]);
        return;
      }
    }

    
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
        throw new Exception("Connected program is not a CLChat!");
      }
      System.out.println("Connected.  Waiting for the first message.");
      flag = true;
    }
    catch (Exception e) {
      System.out.println("An error occurred while opening connection.");
      System.out.println(e.toString());
      return;
    }
  }
  
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
    outgoing.println(MESSAGE + messageOut);
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