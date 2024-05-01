public class MainApplication 
{
	
    public static void main(String[] args){
		NetworkSettings Network = new NetworkSettings(Integer.parseInt(args[0]));
		
		try {
			Network.StartNetwork();	//Start Network 
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
};
