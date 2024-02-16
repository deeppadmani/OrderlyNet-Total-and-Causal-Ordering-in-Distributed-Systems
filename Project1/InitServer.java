import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class InitServer extends NetworkSettings implements Runnable {
    
    public InitServer(int NetworkID)
    {
        super(NetworkID);
        
    }
		public void run() {
			System.out.println("InitServer: Start listening\n");
			while (!CheckSocketsExistance()) {
				 //System.out.println("InitListener: listening\n");
				try {
					String[] tempServer = Common.ADRESS1[NetworkID].split(" ");
                    System.out.println("ServerID: " + NetworkID + "    tempServer: "+ Integer.parseInt(tempServer[1]));
					ServerSocket Server = new ServerSocket(Integer.parseInt(tempServer[1]));
					Socket socket = Server.accept();
					 System.out.println("1 try");
					BufferedReader ServerReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					System.out.println("1");
					try {
						int Node = Integer.parseInt(ServerReader.readLine());

						System.out.println(
								"InitListener: Network #" + NetworkID + " received connection request from Network #" + Node + ".\n");
                        InsertSocket(Node, socket);// synchronized
						//setExistance(i);// synchronized
					} catch (Exception e) {
						e.getStackTrace();
					}
				} catch (Exception e) {

				}
			}
			System.out.println("InitListener: End listening\n");
		}
}
