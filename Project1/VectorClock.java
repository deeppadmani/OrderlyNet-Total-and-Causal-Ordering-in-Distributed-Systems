
import java.util.Arrays;

public class VectorClock {
    private int[] clock;

    public VectorClock(int size) {
        this.clock = new int[size];
    }
    public VectorClock(int size,String Msg) {
        this.clock = new int[size];
        String []AllClock = Msg.split(",");
        for(int Idx = 0;Idx < size;Idx++)
            this.clock[Idx] =  Integer.parseInt(String.valueOf(AllClock[Idx]));
    }
    public synchronized void tick(int processId) {
        this.clock[processId]++;
    }

    public void update(VectorClock other) {
        for (int i = 0; i < this.clock.length; i++) {
            this.clock[i] = Math.max(this.clock[i], other.clock[i]);
        }
    }

    public int[] getClock() {
        return this.clock;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.clock);
    }

    
    public int getClockValue(VectorClock others) {
        int totalClkValue = 0;
        for (int i = 0; i < others.clock.length; i++) {
            totalClkValue += others.clock[i] ;
        }
        return totalClkValue;
    }

    public boolean IsMsgbuffered(VectorClock others)
    {
        if(getClockValue(NetworkSettings.LocalVectorClock) < getClockValue(others))
            if((getClockValue(others) - getClockValue(NetworkSettings.LocalVectorClock)) > 1)
                return true;
        return false;
    }

}
