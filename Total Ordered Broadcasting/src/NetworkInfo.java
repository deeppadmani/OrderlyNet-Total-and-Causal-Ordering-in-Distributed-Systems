// Class to store network information such as hostname and port number
public class NetworkInfo {
    String HostName ;       // Hostname of the network node
    int Port;               // Port number for communication

    // Constructor to initialize network information with provided hostname and port
    public NetworkInfo(String HostName,int Port)
    {
        this.HostName = HostName;       // Initialize the hostname
        this.Port = Port;               // Initialize the port number
    }

}
