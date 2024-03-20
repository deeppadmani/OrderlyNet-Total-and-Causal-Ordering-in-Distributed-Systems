import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

// Class for receiving network messages as a thread
public class NetworkReceiver extends Thread {
		
		BufferedReader ReaderObject;		// Buffered reader for reading messages from the network
		
		// Constructor to initialize the network receiver with a buffered reader
		public NetworkReceiver(BufferedReader ReaderObject) throws IOException
		{
			this.ReaderObject = ReaderObject;		// Initialize the buffered reader
		}
        
		// Method to run the network receiver thread
		public synchronized void run() {
			Random Rdelay = new Random();		// Random delay generator
			while (true) {
				try {		// Synchronize access to the buffered reader
						
						String MsgStr = this.ReaderObject.readLine();		// Read a message from the network
						
							
						// Create a message object from the received message string
						Message Msg = new Message(MsgStr);
						sleep(Rdelay.nextInt(5)+1);		// Introduce random delay before processing next
						
						if(Msg.MsgSeq == 0 &&  (NetworkSettings.SequencerSrc == NetworkSettings.NodeID))
						{
							if(false == NetworkSettings.LocalVectorClock.IsMsgbuffered(Msg))
							{
								System.out.println("addFirst in Seq: " +Msg.ObjtoString());
								NetworkSettings.SeqMsgbuffer.addFirst(Msg);
								NetworkSettings.LocalVectorClock.update(Msg.vc);
							}
							else
							{
								System.out.println("Message "+ Msg.ObjtoString() + " is Bufferd --> " + NetworkSettings.LocalVectorClock.toString());
								NetworkSettings.Msgbuffer.addLast(Msg);
							}
							// Check if there are messages in the buffer to process
							if(NetworkSettings.Msgbuffer.isEmpty() == false)
							{
								for(Iterator<Message> iterator = NetworkSettings.Msgbuffer.iterator(); iterator.hasNext();)
								{
									Message m = iterator.next();
									// Check if the message can be delivered based on vector clock comparison
									if(false == NetworkSettings.LocalVectorClock.IsMsgbuffered(m))
									{
										NetworkSettings.SeqMsgbuffer.addFirst(m);
										System.out.println("addFirst in Seq: " +m.ObjtoString());
										NetworkSettings.LocalVectorClock.update(m.vc);
										iterator.remove();			 // Remove the processed message from the buffer
									}
								}
							}
						}
						else{
							System.out.println("Msg Passed " + Msg.ObjtoString());
							NetworkSettings.LocalVectorClock.update(Msg.vc);
						
						}
					/* 	if(true == NetworkSettings.NwMsgTxDone()){
							Thread.interrupted();
						}*/
						
						}
					
					catch (Exception e) {
						System.out.println("Exception Raised while handling the message!");
						e.printStackTrace();
						break;
					}
			} 
		}
}