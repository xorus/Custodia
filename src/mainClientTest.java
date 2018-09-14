import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class mainClientTest {

	public mainClientTest() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		PrintWriter out;
		BufferedReader in;
		Socket clientSocket;
		int port=50007;
		String ipServer="192.168.56.1";//iplocal
		ipServer="193.48.125.70";
		//ipServer="193.48.125.219";
		//new Thread(new Client(ipServer,port,"C1")).start();
		//Min + (Math.random() * (Max - Min))
		//ClientUtilisateur c1 = new ClientUtilisateur(ipServer,port,"C1_A"+(int)(Math.random() * (100000)));
		//new Thread(c1).start();
		try {
			clientSocket = new Socket(ipServer, port);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out.println("{\"type\":\"Init\",\"infoInit\":\"Client-->Server  demande de connexion\", \"clientName\": \""+""+"\", \"clientType\":\"Java\"}");
			out.println("{\"type\":\"Message\",\"message\":\"test1\"}");
			out.println("{\"type\":\"Message\",\"message\":\"test2\"}");
			out.println("{\"type\":\"Message\",\"message\":\"test3\"}");
			out.println("{\"type\":\"Message\",\"message\":\"test4\"}");
			out.println("{\"type\":\"Message\",\"message\":\"test5\"}");
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(""+this.nom+"\tgetOutputStream: "+clientSocket.getOutputStream());

	}
}