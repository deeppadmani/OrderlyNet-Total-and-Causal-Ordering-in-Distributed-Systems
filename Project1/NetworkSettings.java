
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class NetworkSettings 
{
    int NetworkID = -1;
	int IsMsgSend = 0;
    int sentDone = 0;

    LinkedList<Message> Msgbuffer = new LinkedList<Message>();
    protected Socket[] sockets = new Socket[4];
    protected boolean[] IsSocketExist = new boolean[10];

    int[] Rx = new int[4]; // causal dependency info
	int[][] Tx = new int[4][4];// causal dependency info
    int CountOfTotalMsg = 0;
    
    public NetworkSettings(int NetworkID) {
		this.NetworkID = NetworkID;
        IsMsgSend = 0;
		
		if (NetworkID == 0)
			for (int node = 1; node < 4; node++)
            Msgbuffer.add(new Message(0, node));
	}

    protected synchronized void InsertSocket(int NodeID, Socket socket) {
		sockets[NodeID] = socket;
	}

    protected synchronized boolean CheckSocketsExistance() 
    {
        
		for (int SocNode = 0; SocNode < 10; SocNode++) {
			if (SocNode != NetworkID && (IsSocketExist[SocNode] == false)) 
            {
                System.out.println("CheckSocketsExistance false");
				return false;
			}
		}
        System.out.println("CheckSocketsExistance true");
		return true;
	}

    public void StartNetwork(int NetworkID) throws InterruptedException {
		InitServer Server = new InitServer(NetworkID);
        Thread SeverThread = new Thread();

    
        System.out.println("Server Started");
        Scanner sc = new Scanner(System.in);

        InitClients Clients = new InitClients(NetworkID);
        Thread ClientsThread = new Thread(Clients);

		SeverThread.start();
	    while (sc.nextInt() != 1) {
		}

        ClientsThread.start();
		while (!SeverThread.isInterrupted()) {
		}
		System.out.println("Network#" + NetworkID + ": End initialization.\n");

		NetworkTransmitter Sender = new NetworkTransmitter(NetworkID);
        Thread SenderThread = new Thread(Sender);
        SenderThread.start();

        NetworkReceiver Receiver = new NetworkReceiver(NetworkID);
        Thread ReceiverThread = new Thread(Receiver);
        ReceiverThread.start();

	}

}
