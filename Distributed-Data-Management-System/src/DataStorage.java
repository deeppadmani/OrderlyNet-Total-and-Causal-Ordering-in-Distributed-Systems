import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataStorage 
{
    String filename;
    JSONObject jsonObject;
    JSONParser jsonParser;

    public DataStorage()
    {
        this.filename = "../JSON/MessageJSON_" + NetworkSettings.NodeID + ".json";
        this.jsonObject = new JSONObject();
        this.jsonParser = new JSONParser();
    }

    @SuppressWarnings("unchecked")
    public void writeMsginFile(int key,String m)
    {
        JSONArray existingData = readExistingDataFromFile();
        jsonObject.put("key", key);
        jsonObject.put("Message", m);

        JSONObject msgObj = new JSONObject(); 
        msgObj.put("MessageObject", jsonObject);

        existingData.add(msgObj);

        try (FileWriter file = new FileWriter(filename)) {
            file.write(existingData.toString());
            System.out.println("Data has been written to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void readMsgFromFile()
    {
        try
        {
            File file = new File(filename);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
                FileWriter writer = new FileWriter(file);
                writer.write("[]");
                writer.close();
            } else {
                System.out.println("File already exists.");
                
                FileReader reader = new FileReader(filename);
                //Read JSON file
                Object obj = jsonParser.parse(reader);
                JSONArray msgList = (JSONArray) obj;
                msgList.forEach( msg -> parseMsgObject( (JSONObject) msg ) );
                
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void parseMsgObject(JSONObject msg) 
    {
        if (msg.containsKey("MessageObject")) {
            JSONObject msgObject = (JSONObject) msg.get("MessageObject");
            
            // Check if the "msgObject" is not null
            if (msgObject != null) {
                int key = ((Number) msgObject.get("key")).intValue();    
                System.out.println(key);
    
                String message = (String) msgObject.get("Message");  
                System.out.println(message);
                Message m = new Message(message);
                // Assuming NetworkSettings.Msgbuffer is your message buffer
                NetworkSettings.Msgbuffer.addMsg(key, m.Msg);
                
            } else {
                System.err.println("MessageObject is null");
            }
        } else {
            System.err.println("MessageObject key not found in JSON object");
        }
         
    }

    private JSONArray readExistingDataFromFile() {
        JSONArray  existingData = new JSONArray();

        try (FileReader reader = new FileReader(filename)) {
            // Parse existing JSON file
            Object obj = jsonParser.parse(reader);

            // If existing JSON content exists, parse it into a JSONObject
            if (obj instanceof JSONArray) {
                existingData = (JSONArray )obj;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return existingData;
    }
}
