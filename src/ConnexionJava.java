import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ConnexionJava implements Runnable {
	private ServerRobotino serverRobotino;
	private Socket socketClient;
	private PrintWriter out;
	private BufferedReader in;
	public ConnexionJava(ServerRobotino serverRobotino, Socket socketClient, String firstLine) {
		try {
			this.out = new PrintWriter(socketClient.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("C\tgetIntputStreamServer: "+firstLine);
	}

	@Override
	public void run() {
		try {
			String inLine;
			while(this.serverRobotino.isServerRunning()){//lecture des nouveau message
				inLine = in.readLine();
				if(!inLine.equals("commande reçu")){
					//this.out.println("commande reçu");
					System.out.println("C\tgetIntputStreamServer: "+inLine);
					//this.serverRobotino.decodeurJson(inLine);
				}
			}
		} catch (IOException e) {e.printStackTrace();}
		System.out.println("C\ttest fin de conection par rupture de conexion: ");
	}

}
