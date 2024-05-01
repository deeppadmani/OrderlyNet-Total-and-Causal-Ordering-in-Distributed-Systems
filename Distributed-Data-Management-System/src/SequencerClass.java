import java.util.LinkedList;

public class SequencerClass 
{
    LinkedList<Message> Msgbuffer;
    int MsgSeq;

    public SequencerClass()
    {
        Msgbuffer  = new LinkedList<Message>();
        MsgSeq = 0;
    }
    public synchronized Message getLast()
    {
        return this.Msgbuffer.getLast();
    }

    public synchronized void removeLast()
    {
        this.Msgbuffer.removeLast();
    }

    public synchronized void addFirst(Message msg)
    {
        this.Msgbuffer.addFirst(msg);
    }

    public synchronized boolean isEmpty()
    {
        return this.Msgbuffer.isEmpty();
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
        for(Message m:Msgbuffer)
        {
            System.out.println(m.ObjtoString());
        }
        System.out.println("---------------------");
        
    }
}
