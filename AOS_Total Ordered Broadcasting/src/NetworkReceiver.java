import java.io.BufferedReader;
import java.io.IOException;
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
							NetworkSettings.SeqMsgbuffer.addFirst(Msg);
						}
						else{
							if(Msg.NodeId != NetworkSettings.NodeID){
								NetworkSettings.LocalVectorClock.tick(Msg.NodeId);
							}
							System.out.println("Msg Passed " + Msg.ObjtoString() + " with-Local Clock-> " + NetworkSettings.LocalVectorClock.toString());
						
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