// Java implementation of Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java

import java.io.*;
import java.net.*;

// Server class
public class Server 
{
    static ServerSocket serverSocket;
    static int port = 5555;
    public static void main(String[] args) {
        
        serverSocket = null;
        try{
            serverSocket = new ServerSocket(port);
        }catch(IOException e){
            System.out.println("ERROR: "+ port);
            System.exit(1);
        }
    }

    void RxClient()
    {
        while (true) {
            try{
                Socket socket = serverSocket.accept();
                ////////////
                ClientThread client = new ClientThread(socket);
                Thread thread = new Thread(client);
                thread.start();
                ////////////////
            }catch(IOException e){
                System.out.println("RxClient --> ERROR: "+ port);
                
            }
        }
    }
}
