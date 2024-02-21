
import java.io.*;

//message class. Message objects contain timestamp and causal dependency info.
public class Message implements Serializable {
	int NodeId;
    int MsgCount ;
    VectorClock vc;
	
    public Message(){}
    public Message(String Msg)
    {
        String []SplitMsg = Msg.split("\\|");
        this.NodeId = Integer.parseInt(SplitMsg[0]);
        this.MsgCount =  Integer.parseInt(SplitMsg[1]);
        this.vc = new VectorClock(NetworkSettings.TotalNode,SplitMsg[2]);
        
    }
    public Message(int MsgCount,VectorClock vc)
    {
        this.NodeId = NetworkSettings.NodeID;
        this.MsgCount = MsgCount;
        this.vc = vc;
    }

    public String ObjtoString()
    {
        String MsgStr =  "" + this.NodeId +"|"+ this.MsgCount +"|"+ this.vc.toString().replaceAll(" ", "")
                                                                         .replaceAll("\\[", "")
                                                                         .replaceAll("]", "");
        return MsgStr;
    }
    public void MsgPrint()
    {
        System.out.println("NodeId["+this.NodeId+"]  MsgNo["+this.MsgCount+"]   VectorClock["+this.vc.toString()+"]");
    }
}
