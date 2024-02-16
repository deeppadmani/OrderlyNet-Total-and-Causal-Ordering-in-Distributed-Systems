import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.Date;

public class NetworkTransmitter extends NetworkSettings implements Runnable {

		private ObjectOutputStream out;
		private Random rdm = new Random(233);
        public NetworkTransmitter(int NetworkID)
        {
            super(NetworkID);
        }
		public void run() 
        {
			try {
				while (true) {
					if (CountOfTotalMsg == 100) {
						if (NetworkID != 0) {
							ObjectOutputStream w = new ObjectOutputStream(sockets[0].getOutputStream());
							w.writeObject(new Integer(0));
						} else
							sentDone++;
						System.out.println(
								"site " + NetworkID + " has sent 100 messages, and it will be interrupted right now!\n");
                        Thread.currentThread().interrupt();
					} // check if send thread complete

					// first, send all messages in buffer(buffer is a FIFO
					// buffer)
					Thread.sleep(3000);

					while (!Msgbuffer.isEmpty()) {
						Message tempMsg = Msgbuffer.removeFirst();

						// causal ordering algorithm implementation
						out = new ObjectOutputStream(sockets[tempMsg.Dest].getOutputStream());
						Thread.sleep(rdm.nextInt(81) + 20);
						tempMsg.TimeStamp = new Date();
						Tx[NetworkID][tempMsg.Dest]++;
						tempMsg.sent = Tx;
						out.writeObject(tempMsg);
						System.out.println("Site#" + NetworkID + ": Sent a msg to site#" + tempMsg.Dest + ".\n");
						CountOfTotalMsg++;
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// do nothing
			}
		}    
}
