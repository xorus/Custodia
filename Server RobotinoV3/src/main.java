import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;


public class main {
	public static class CreateTestClient extends Thread {
		public int port;
		public CreateTestClient(String name, int port){
			super(name);
			this.port=port;
		}
		public void run(){
			try {
				TimeUnit.MILLISECONDS.sleep(500);//(long) delay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Test connexion aux serveur...");
			try {
				Socket testSocket = new Socket("192.168.56.1", this.port);
				System.out.println("getOutputStream: "+testSocket.getOutputStream());
				PrintWriter out = new PrintWriter(testSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(testSocket.getInputStream()));
				TimeUnit.MILLISECONDS.sleep(500);
				out.println("{\"name\": \""+this.getName()+"\", \"type\":\"Autre\", \"is_vip\":true, \"other\":\"foo\"}");
				//System.out.println("getIntputStream: "+in.readLine());
				//System.out.println("getIntputStream: "+in.readLine());
				TimeUnit.MILLISECONDS.sleep(500);
				out.println("message1");
				TimeUnit.MILLISECONDS.sleep(500);
				out.println("message2");
				//System.out.println("getIntputStream: "+in.readLine());// récupère les message du server

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	/*public static class WaitConnexionClient extends Thread {
		ServerRobotino s;
		public WaitConnexionClient(String name,ServerRobotino s){
			super(name);
			this.s = s;
		}
		public void run(){
			s.waitNewConnexion();
			
		}
	}*/
	public static void main(String[] args) {
		int port=50007;
		String ipLocal="192.168.56.1";
		//new Thread(new Client(ipServer,port,"C1")).start();
		//Min + (Math.random() * (Max - Min))
		/*Client c1 = new Client(ipLocal,port,"C1_"+(int)(Math.random() * (100000)));
		new Thread(c1).start();
		Client c2 = new Client(ipLocal,port,"C1_"+(int)(Math.random() * (100000)));
		new Thread(c2).start();
		Client c3 = new Client(ipLocal,port,"C1_"+(int)(Math.random() * (100000)));
		new Thread(c3).start();
		Client c4 = new Client(ipLocal,port,"C1_"+(int)(Math.random() * (100000)));
		new Thread(c4).start();*/
		ServerRobotino server = new ServerRobotino(port);
		
		/*WaitConnexionClient w1 = new WaitConnexionClient("W1",server);
		w1.start();*/
		//server.waitNewConnexion();
		
		
		
		
		
		
		
		/*try {
			TimeUnit.MILLISECONDS.sleep(10000);//(long) delay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server.stopServer();*/
	}
}

