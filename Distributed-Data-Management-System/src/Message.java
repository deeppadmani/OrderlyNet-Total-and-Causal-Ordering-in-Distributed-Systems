
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Class representing a message that can be sent over the network
public class Message implements Serializable {
	int MsgId;         // ID of the node that created the message
    int RW;
    int src;      // Message count or identifier
    int dest;     // Vector clock associated with the message
    int objNo;
    Boolean []ReplicaInfo;
    String Msg; 
	
    // Default constructor (required for serialization)
    public Message(){}

    // Constructor to initialize message from a string representation
    public Message(String Msg)
    {
        // Split the message string using "|" as delimiter
        String []SplitMsg = Msg.split("\\|");

        // Parse the node ID and message count from the split parts
        this.MsgId = Integer.parseInt(SplitMsg[0]);
        this.RW =  Integer.parseInt(SplitMsg[1]);
        this.src =  Integer.parseInt(SplitMsg[2]);
        this.dest =  Integer.parseInt(SplitMsg[3]);
        this.objNo = Integer.parseInt(SplitMsg[4]);
        this.ReplicaInfo = new Boolean[NetworkSettings.TotalServerNode];
        String []ReplicaInfo = SplitMsg[5].split(",");
        for(int i=0;i<NetworkSettings.TotalServerNode;i++)
        {
            this.ReplicaInfo[i] = "1".equals(ReplicaInfo[i]);
        }
        this.Msg = SplitMsg[6];
        
    }

    public Message(int op, int dest,int objNo,String msg)
    {

        // Parse the node ID and message count from the split parts
        this.MsgId = 0;
        this.RW =  op;
        this.src =  NetworkSettings.NodeID;
        this.dest =  dest;
        this.objNo = objNo;
        this.ReplicaInfo = new Boolean[NetworkSettings.TotalServerNode];
        Arrays.fill(this.ReplicaInfo, false);
        int i = 0;
        while(i<3){
            this.ReplicaInfo[NetworkSettings.getHashCode(dest+(i*2))] = true;
            i++;
        }
        this.Msg = msg;
    }


    // Method to convert message object to string representation
    public String ObjtoString()
    {
        StringBuilder stringReplicaInfo = new StringBuilder();
        for (boolean value : this.ReplicaInfo) {
            stringReplicaInfo.append(value ? "1" : "0").append(",");
        }

        // Convert the message fields to a string with "|" as delimiter
        String MsgStr =  "" + this.MsgId +"|"+ this.RW +"|"+ this.src +"|"+ this.dest +"|" + this.objNo + "|" + stringReplicaInfo + "|" + this.Msg;
        return MsgStr;      // Return the string representation of the message
    }

    // Method to print the message details
    public void MsgPrint()
    {
        // Print node ID, message count, and vector clock
        System.out.println("MsgId["+this.MsgId+"]  RW["+this.RW+"]   src["+this.src+"]--> dest[" + this.dest + "] "+ "Msg[" + this.Msg + "]");
    }

    public void printRXMessages()
    {
        String []SplitMsg = this.Msg.split("_");
        System.out.println("Messages From Server No." + this.src + "   Object:" + this.objNo);
        for(String m: SplitMsg){
            System.out.println(m);
        }
    }

    public List<Integer> getServerNodeFromReplicaInfo() {
        List<Integer> trueIndices = new ArrayList<>();
        for (int i = 0; i < this.ReplicaInfo.length; i++) {
            if (this.ReplicaInfo[i]) {
                trueIndices.add(i);
            }
        }
        return trueIndices;
    }
}
