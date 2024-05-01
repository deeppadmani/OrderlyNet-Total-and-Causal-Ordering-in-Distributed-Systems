public class MainApplication 
{
	
    public static void main(String[] args){
		NetworkSettings Network = new NetworkSettings(Integer.parseInt(args[1]));
		
		try {
			if(args[0].equalsIgnoreCase("Server")){
				Network.initProcess();
				Network.StartNetwork();	//Start Network 
			}
			else if(args[0].equalsIgnoreCase("Client")){
				Network.StartClient();
			}
			else
			{
				System.out.println("Wrong Call:[e.g.: java MainApplication \"Server/Client\" [NodeId] ]");
			}

		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
};
