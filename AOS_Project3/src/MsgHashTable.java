import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;

public class MsgHashTable 
{
    Hashtable <Integer,LinkedList<String>> MsgTable= new Hashtable<>();

    public MsgHashTable() 
    {
        this.MsgTable = new Hashtable<>();
    }

    void addMsg(int key,String msg)
    {
        if(this.MsgTable.containsKey(key))
        {
            LinkedList<String> msgList=MsgTable.get(key);
            msgList.add(msg) ;
            MsgTable.put(key, msgList);
        }
        else{
            MsgTable.put(key, new LinkedList<>(Arrays.asList(msg)));
        }
    }

    String readObj(int key)
    {
        String allmsg="";
        LinkedList<String> msgList=MsgTable.get(key);
        for (String element : msgList) {
            allmsg = allmsg + element + "_";
        }
        System.out.println("OBJ: "+ allmsg);
        return allmsg;
    }
}
