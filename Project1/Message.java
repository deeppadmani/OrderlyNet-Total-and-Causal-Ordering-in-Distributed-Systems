import java.util.Date;


public class Message 
{
    int[][] sent = new int[4][4];
	String Msg;
	Date TimeStamp;
	int Src;
	int Dest;

	public Message(int Src, int Dest) {
		this.Msg = "Test From Application!";
		this.Src = Src;
		this.Dest = Dest;
	}
	public Message(Message msg, int Src, int Dest){
		this.sent = msg.sent;
		this.Msg = msg.Msg;
		this.TimeStamp = msg.TimeStamp;
		this.Src = Src;
		this.Dest = Dest;
	}
}
