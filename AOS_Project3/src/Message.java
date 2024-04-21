
import java.io.*;

// Class representing a message that can be sent over the network
public class Message implements Serializable {
	int MsgId;         // ID of the node that created the message
    int RW;
    int src;      // Message count or identifier
    int dest;     // Vector clock associated with the message
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
        this.Msg = SplitMsg[4];
        
    }

    public Message(int op, int dest,String msg)
    {

        // Parse the node ID and message count from the split parts
        this.MsgId = 0;
        this.RW =  op;
        this.src =  NetworkSettings.NodeID;
        this.dest =  dest;
        this.Msg = msg;
        
    }

    // // Constructor to initialize message with message count and vector clock
    // public Message(int MsgCount,int MsgNo)
    // {
    //     // Set the node ID to the current node's ID
    //     this.MsgId = NetworkSettings.NodeID;
    //     this.MsgSeq = MsgCount;   // Set the message count
    //     this.MsgNo = MsgNo;       // Set the vector clock
    // }

    // Method to convert message object to string representation
    public String ObjtoString()
    {
        // Convert the message fields to a string with "|" as delimiter
        String MsgStr =  "" + this.MsgId +"|"+ this.RW +"|"+ this.src +"|"+ this.dest +"|"+ this.Msg;
        return MsgStr;      // Return the string representation of the message
    }

    // Method to print the message details
    public void MsgPrint()
    {
        // Print node ID, message count, and vector clock
        System.out.println("MsgId["+this.MsgId+"]  RW["+this.RW+"]   src["+this.src+"]--> dest[" + this.dest + "] "+ "Msg[" + this.Msg + "]");
    }
}
