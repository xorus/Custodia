
public class mainClient {

	public mainClient() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		int port=50007;
		String ipServer="192.168.56.1";//iplocal
		ipServer="193.48.125.70";
		ipServer="193.48.125.219";
		//new Thread(new Client(ipServer,port,"C1")).start();
		//Min + (Math.random() * (Max - Min))
		ClientUtilisateur c1 = new ClientUtilisateur(ipServer,port,"C1_A"+(int)(Math.random() * (100000)));
		new Thread(c1).start();
		/*new Thread(new Client(ipServer,port,"C2_"+(int)(Math.random() * (100000)))).start();
		new Thread(new Client(ipServer,port,"C3_"+(int)(Math.random() * (100000)))).start();
		new Thread(new Client(ipServer,port,"C4_"+(int)(Math.random() * (100000)))).start();*/

	}
}
