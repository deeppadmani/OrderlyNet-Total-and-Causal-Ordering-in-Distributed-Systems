import java.io.BufferedReader;
import java.io.IOException;
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
		public synchronized void run() {
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
							if(Msg.MsgId == 0)
							{
								NetworkSettings.Msgbuffer.addFirst(Msg);
								NetworkSettings.SeqMsgbuffer.SeqIncrement();
								Msg.MsgId = NetworkSettings.SeqMsgbuffer.getSeq();
								NetworkSettings.SeqMsgbuffer.addFirst(Msg);
							}
							else{
								System.out.println(Msg.ObjtoString());
								NetworkSettings.Msgbuffer.addFirst(Msg);
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