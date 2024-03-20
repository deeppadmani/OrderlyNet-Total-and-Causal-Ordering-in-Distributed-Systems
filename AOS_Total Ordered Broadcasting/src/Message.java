
import java.io.*;

// Class representing a message that can be sent over the network
public class Message implements Serializable {
	int NodeId;         // ID of the node that created the message
    int MsgSeq;      // Message count or identifier
    VectorClock vc;     // Vector clock associated with the message
	
    // Default constructor (required for serialization)
    public Message(){}

    // Constructor to initialize message from a string representation
    public Message(String Msg)
    {
        // Split the message string using "|" as delimiter
        String []SplitMsg = Msg.split("\\|");

        // Parse the node ID and message count from the split parts
        this.NodeId = Integer.parseInt(SplitMsg[0]);
        this.MsgSeq =  Integer.parseInt(SplitMsg[1]);

        // Create a vector clock from the third part of the split message
        this.vc = new VectorClock(NetworkSettings.TotalNode,SplitMsg[2]);
        
    }

    // Constructor to initialize message with message count and vector clock
    public Message(int MsgCount,VectorClock vc)
    {
        // Set the node ID to the current node's ID
        this.NodeId = NetworkSettings.NodeID;
        this.MsgSeq = MsgCount;   // Set the message count
        this.vc = vc;       // Set the vector clock
    }

    // Method to convert message object to string representation
    public String ObjtoString()
    {
        // Convert the message fields to a string with "|" as delimiter
        String MsgStr =  "" + this.NodeId +"|"+ this.MsgSeq +"|"+ this.vc.toString().replaceAll(" ", "")
                                                                         .replaceAll("\\[", "")
                                                                         .replaceAll("]", "");
        return MsgStr;      // Return the string representation of the message
    }

    // Method to print the message details
    public void MsgPrint()
    {
        // Print node ID, message count, and vector clock
        System.out.println("NodeId["+this.NodeId+"]  MsgSeq["+this.MsgSeq+"]   VectorClock["+this.vc.toString()+"]");
    }
}
