import java.util.LinkedList;

public class SequencerClass 
{
    LinkedList<Message> SeqMsgbuffer;
    int MsgSeq;

    public SequencerClass()
    {
        SeqMsgbuffer  = new LinkedList<Message>();
        MsgSeq = 0;
    }
    public synchronized Message getLast()
    {
        return this.SeqMsgbuffer.getLast();
    }

    public synchronized void removeLast()
    {
        this.SeqMsgbuffer.removeLast();
    }

    public synchronized void addFirst(Message msg)
    {
        this.SeqMsgbuffer.addFirst(msg);
    }

    public synchronized boolean isEmpty()
    {
        return this.SeqMsgbuffer.isEmpty();
    }

    public synchronized void SeqIncrement()
    {
        this.MsgSeq++;
    }

    public synchronized int getSeq()
    {
        return this.MsgSeq;
    }

    public synchronized void print()
    {
        System.out.println("---------------------");
        for(Message m:SeqMsgbuffer)
        {
            System.out.println(m.ObjtoString());
        }
        
    }
}
