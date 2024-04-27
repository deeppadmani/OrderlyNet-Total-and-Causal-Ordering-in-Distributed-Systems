
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class NetworkSettings 
{
    // Static variables to hold node-specific information
    static int NodeID = 0;
    static List <NetworkInfo> allServerNetworks;
    static List <NetworkInfo> allClientNetworks;
    static MsgHashTable Msgbuffer;
    static SequencerClass SeqMsgbuffer;
    static DataStorage dataStorage;
    static int TotalServerNode; //Total node of Network
    static int TotalClientNodes; //Total node of Network
    static int MessageCapacity; // Total No. of Messages
    static int SequencerSrc = -1;
    static Boolean []ServerSocketStatus;
    static Socket []allsocket;
    static PrintWriter []allsocketWriter;

    @SuppressWarnings("static-access")
    public NetworkSettings(int NodeID)
    {
        this.NodeID = NodeID;
        this.allServerNetworks = new ArrayList <NetworkInfo>();
        this.allClientNetworks = new ArrayList <NetworkInfo>();
        ConfigFileReader readInit = new ConfigFileReader("NetworkConfig.conf");
		readInit.ReadCofigFile();

        dataStorage = new DataStorage();
        allsocket = new Socket[TotalServerNode];
        allsocketWriter = new PrintWriter[TotalServerNode];
        SeqMsgbuffer = new SequencerClass();
        Msgbuffer = new MsgHashTable();
        ServerSocketStatus = new Boolean[TotalServerNode];
        Arrays.fill(this.ServerSocketStatus, false);
    }

    public synchronized void initProcess()
    {
        dataStorage.readMsgFromFile();
    }
    public synchronized static int getHashCode(int NodeId)
    {
        return NodeId % TotalServerNode;
    }

    public synchronized void CheckAllSockets() throws IOException
    {
        for(int id = 0;id < TotalServerNode ; id++) 
        {
            if(false == ServerSocketStatus[id])
            {
                    try{
                        allsocket[id] = new Socket(NetworkSettings.allServerNetworks.get(id).HostName, NetworkSettings.allServerNetworks.get(id).Port);
                        allsocketWriter[id] = new PrintWriter(allsocket[id].getOutputStream(),true);
                        System.out.println("Socket Connected !");
                        System.out.println("Just connected to " + allsocket[id].getRemoteSocketAddress() + "  Status : " + allsocket[id].isConnected());
                        ServerSocketStatus[id] = true;
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
                    
                    ServerSocketStatus[id] = true;
                    socket.close(); // Close the socket
                } catch (Exception e) {
                    allsocket[id].close();
                    allsocketWriter[id].close();
                    ServerSocketStatus[id] = false;
                }
            }
        }
    }

    // Method to start the network communication
    public synchronized void StartNetwork() throws InterruptedException 
    {
        Random Rdelay = new Random();
        List<Integer> serversToSend;

        try {
            // Starting Server of Resoective to Node(Ex C1=0,C2=1...) which provied in Commamd line argument
            Thread ServerThread = new ServerThread(allServerNetworks.get(NodeID).Port);
            ServerThread.start();
        }catch(Exception e){
            System.out.println("Error in starting server thread");
        }

            //Wait for server threads to start
            Thread.sleep(15000);

            for (int SocketIDX = 0; SocketIDX < NetworkSettings.TotalServerNode; SocketIDX++)  
            {
                try{
                    if(NetworkSettings.NodeID != SocketIDX)
                    { 
                        // Connect to other nodes Socket For writing data
                        allsocket[SocketIDX] = new Socket(NetworkSettings.allServerNetworks.get(SocketIDX).HostName, NetworkSettings.allServerNetworks.get(SocketIDX).Port);
                        allsocketWriter[SocketIDX] = new PrintWriter(allsocket[SocketIDX].getOutputStream(),true);
                        ServerSocketStatus[SocketIDX] = true;
                        System.out.println("Socket Connected !");
                        System.out.println("Just connected to " + allsocket[SocketIDX].getRemoteSocketAddress() + "  Status : " + allsocket[SocketIDX].isConnected());
                        
                    }
                } catch (IOException e) {
                    // Handle connection or IO exceptions
                    System.err.println("Failed to connect to node " + SocketIDX + ": " + e.getMessage());
                    // Optionally, you can log the exception or perform other error handling here
                }
            }
            Thread.sleep(3000);
            // Send messages to other nodes
            System.out.println("Network Setup Done!");
            NetworkValidator validator = new NetworkValidator(Const.HEARTBEAT_INTERVAL);
            validator.start();
            
            // Start the server

            while (true) {
              
            /*     try {
                    CheckAllSockets();
                } catch (IOException e) {
                    System.out.println("An error occurred while checking all sockets: " + e.getMessage());
                }*/
                if( NetworkSettings.SeqMsgbuffer.isEmpty() == false)
                {
                    Message Msg = NetworkSettings.SeqMsgbuffer.getLast();
                    String StringMsg = Msg.ObjtoString();
                    Thread.sleep(Rdelay.nextInt(10)+1);
                    System.out.println(StringMsg);
                    serversToSend = Msg.getServerNodeFromReplicaInfo();
                    int status = IsAllServerConnected(serversToSend);
                    if(1 >= status)
                    {
                        if(status == 1)
                            System.out.println("1 servers are not connected");
                        for(int dest:serversToSend)
                        {
                            if(dest != NodeID && isConnected(allsocket[dest]))
                            {
                                allsocketWriter[dest].println(StringMsg);
                                Thread.sleep(Rdelay.nextInt(10)+1);
                            }
                        }
                    }
                    else{
                        System.out.println("2 servers are not connected");
                    }  
                    NetworkSettings.SeqMsgbuffer.removeLast();
                }
            }
    }

    public static Boolean isConnected(Socket s)
    {
        boolean var = true;
        if(s == null)
        {
            var = false;
        }
        else if(!(s.isConnected()))
        {
            var = false;
        }
        return var;
    }

    public static int IsAllServerConnected(List<Integer> listOfServer)
    {
        int err=0;
        for(int id:listOfServer)
        {
            if(id != NetworkSettings.NodeID)
            {
                if(false == NetworkSettings.ServerSocketStatus[id])
                {
                    err++;
                }
                else{
                    System.out.println("Socket Connection id " + ": " + id +" ["+ NetworkSettings.ServerSocketStatus[id] + "]");
                }
            }
        }
        return err;
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
            int objNo;
            do
            {
                System.out.println("Enter your operation Read(1)/Write(2) : ");
                int operation = input.nextInt();
                if(Const.WRITE_OP == operation-1)
                {
                    System.out.println("Writing Process...!!");

                    System.out.println("Enter your Object No : ");
                    objNo = input.nextInt();
                    input.nextLine();
                    
                    System.out.println("Enter your Message : ");
                    msg = input.nextLine();

                    System.out.println("Enter Destination Server Number : ");
                    DestServer = input.nextInt();

                    Message ClientMsg = new Message(Const.WRITE_OP,DestServer,objNo,msg);

                    
                    int attempts = 0;
                    while(attempts < Const.MAX_CONNECTION_ATTEMPTS)
                    {
                        try {
                            @SuppressWarnings("resource")
                            Socket socket = new Socket();
                            socket.connect(new InetSocketAddress(NetworkSettings.allServerNetworks.get(DestServer).HostName, NetworkSettings.allServerNetworks.get(DestServer).Port));
                            if(socket.isConnected())
                            {
                                PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(),true);
                                System.out.println("Socket Connected !");
                                System.out.println("Just connected to " + socket.getRemoteSocketAddress());
                                Thread.sleep(3000);

                                socketWriter.println(ClientMsg.ObjtoString());
                                Thread.sleep(Rdelay.nextInt(10)+1);
                                socket.close();
                                break;
                            }
                        } catch (IOException e) {
                            // Handle connection or IO exceptions
                            System.err.println("Failed to connect: " + e.getMessage());
                        } catch (InterruptedException e) {
                            // Handle thread interruption exceptions
                            System.err.println("Thread interrupted: " + e.getMessage());
                            Thread.currentThread().interrupt(); // Preserve interrupted status
                        }
                        DestServer = getHashCode(DestServer + 2);
                        attempts++;
                    }
                    if (attempts == Const.MAX_CONNECTION_ATTEMPTS) {
                        System.out.println("Maximum connection attempts reached. Exiting...");
                    }

                }
                else if(Const.READ_OP == operation-1){
                    System.out.println("Reading Process...!!");
                    System.out.println("Enter your Object No : ");
                    objNo = input.nextInt();

                    System.out.println("Enter Source Server Number : ");
                    DestServer = input.nextInt();

                    @SuppressWarnings("resource")
                    Socket socket = new Socket(NetworkSettings.allServerNetworks.get(DestServer).HostName, NetworkSettings.allServerNetworks.get(DestServer).Port);
                    PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(),true);
                    System.out.println("Socket Connected !");
                    System.out.println("Just connected to " + socket.getRemoteSocketAddress());

                    Thread.sleep(3000);
                    String serverstr = "" + NetworkSettings.allClientNetworks.get(NodeID).HostName + " " + NetworkSettings.allClientNetworks.get(NodeID).Port;
                    Message ClientMsg = new Message(Const.READ_OP,DestServer,objNo,serverstr);
                    socketWriter.println(ClientMsg.ObjtoString());
                    Thread.sleep(Rdelay.nextInt(10)+1);

                    ServerSocket serverSocket = new ServerSocket(NetworkSettings.allClientNetworks.get(NodeID).Port);
                    System.out.println("Waiting for client on port " + 
                    serverSocket.getLocalPort() + "...");
                    socket = serverSocket.accept();

                    BufferedReader ReaderObject = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String MsgStr = ReaderObject.readLine();

                    Message m = new Message(MsgStr);
                    m.printRXMessages();

                    serverSocket.close();
                    socket.close();
                    socketWriter.close();
                    ReaderObject.close();
                    System.out.println("Socket Closed !");
                }
                else
                {
                    System.out.println("Wrong Option");
                }
                System.out.println("Want to add/read Another Msg: [Y/n]: ");
                op = input.next();
            }while(op.equals("y")||op.equals("Y"));
            input.close();
        }catch (ConnectException e) {
            // Handle connection refused exception
            System.err.println("Connection refused: " + e.getMessage()); 
        }
        catch (IOException e) {
            e.printStackTrace();
	    }   
    }
    
    public synchronized static void ReplayToClient(String DestServerInfo, Message m) throws InterruptedException, IOException
    {
        Random Rdelay = new Random();
        String []iponfig = DestServerInfo.split(" ");
        System.out.println("IP: " + iponfig[0] + "   PORT: " + iponfig[1]);
        Thread.sleep(Rdelay.nextInt(10)+1);
        Socket socket = new Socket(iponfig[0], Integer.parseInt(iponfig[1]));
        PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(),true);
        System.out.println("Socket Connected !");
        System.out.println("Just connected to " + socket.getRemoteSocketAddress());
        System.out.println("TX: "+m.ObjtoString());
        Thread.sleep(Rdelay.nextInt(10)+1);
        socketWriter.println(m.ObjtoString());
        Thread.sleep(Rdelay.nextInt(10)+1);
        socket.close();
    }
}
