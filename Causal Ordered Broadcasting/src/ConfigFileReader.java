import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigFileReader {
    String Filename;

    public ConfigFileReader(String Fname)
    {
        this.Filename = Fname;
    }

    List <NetworkInfo> ReadCofigFile()
    {
        List <NetworkInfo> allNetworks;
        allNetworks = new ArrayList <NetworkInfo>();
        // Create a Properties object
        Properties properties = new Properties();

        // Load the .conf file into the Properties object
        try {
            FileReader reader = new FileReader(this.Filename);
            properties.load(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String TotalNode = properties.getProperty("TotalNodes");

        String AllIps = properties.getProperty("NetworkNodes");

        NetworkSettings.TotalNode = Integer.parseInt(TotalNode);

        String []Networks =  AllIps.split(",");

        for(String net:Networks)
        {
            String []iponfig = net.split(" ");
            System.out.println("IP: "+ iponfig[0] + "  PORT: "+ iponfig[1]);
            allNetworks.add(new NetworkInfo(iponfig[0],Integer.parseInt(iponfig[1])));
        }
        return allNetworks;
    }
}
