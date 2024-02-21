// File Name GreetingServer.java
import java.net.*;


import java.io.*;

public class ServerThread extends Thread {
   private ServerSocket serverSocket;
   int NoofClients;
   public ServerThread(int port) throws IOException {
      serverSocket = new ServerSocket(port);
      NoofClients = 0;
   }

   public void run() {
      Socket server;
      
      while(NoofClients < NetworkSettings.TotalNode-1) {
         try {
            System.out.println("Waiting for client on port " + 
            serverSocket.getLocalPort() + "...");
            server = serverSocket.accept();
            NoofClients++;
            
            BufferedReader ReaderObject = new BufferedReader(new InputStreamReader(server.getInputStream()));

            NetworkReceiver Client = new NetworkReceiver(ReaderObject);
            System.out.println("Let's Start NetworkReceiver ");
            new Thread(Client).start();
            
         } catch (IOException e) {
            e.printStackTrace();
         }
      }

   }
}
