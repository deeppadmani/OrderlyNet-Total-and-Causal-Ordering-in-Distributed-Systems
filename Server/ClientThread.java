import java.io.*;
import java.net.*;

public class ClientThread extends Server implements Runnable{

    public Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;
    public ClientThread(Socket socket)
    {
        this.socket = socket;
    }

    public void run()
    {
        try {
        // takes input from terminal
        input = new DataInputStream(System.in);
        
        // sends output to the socket
        out = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException u) {
            System.out.println(u);
            return;
        }

        String line = "";
        
        while (!line.equals("Over")) {
            try {
                line = input.readLine();
                out.writeUTF(line);
            }
            catch (IOException i) {
                System.out.println(i);
            }
        }
        try {
            input.close();
            out.close();
            socket.close();
        }
        catch (IOException i) {
            System.out.println(i);
        }
    }
}
