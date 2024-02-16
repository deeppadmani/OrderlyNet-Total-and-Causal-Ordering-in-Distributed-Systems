

import java.io.*; 
import java.net.*; 
import java.util.Scanner; 


public class Client 
{ 
	public static void main(String[] args) {
		Socket socket = null;
		System.out.println("Your Name: ");
		Scanner scan = new Scanner(System.in);
		String name = scan.nextLine();
		System.out.println("NAME: --> "+ name);
		scan.close();

		int port = 5555;
		try{
            socket = new Socket("localhost",port);

			Thread server = new Thread(new ServerThread(socket));
			server.start();

        }catch(IOException e){
            System.out.println("ERROR: "+ port);
            e.printStackTrace();
        }
	}
} 
