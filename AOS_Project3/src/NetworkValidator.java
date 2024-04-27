import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkValidator extends Thread
{
    int heartbeatInterval;;

    public NetworkValidator(int heartbeatInterval)
    {
        this.heartbeatInterval = heartbeatInterval;
    }
    public synchronized void run() 
	{
        System.out.println("Network Validator Started !");
        while(true)
        {
            for(int id = 0;id < NetworkSettings.TotalServerNode ; id++) 
            {
                if(false == NetworkSettings.ServerSocketStatus[id])
                {
                        try{
                            NetworkSettings.allsocket[id] = new Socket(NetworkSettings.allServerNetworks.get(id).HostName, NetworkSettings.allServerNetworks.get(id).Port);
                            NetworkSettings.allsocketWriter[id] = new PrintWriter(NetworkSettings.allsocket[id].getOutputStream(),true);
                            System.out.println("Socket Connected !");
                            System.out.println("Just connected to " + NetworkSettings.allsocket[id].getRemoteSocketAddress() + "  Status : " + NetworkSettings.allsocket[id].isConnected());
                            NetworkSettings.ServerSocketStatus[id] = true;
                    } catch (IOException e) {
                        // Handle connection or IO exceptions
                        //System.err.println("Failed to connect to node " + id + ": " + e.getMessage());
                        // Optionally, you can log the exception or perform other error handling here
                    }
                }
                else{
                    try {
                        // Attempt to create a socket connection to the specified host and port
                        Socket socket = new Socket(NetworkSettings.allServerNetworks.get(id).HostName, NetworkSettings.allServerNetworks.get(id).Port);
                        
                        NetworkSettings.ServerSocketStatus[id] = true;
                        socket.close(); // Close the socket
                    } catch (Exception e) {
                        try {
                            NetworkSettings.allsocket[id].close();
                        } catch (IOException e1) {
                            System.out.println("An error occurred while closing the socket: " + e1.getMessage());
                        }
                        NetworkSettings.allsocketWriter[id].close();
                        NetworkSettings.ServerSocketStatus[id] = false;
                    }
                }
            }
            try {
                Thread.sleep(heartbeatInterval);
            } catch (InterruptedException e) {
                System.out.println("Sleep operation was interrupted: " + e.getMessage());
            }
        }
	}
}
