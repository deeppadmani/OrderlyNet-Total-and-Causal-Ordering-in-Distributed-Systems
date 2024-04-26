import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Random;

// Class for receiving network messages as a thread
public class NetworkReceiver extends Thread 
{
		
		BufferedReader ReaderObject;		// Buffered reader for reading messages from the network
		
		// Constructor to initialize the network receiver with a buffered reader
		public NetworkReceiver(BufferedReader ReaderObject) throws IOException
		{
			this.ReaderObject = ReaderObject;		// Initialize the buffered reader
		}
        
		// Method to run the network receiver thread
		public synchronized void run() 
		{
			Random Rdelay = new Random();		// Random delay generator
			while (true)
			{
				try {		// Synchronize access to the buffered reader
						
						String MsgStr = this.ReaderObject.readLine();		// Read a message from the network
						if(null != MsgStr)	
						{
							// Create a message object from the received message string
							Message Msg = new Message(MsgStr);
							sleep(Rdelay.nextInt(5)+1);		// Introduce random delay before processing next
							if(Msg.RW == Const.WRITE_OP)
							{
								if(Msg.MsgId == Const.NULL)
								{
									List<Integer> serversToSend = Msg.getServerNodeFromReplicaInfo();
									if (2 > NetworkSettings.IsAllServerConnected(serversToSend))
									{
										NetworkSettings.dataStorage.writeMsginFile(Msg.objNo, MsgStr);
										NetworkSettings.Msgbuffer.addMsg(Msg.objNo, Msg.Msg);
										NetworkSettings.SeqMsgbuffer.SeqIncrement();
										Msg.MsgId = NetworkSettings.SeqMsgbuffer.getSeq();
										NetworkSettings.SeqMsgbuffer.addFirst(Msg);
									}
									else{
										System.out.println("Error: 2 servers are not connected");
									}
								}
								else
								{
									System.out.println(Msg.ObjtoString());
									NetworkSettings.dataStorage.writeMsginFile(Msg.objNo, MsgStr);
									NetworkSettings.Msgbuffer.addMsg(Msg.objNo, Msg.Msg);
								}	
							}
							else if(Msg.RW == Const.READ_OP)
							{
								String readObjMsgs = NetworkSettings.Msgbuffer.readObj(Msg.objNo);
								Message m = new Message(Const.OP_MAX,Msg.src,Msg.objNo,readObjMsgs);
								sleep(Rdelay.nextInt(5)+1);
								NetworkSettings.ReplayToClient(Msg.Msg,m);
							}
						}
					}	
			catch (Exception e) 
			{
				System.out.println("Exception Raised while handling the message!");
				e.printStackTrace();
				break;
			}
		}
	}
}