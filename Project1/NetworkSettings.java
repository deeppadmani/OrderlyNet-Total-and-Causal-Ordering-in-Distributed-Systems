
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class NetworkSettings 
{
    static int NodeID = 0;
    static List <NetworkInfo> allNetworks;
    static VectorClock LocalVectorClock;
    static LinkedList<Message> Msgbuffer = new LinkedList<Message>();
    static int TotalNode;
    Socket []allsocket;
    PrintWriter []allsocketWriter;

    public NetworkSettings(int NodeID)
    {
        this.NodeID = NodeID;
        TotalNode = 2;
        allNetworks = new ArrayList <NetworkInfo>();
        LocalVectorClock = new VectorClock(TotalNode);
        
        allNetworks.add(new NetworkInfo("127.0.0.1",6066));
        allNetworks.add(new NetworkInfo("127.0.0.1",6067));
        allNetworks.add(new NetworkInfo("127.0.0.1",6068));
        allNetworks.add(new NetworkInfo("127.0.0.1",6069));
        allsocket = new Socket[TotalNode];
        allsocketWriter = new PrintWriter[TotalNode];
        
    }

    public void StartNetwork() throws InterruptedException 
    {
        int countSentMsg = 0;
        Random Rdelay = new Random();
        try
        { 
            Thread ServerThread = new ServerThread(allNetworks.get(NodeID).Port);
            ServerThread.start();

            Thread.sleep(10000);
              for (int SocketIDX = 0; SocketIDX < NetworkSettings.TotalNode; SocketIDX++)  
                {
                    if(NetworkSettings.NodeID != SocketIDX)
                    {
                        allsocket[SocketIDX] = new Socket(NetworkSettings.allNetworks.get(SocketIDX).HostName, NetworkSettings.allNetworks.get(SocketIDX).Port);
                        allsocketWriter[SocketIDX] = new PrintWriter(allsocket[SocketIDX].getOutputStream(),true);
                        System.out.println("Socket Connected !");
                        System.out.println("Just connected to " + allsocket[SocketIDX].getRemoteSocketAddress());
                    }
                }
            while (countSentMsg != 10) {
                synchronized(NetworkSettings.LocalVectorClock)
                {
                    NetworkSettings.LocalVectorClock.tick(NodeID);
                }
                Message Msg = new Message(countSentMsg+1,NetworkSettings.LocalVectorClock);
                String StringMsg = Msg.ObjtoString();
                
                for (int SocketIDX = 0; SocketIDX < NetworkSettings.TotalNode; SocketIDX++)  
                {
                    if(NetworkSettings.NodeID != SocketIDX)
                    {
                       allsocketWriter[SocketIDX].println(StringMsg);
                       System.out.println(StringMsg);
                        
                    }
                    Thread.sleep(Rdelay.nextInt(10)+1);
                    
                }
                countSentMsg++;
            }
        } catch (IOException e) {
            
            e.printStackTrace();
	    }   
    }
    
   
    
}
