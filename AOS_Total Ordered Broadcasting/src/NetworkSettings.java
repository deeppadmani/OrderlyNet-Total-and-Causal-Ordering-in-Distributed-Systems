
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class NetworkSettings 
{
    // Static variables to hold node-specific information
    static int NodeID = 0;
    static List <NetworkInfo> allNetworks;
    static VectorClock LocalVectorClock;
    static LinkedList<Message> Msgbuffer = new LinkedList<Message>();
    static LinkedList<Message> SeqMsgbuffer = new LinkedList<Message>();
    static int TotalNode; //Total node of Network
    static int MessageCapacity; // Total No. of Messages
    static int SequencerSrc = -1;
    static int MsgSeq = 0;

    Socket []allsocket;
    PrintWriter []allsocketWriter;

    public NetworkSettings(int NodeID)
    {
        this.NodeID = NodeID;
        ConfigFileReader readInit = new ConfigFileReader("NetworkConfig.conf");
		allNetworks = readInit.ReadCofigFile();

        LocalVectorClock = new VectorClock(TotalNode);
        allsocket = new Socket[TotalNode];
        allsocketWriter = new PrintWriter[TotalNode];
    }

    public static boolean NwMsgTxDone()
    {
        if(LocalVectorClock.clock[0] == (MessageCapacity*TotalNode) &&
        LocalVectorClock.clock[1] == (MessageCapacity*TotalNode)    &&
        LocalVectorClock.clock[2] == (MessageCapacity*TotalNode)    &&
        LocalVectorClock.clock[3] == (MessageCapacity*TotalNode))
            return true;
        return false;
    }
    // Method to start the network communication
    public synchronized void StartNetwork() throws InterruptedException 
    {
        int countSentMsg = 0;
        Random Rdelay = new Random();
        try
        { 
            // Starting Server of Resoective to Node(Ex C1=0,C2=1...) which provied in Commamd line argument
            Thread ServerThread = new ServerThread(allNetworks.get(NodeID).Port);
            ServerThread.start();

            //Wait for server threads to start
            Thread.sleep(10000);

            for (int SocketIDX = 0; SocketIDX < NetworkSettings.TotalNode; SocketIDX++)  
            {
                if(NetworkSettings.NodeID != SocketIDX)
                {
                    
                    // Connect to other nodes Socket For writing data
                    allsocket[SocketIDX] = new Socket(NetworkSettings.allNetworks.get(SocketIDX).HostName, NetworkSettings.allNetworks.get(SocketIDX).Port);
                    allsocketWriter[SocketIDX] = new PrintWriter(allsocket[SocketIDX].getOutputStream(),true);
                    System.out.println("Socket Connected !");
                    System.out.println("Just connected to " + allsocket[SocketIDX].getRemoteSocketAddress());
                }
            }
            Thread.sleep(3000);
            // Send messages to other nodes
            while (true) {
                
                if(countSentMsg < NetworkSettings.MessageCapacity )
                {
                    // Increment local vector clock
                    NetworkSettings.LocalVectorClock.tick(NodeID);
                    Message Msg = new Message(0,NetworkSettings.LocalVectorClock);
                    System.out.println("Add In SeqBuf: "+ Msg.ObjtoString());
                    NetworkSettings.SeqMsgbuffer.addFirst(Msg);
                    countSentMsg++;
                }
                Thread.sleep(10);
                if( NetworkSettings.SeqMsgbuffer.isEmpty() == false)
                {
                    Message Msg = NetworkSettings.SeqMsgbuffer.getLast();
            //    System.out.println(" TEST: "+ NetworkSettings.MsgSeq);
                    if(NetworkSettings.NodeID == NetworkSettings.SequencerSrc)
                    {
                            NetworkSettings.MsgSeq++;
                            Msg.MsgSeq = NetworkSettings.MsgSeq;
                            String StringMsg = Msg.ObjtoString();
                            System.out.println("Add Seq for Tx: "+ StringMsg);
                            Thread.sleep(Rdelay.nextInt(10)+1);

                            // Send the message to all other nodes
                            for (int SocketIDX = 0; SocketIDX < NetworkSettings.TotalNode; SocketIDX++)  
                            {
                                if(NetworkSettings.NodeID != SocketIDX)
                                {
                                    allsocketWriter[SocketIDX].println(StringMsg);
                                    System.out.println("Tx: "+ StringMsg);
                                    
                                }
                                // Introduce random delay before sending the next message
                                Thread.sleep(Rdelay.nextInt(10)+1);
                            }
                    }
                    else
                    {
                        String StringMsg = Msg.ObjtoString();
                        System.out.println("Send to Seq : "+ StringMsg);
                        allsocketWriter[SequencerSrc].println(StringMsg);
                        Thread.sleep(Rdelay.nextInt(10)+1);
                    }
                    NetworkSettings.SeqMsgbuffer.removeLast();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
	    }   
    }   
}
