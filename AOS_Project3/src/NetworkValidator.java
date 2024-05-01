import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

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
                if(id != NetworkSettings.NodeID)
                {
                    if(false == NetworkSettings.ServerSocketStatus[id])
                    {
                            try{
                                NetworkSettings.allsocket[id] = new Socket(NetworkSettings.allServerNetworks.get(id).HostName, NetworkSettings.allServerNetworks.get(id).Port);
                                NetworkSettings.allsocketWriter[id] = new PrintWriter(NetworkSettings.allsocket[id].getOutputStream(),true);
                                System.out.println("Socket Connected !");
                                System.out.println("Just connected to " + NetworkSettings.allsocket[id].getRemoteSocketAddress() + "  Status : " + NetworkSettings.allsocket[id].isConnected() + "   ID : "+ id);
                                NetworkSettings.ServerSocketStatus[id] = true;
                        } catch (IOException e) {
                            // Handle connection or IO exceptions
                            //System.err.println("Failed to connect to node " + id + ": " + e.getMessage());
                            // Optionally, you can log the exception or perform other error handling here
                        }
                    }
                    else{
                            try{
                                Message m = new Message(Const.HEARTBEAT_OP,id,-1,Const.HEARTBEAT_STR);
                                NetworkSettings.allsocketWriter[id].println(m.ObjtoString());
                                NetworkSettings.allsocket[id].setSoTimeout(Const.CONNECTION_TIMEOUT);
                                
                            }catch (SocketException e) {
                                NetworkSettings.ServerSocketStatus[id]= false;
                                System.out.println("Socket["+id+"] Closed!");;
                            }
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
