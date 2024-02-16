import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Client implements Runnable{
    public Socket socket;
    private BufferedReader input = null;
    private BufferedReader out = null;

    public ServerThread(Socket socket)
    {
        this.socket = socket;
    }

    public void run()
    {
        try
        {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedReader(new InputStreamReader(System.in));
            String line = "";
 
            while (!socket.isClosed())
            {
                if(input.ready())
                {
                    String in = input.readLine();
                    if(null != in)
                    {
                        System.out.println(in);
                    }
                }

                if(out.ready())
                {
                    System.out.println("-->" + out.readLine());
                }
                
            }
            System.out.println("Closing connection");
 
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
    
}
