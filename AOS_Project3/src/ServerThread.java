// File Name GreetingServer.java
import java.net.*;
import java.io.*;

// Class for handling server functionality as a thread
public class ServerThread extends Thread {
   private ServerSocket serverSocket;     // Server socket for accepting client connections
   int NoofClients;        // Variable to keep track of the number of connected clients

   // Constructor to initialize the server socket with the provided port
   public ServerThread(int port) throws IOException {
      serverSocket = new ServerSocket(port);
      NoofClients = 0;
      System.out.println("Waiting for client on port " + 
            serverSocket.getLocalPort() + "...");
   }

   // Method to run the server thread
   public void run() 
   {
      Socket server;
      
      // Loop until all expected clients have connected
      while(true) {
         try {
          //  System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
            server = serverSocket.accept();

            InetSocketAddress remoteSocketAddress = (InetSocketAddress) server.getRemoteSocketAddress();
            //System.out.println("Just connected to " + remoteSocketAddress.getAddress());
            
            NoofClients++;
            
            // Create a buffered reader to read data from the client
            BufferedReader ReaderObject = new BufferedReader(new InputStreamReader(server.getInputStream()));

            // Create a network receiver object to handle communication with the client
            NetworkReceiver Client = new NetworkReceiver(ReaderObject);
            
            new Thread(Client).start(); //// Start the network receiver in a new thread
            
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
}
