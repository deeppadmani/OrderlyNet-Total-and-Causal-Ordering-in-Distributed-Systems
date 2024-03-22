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
		public void run() {
			Random Rdelay = new Random();		// Random delay generator
			while (true) {
				try {		// Synchronize access to the buffered reader
						
						String MsgStr = this.ReaderObject.readLine();		// Read a message from the network
						
							
						// Create a message object from the received message string
						Message Msg = new Message(MsgStr);
						sleep(Rdelay.nextInt(5)+1);		// Introduce random delay before processing next
						
						if(false == NetworkSettings.LocalVectorClock.IsMsgbuffered(Msg))
						{
							NetworkSettings.LocalVectorClock.update(Msg.vc);
							System.out.println("Message "+ Msg.ObjtoString() + " is Passed [Local Clock] --> " + NetworkSettings.LocalVectorClock.toString());
						}
						else
						{
							System.out.println("Message "+ Msg.ObjtoString() + " is Bufferd --> " + NetworkSettings.LocalVectorClock.toString());
							NetworkSettings.Msgbuffer.addLast(Msg);
						}
						// Check if there are messages in the buffer to process
					 	if(NetworkSettings.Msgbuffer.isEmpty() == false)
						{
							for(Message m:NetworkSettings.Msgbuffer)
							{
								// Check if the message can be delivered based on vector clock comparison
								if(false == NetworkSettings.LocalVectorClock.IsMsgbuffered(m))
								{
									
									System.out.println("Message "+ m.ObjtoString() + " is Passed & Removed from Message Buffer [Local Clock] --> " + NetworkSettings.LocalVectorClock.toString());
									NetworkSettings.LocalVectorClock.update(m.vc);
									NetworkSettings.Msgbuffer.remove(m);			 // Remove the processed message from the buffer
								}
							}
						}
						if(true == NetworkSettings.Msg100Done()){
							Thread.interrupted();
						}
						
						}
					
					catch (Exception e) {
						System.out.println("Exception Raised while handling the message!");
						e.printStackTrace();
						break;
					}
			} 
		}
}