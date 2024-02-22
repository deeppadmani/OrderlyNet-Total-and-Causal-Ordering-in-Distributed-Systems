
import java.util.Arrays;


// Class to represent a vector clock
public class VectorClock {
    private int[] clock;            // Array to hold the clock values for each process

    // Constructor to initialize the vector clock with a given size
    public VectorClock(int size) {
        this.clock = new int[size];     // Initialize the clock array with the given size
    }

    // Constructor to initialize the vector clock from a string representation
    public VectorClock(int size,String Msg) {
        this.clock = new int[size];         // Initialize the clock array with the given size

        // Split the message string and parse clock values from it
        String []AllClock = Msg.split(",");
        
        for(int Idx = 0;Idx < size;Idx++)
        {
            this.clock[Idx] =  Integer.parseInt(String.valueOf(AllClock[Idx]));
        }
    }

    // Method to increment the clock value for a given process
    public synchronized void tick(int processId) {
        this.clock[processId]++;        // Increment the clock value for the specified process
    }

    public synchronized void update(VectorClock otherClock) {
        for (int i = 0; i < clock.length; i++) {
            this.clock[i] = Math.max(this.clock[i], otherClock.clock[i]);
        }
    }

    // Method to convert the vector clock to a string representation
    @Override
    public String toString() {
        return Arrays.toString(this.clock);         // Return the string representation of the clock array
    }

    // Method to calculate the total value of the clock
    public int getClockValue(VectorClock others) {
        int totalClkValue = 0;
        for (int i = 0; i < others.clock.length; i++) {
            totalClkValue += others.clock[i] ;
        }
        return totalClkValue;
    }

    // Method to check if a message is buffered based on vector clock comparison
    public synchronized boolean IsMsgbuffered(Message others)
    {
        boolean Result = false;
        for(int Idx = 0;Idx < NetworkSettings.TotalNode;Idx++)
        {
            if(Idx != others.NodeId)
            {
                if(others.vc.clock[Idx] > NetworkSettings.LocalVectorClock.clock[Idx])
                {
                    Result = true;
                }
            }
        }

        return Result;
    }
}
