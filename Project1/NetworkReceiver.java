import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;

public class NetworkReceiver extends Thread {
		
		BufferedReader ReaderObject;
		

		public NetworkReceiver(BufferedReader ReaderObject) throws IOException
		{
			System.out.println("In NetworkReceiver --> ");
			this.ReaderObject = ReaderObject;
		}
        
		public void run() {
			Random Rdelay = new Random();
			while (true) {
				try {
					synchronized(ReaderObject){
						
						String MsgStr = this.ReaderObject.readLine();
						
						Message Msg = new Message(MsgStr);
						//NetworkSettings.Msgbuffer.addLast(Msg);
						Msg.MsgPrint();
						sleep(Rdelay.nextInt(5)+1);
						
					/* 	if(NetworkSettings.Msgbuffer.isEmpty() == false)
						{
							for(Message m:NetworkSettings.Msgbuffer)
							{
								if(false == NetworkSettings.LocalVectorClock.IsMsgbuffered(m.vc))
								{
									NetworkSettings.LocalVectorClock.tick(m.NodeId);
									System.out.println("Message "+ m.ObjtoString() + " is Passed & Removed from Message Buffer [Local Clock] --> " + NetworkSettings.LocalVectorClock.toString());
									NetworkSettings.Msgbuffer.remove(m);
								}
							}
						}*/
						
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