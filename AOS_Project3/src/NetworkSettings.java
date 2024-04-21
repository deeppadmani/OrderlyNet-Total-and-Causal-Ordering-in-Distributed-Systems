
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

public class NetworkSettings 
{
    // Static variables to hold node-specific information
    static int NodeID = 0;
    static List <NetworkInfo> allServerNetworks;
    static List <NetworkInfo> allClientNetworks;
    static VectorClock LocalVectorClock;
    static LinkedList<Message> Msgbuffer = new LinkedList<Message>();
    static SequencerClass SeqMsgbuffer;
    static int TotalServerNode; //Total node of Network
    static int TotalClientNodes; //Total node of Network
    static int MessageCapacity; // Total No. of Messages
    static int SequencerSrc = -1;

    Socket []allsocket;
    PrintWriter []allsocketWriter;

    public NetworkSettings(int NodeID)
    {
        this.NodeID = NodeID;
        this.allServerNetworks = new ArrayList <NetworkInfo>();
        this.allClientNetworks = new ArrayList <NetworkInfo>();
        ConfigFileReader readInit = new ConfigFileReader("NetworkConfig.conf");
		readInit.ReadCofigFile();

        LocalVectorClock = new VectorClock(TotalServerNode);
        allsocket = new Socket[TotalServerNode];
        allsocketWriter = new PrintWriter[TotalServerNode];
        SeqMsgbuffer = new SequencerClass();
    }

    public synchronized int getHashCode(int NodeId)
    {
        return NodeId % TotalServerNode;
    }
    // Method to start the network communication
    public synchronized void StartNetwork() throws InterruptedException 
    {
        Random Rdelay = new Random();
        try
        { 
            // Starting Server of Resoective to Node(Ex C1=0,C2=1...) which provied in Commamd line argument
            Thread ServerThread = new ServerThread(allServerNetworks.get(NodeID).Port);
            ServerThread.start();

            //Wait for server threads to start
            Thread.sleep(15000);

            for (int SocketIDX = 0; SocketIDX < NetworkSettings.TotalServerNode; SocketIDX++)  
            {
                if(NetworkSettings.NodeID != SocketIDX)
                { 
                    // Connect to other nodes Socket For writing data
                    allsocket[SocketIDX] = new Socket(NetworkSettings.allServerNetworks.get(SocketIDX).HostName, NetworkSettings.allServerNetworks.get(SocketIDX).Port);
                    allsocketWriter[SocketIDX] = new PrintWriter(allsocket[SocketIDX].getOutputStream(),true);
                    System.out.println("Socket Connected !");
                    System.out.println("Just connected to " + allsocket[SocketIDX].getRemoteSocketAddress());
                }
            }
            Thread.sleep(3000);
            // Send messages to other nodes
            while (true) {
              
                
                if( NetworkSettings.SeqMsgbuffer.isEmpty() == false)
                {
                    Message Msg = NetworkSettings.SeqMsgbuffer.getLast();
                    String StringMsg = Msg.ObjtoString();
                    Thread.sleep(Rdelay.nextInt(10)+1);
                    System.out.println(StringMsg);
                    // Send the message to all other nodes
                    for (int SocketIDX = 1; SocketIDX < 3; SocketIDX++)  
                    {
                        Msg.dest = getHashCode(NodeID + (SocketIDX*2));
                        Msg.src = NodeID;
                        StringMsg = Msg.ObjtoString();
                        allsocketWriter[Msg.dest].println(StringMsg);

                        // Introduce random delay before sending the next message
                        Thread.sleep(Rdelay.nextInt(10)+1);
                    }
                    
                    NetworkSettings.SeqMsgbuffer.removeLast();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
	    }   
    }
    public synchronized void StartClient() throws InterruptedException
    {
        Random Rdelay = new Random();
        try
        { 
            String msg;
            int DestServer;
            String op;
            Scanner input = new Scanner(System.in);
            do
            {
                System.out.println("Enter your operation Read(1)/Write(2) : ");
                int operation = input.nextInt();
                if(2 == operation)
                {
                    System.out.println("Writing Process...!!");
                    System.out.println("Enter your Message : ");
                    msg = input.next();

                    System.out.println("Enter Destination Server Number : ");
                    DestServer = input.nextInt();

                    Message ClientMsg = new Message(operation-1,DestServer,msg);

                    Socket socket = new Socket(NetworkSettings.allServerNetworks.get(DestServer).HostName, NetworkSettings.allServerNetworks.get(DestServer).Port);
                    PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(),true);
                    System.out.println("Socket Connected !");
                    System.out.println("Just connected to " + socket.getRemoteSocketAddress());
                    Thread.sleep(3000);

                    socketWriter.println(ClientMsg.ObjtoString());
                    Thread.sleep(Rdelay.nextInt(10)+1);
                    socket.close();

                }
                else{
                    System.out.println("Reading Process...!!");

                }
                System.out.println("Want to sent Another Msg: [Y/n]: ");
                op = input.next();
            }while(op.equals("y")||op.equals("Y"));
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
	    }   
    }   
}
