import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.Random;

public class NetworkReceiver extends NetworkSettings implements Runnable {
		private ObjectInputStream in;
		private LinkedList<Message> buffer1 = new LinkedList<Message>();
		private Random rdm1 = new Random(233);

        public NetworkReceiver(int NetworkID)
        {
            super(NetworkID);
        }
		public void run() {
			while (true) {
				try {
					if (NetworkID == 0 && sentDone == 10) {
						System.out.println(
								"Site 0: All sites have sent 100 messages. Receiver thread will be interrupted right now!\n");
						//System.out.println(outputResults);// output data
															// results.
                        Thread.currentThread().interrupt();
					}

					for (int i = 0; i != NetworkID && i < 10; i++) {
						in = new ObjectInputStream(sockets[i].getInputStream());
						Object temp = in.readObject();
						if (((Integer) temp).intValue() == 0) // if received a
																// flag msg
																// which means
																// other one
																// site has sent
																// 100 msgs.
							sentDone++;
						else
							buffer1.addLast((Message) temp);
					}

					if (buffer1.isEmpty())
						continue;
					Message msgTemp = buffer1.removeFirst();
					Thread.sleep(rdm1.nextInt(151) + 50); // requirement t

					// causal ordering algorithm implementation
					int[][] sentTemp = msgTemp.sent;
					if ((Rx[msgTemp.Src] + 1) == sentTemp[msgTemp.Src][NetworkID])
						for (int k = 0; k < 10 && k != msgTemp.Src; k++)
							if (Rx[k] >= sentTemp[k][NetworkID]) {

								for (int i = 0; i != NetworkID && i < 10; i++)// broadcast
									Msgbuffer.addLast(new Message(msgTemp, NetworkID, i));

								/*
								 * //multi-cast int rdmAmount =
								 * rdm1.nextInt(10)+1; int[] checkDupl = new
								 * int[10]; int tmpCounter = 0; while(tmpCounter
								 * <= rdmAmount){ int ttemp=rdm1.nextInt(10);
								 * if(ttemp != ID&& checkDupl[ttemp]!=1){
								 * tmpCounter++; checkDupl[ttemp] = 1;
								 * buffer.addLast(new Message(msgTemp, ID,
								 * ttemp)); } }
								 */
								System.out.println("Site#" + NetworkID + ": A msg is ready to be delivered.\n");
								Rx[msgTemp.Src]++;
								for (int i = 0; i < 10; i++)
									for (int j = 0; j < 10; j++)
										Tx[i][j] = (Tx[i][j] < sentTemp[i][j]) ? sentTemp[i][j] : Tx[i][j];
							} else
								buffer1.addLast(msgTemp);
					else
						buffer1.addLast(msgTemp);
				} catch (Exception e) {

				}

			}
		}

	}
