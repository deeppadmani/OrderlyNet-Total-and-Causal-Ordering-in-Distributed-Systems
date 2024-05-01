public class Const 
{
    public static int READ_OP = 0;
    public static int WRITE_OP = 1; 
    public static int HEARTBEAT_OP = 2;
    public static int HEARTBEAT_RPY_OP = 3;
    public static int WHO_I_AM_OP = 4;
    public static int OP_MAX = 5;
    
    public static int NULL = 0;
    public static int MAX_CONNECTION_ATTEMPTS = 2;
    public static int ERROR_CODE = -1;
    public static int HEARTBEAT_INTERVAL = 5000;
    public static int CONNECTION_TIMEOUT = 5000;
    public static String HEARTBEAT_STR = "HEARTBEAT";
    public static String SOCKET_ALIVE_STR = "SOCKET_ALIVE";
}
