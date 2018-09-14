import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;


public class ConnexionRobotino implements Runnable {
	private ServerRobotino serverRobotino;
	private Socket socketClient;
	private PrintWriter out;
	private BufferedReader in;
	public ConnexionRobotino(ServerRobotino serverRobotino, Socket socketClient, String firstLine) {
		try {
			this.out = new PrintWriter(socketClient.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
			out.println("{\"type\":\"Init\",\"infoInit\":\"Connection accepté\"}");
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.serverRobotino=serverRobotino;
		this.socketClient=socketClient;
		System.out.println("C\tgetIntputStreamServer: "+firstLine);
	}

	@Override
	public void run() {
		serverRobotino.addConnexionRobotino(this);
		try {
			String inLine = "";
			while(this.serverRobotino.isServerRunning()&&inLine!=null){//lecture des nouveau message
				inLine = in.readLine();
				System.out.println("C\tgetIntputStreamServer2: "+inLine);
				//this.serverRobotino.decodeurJson(inLine);
			}
		} catch (IOException e) {/*e.printStackTrace();*/}//connexion fermé
		System.out.println("C\ttest fin de conection par rupture de connexion: ");
		serverRobotino.removeConnexionRobotino(this);
	}

}
