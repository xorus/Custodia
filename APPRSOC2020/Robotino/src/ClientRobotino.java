import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;


public class ClientRobotino extends Client{

	public ClientRobotino(String ipServer, int port, String nom) {
		super(ipServer, port, nom);
		// TODO Auto-generated constructor stub
	}
	public boolean connexionAuServer(){
		boolean connexionTrouve=false;
		int nbTentative=0;
		while(!connexionTrouve && nbTentative<10){
			try {
				clientSocket = new Socket(this.ipServer, this.portServer);
				connexionTrouve=true;
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				ip=clientSocket.getLocalSocketAddress().toString().substring(1, clientSocket.getLocalSocketAddress().toString().length());
				System.out.println(""+this.nom+"\tAdresse de la socket: "+ip);
				out.println("{\"type\":\"Init\",\"infoInit\":\"Robotino-->Server  demande de connexion\", \"clientName\": \""+this.nom+"\", \"clientType\":\"robotion\",\"mdp\":\"123\"}");
				String initInLine = in.readLine();
				this.decodeurJson(initInLine);
			} catch (IOException e) {
				nbTentative++;
				if(nbTentative>9){
					System.out.println(""+this.nom+"\tConnexion non trouvé, abandon de la conexion");
				}else{
					System.out.println(""+this.nom+"\tServer introuvable, tentative n°"+nbTentative);
				}
			}
		}
		return connexionTrouve;
	}
	
	public void run() {
		boolean connexionTrouve=connexionAuServer();
		if(connexionTrouve){
			t1 = new Thread(consoleClient = new ConsoleClient(this));
			t1.start();
			this.out.println("{\"type\":\"requeteAllConnexionName\",\"expediteur\":{\"name\":\""+this.nom+"\",\"IP\":\""+this.ip+"\"}}");
			try {
				while(isClientRunning()){
					//if(in.ready()){
						String inLine;
						inLine = in.readLine();
						if(!inLine.equals("commande reçu")){
							this.out.println("commande reçu");
							System.out.println(""+this.nom+"\tgetIntputStreamServer: "+inLine);
							if(inLine.equals("Connexion closed by server stopped")){
								
								this.stopClient();
							}else{
								this.decodeurJson(inLine);
							}
						}
					//}else{
						//TimeUnit.MILLISECONDS.sleep(100);
					//}
					
				}//Fermeture connxion
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			//out.println("Connexion closed by server stoped");
			System.out.println(""+this.nom+"\tConnexion closed by server stoped");

			//if(isClientRunning){
			this.stopClient();
			//}
		}else{
			//Fin du client si la connexion n'à pas débuté
			//interfaceClient.closeInterfaceInMS(4000, "Server Introuvable");
		}
	}
	public boolean decodeurJson(String j) {//recoint un message en JSON non identifié
		if(!super.decodeurJson(j)){//si la classe client n'a pas trouvé à quoi servait le JSON
			try{
				JSONObject JSON = new JSONObject(j);
				String type = JSON.getString("type");
				System.out.println(""+this.nom+"\tType:"+type);
				if(type.equals("commandeRobotino")){
					String commande = JSON.getString("commande");
					System.out.println("commandeRobotino:"+commande);
					if(commande.equals("avancer")){
						String posX = JSON.getJSONObject("infoCommande").getString("posX");
						String posY = JSON.getJSONObject("infoCommande").getString("posY");
						System.out.println("CommandeRobotino:"+commande+" posX:"+posX+" posY:"+posY);
						//A completer!!!
					}
				}else{
					return false;
				}
				
				
				
		
				/*JSONArray arr = JSON.getJSONArray("posts");
				for (int i = 0; i < arr.length(); i++)
				{
				    String post_id = arr.getJSONObject(i).getString("post_id");
		
				}*/
			}catch(org.json.JSONException e){
				System.out.println(""+this.nom+"\terreur decodage JSON: "+e);
				System.out.println(""+this.nom+"\tJSON: "+j);
			}
		}
		return true;
	}
}