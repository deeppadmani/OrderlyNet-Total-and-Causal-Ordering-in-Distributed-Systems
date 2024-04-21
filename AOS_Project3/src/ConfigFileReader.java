import java.io.FileReader;
import java.util.Properties;

public class ConfigFileReader {
    String Filename;

    public ConfigFileReader(String Fname)
    {
        this.Filename = Fname;
    }

    void ReadCofigFile()
    {
        // Create a Properties object
        Properties properties = new Properties();

        // Load the .conf file into the Properties object
        try {
            FileReader reader = new FileReader(this.Filename);
            properties.load(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String TotalserverNode = properties.getProperty("TotalServerNodes");
        String TotalClientNodes = properties.getProperty("TotalClientNodes");
        String AllServerIps = properties.getProperty("ServerNetworkNodes");
        String AllSClientIps = properties.getProperty("ClientNetworkNodes");

        NetworkSettings.TotalServerNode = Integer.parseInt(TotalserverNode);
        NetworkSettings.TotalClientNodes = Integer.parseInt(TotalClientNodes);

        String []ServerNetworks =  AllServerIps.split(",");
        String []ClientNetworks =  AllSClientIps.split(",");

        for(String net:ServerNetworks)
        {
            String []iponfig = net.split(" ");
            System.out.println("SERVER IP: "+ iponfig[0] + "  PORT: "+ iponfig[1]);
            NetworkSettings.allServerNetworks.add(new NetworkInfo(iponfig[0],Integer.parseInt(iponfig[1])));
        }
        
        for(String net:ClientNetworks)
        {
            String []iponfig = net.split(" ");
            System.out.println("Client IP: "+ iponfig[0] + "  PORT: "+ iponfig[1]);
            NetworkSettings.allClientNetworks.add(new NetworkInfo(iponfig[0],Integer.parseInt(iponfig[1])));
        }
    }
}
