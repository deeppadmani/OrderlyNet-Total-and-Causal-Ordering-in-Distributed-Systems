import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

public class InitClients extends NetworkSettings implements Runnable
{
    public InitClients(int NetworkID)
    {
        super(NetworkID);
    }
    public void run() {
			try {
                System.out.println("InitClients");
				for (int NodeIDX = 0; NodeIDX < 4; NodeIDX++) {
                    if(NetworkID != NodeIDX)
                    {
                        System.out.println("InitConnector: Site #" + NetworkID + " tries to connect site #" + NodeIDX + ".\n");
                        String[] tempAdrs = Common.ADRESS1[NodeIDX].split(" ");
                        String ipaddress = tempAdrs[0];
                        int port = Integer.parseInt(tempAdrs[1]);
                        Socket socket = new Socket(ipaddress, port);// blocked??
                        System.out.println("InitConnector: Site #" + NetworkID + " has connected to site #" + NodeIDX + ".\n");
                        Writer writer = new OutputStreamWriter(socket.getOutputStream());
                        writer.write(NetworkID + "\n");
                        writer.flush();
                        InsertSocket(NodeIDX, socket);// synchronized
                        //setExistance(NodeIDX);// synchronized
                    }
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				System.out.println("InitConnector:  End connecting.\n");
				Thread.currentThread().interrupt();
			}
		}
    
}
