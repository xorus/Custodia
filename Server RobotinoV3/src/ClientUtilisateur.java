import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;


public class ClientUtilisateur extends Client{

	public InterfaceClient interfaceClient;
	public String CoNameToDelete="";//utilisé pour bloqué le choix de connexion si celle-ci disparaissait
	public ClientUtilisateur(String ipServer, int port, String nom) {
		super(ipServer, port, nom);
		this.interfaceClient = new InterfaceClient(this);
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
				out.println("{\"type\":\"Init\",\"infoInit\":\"Client-->Server  demande de connexion\", \"clientName\": \""+this.nom+"\", \"clientType\":\"autre\",\"mdp\":\"123\"}");
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
			interfaceClient.closeInterfaceInMS(4000, "Server Introuvable");
		}
	}
	public void JSONinit(String j){
		JSONObject JSON = new JSONObject(j);
		String infoInit = JSON.getString("infoInit");
		serverName = JSON.getString("serverName");
		nomsConnexions.add("All");
		nomsConnexions.add(serverName);
		interfaceClient.SelectionDestinataire.addItem("All");
		interfaceClient.SelectionDestinataire.addItem(serverName);
		interfaceClient.SelectionDestinataire.setSelectedItem(serverName);
	}
		

	public void JSONmessage(String j){
		JSONObject JSON = new JSONObject(j);
		String texte = JSON.getJSONObject("infoMessage").getString("texte");
		String dName = JSON.getJSONObject("infoMessage").getJSONObject("destinataire").getString("name");
		String dIP = JSON.getJSONObject("infoMessage").getJSONObject("destinataire").getString("IP");
		String eName = JSON.getJSONObject("infoMessage").getJSONObject("expediteur").getString("name");
		String eIP = JSON.getJSONObject("infoMessage").getJSONObject("expediteur").getString("IP");
		//String expediteur = JSON.getJSONObject("infoMessage").getString("expediteur");
		System.out.println(""+this.nom+"\tMessage de: "+eName+" "+eIP);
		System.out.println(""+this.nom+"\tpour: "+dName+" "+dIP);
		System.out.println(""+this.nom+"\t"+texte);
		System.out.println(""+this.nom+"\t");
		if(eName.equals(this.nom) ||dName.equals(this.nom) || ( dName.equals("All"))){// && !eName.equals(this.nom))){
			System.out.println(""+this.nom+"\t"+eName+" "+eIP+": "+texte);
			//affichage du message dans l'interface
			this.interfaceClient.TextAreaMessageRecu.append(eName+"-->"+dName+": "+texte+"\n");
			/*System.out.println(interfaceClient.ScrollMessageRecu.getVerticalScrollBar().getValue());
			System.out.println(interfaceClient.ScrollMessageRecu.getVerticalScrollBar().getMaximum());
			System.out.println(interfaceClient.ScrollMessageRecu.getVerticalScrollBar().getHeight());

			//si la scroll bare est au minimun, on la descend
			if(interfaceClient.ScrollMessageRecu.getVerticalScrollBar().getValue()+interfaceClient.ScrollMessageRecu.getVerticalScrollBar().getHeight()==interfaceClient.ScrollMessageRecu.getVerticalScrollBar().getMaximum()){
				interfaceClient.ScrollMessageRecu.getVerticalScrollBar().setValue(interfaceClient.ScrollMessageRecu.getVerticalScrollBar().getMaximum()-interfaceClient.ScrollMessageRecu.getVerticalScrollBar().getHeight());
				System.out.println(" "+interfaceClient.ScrollMessageRecu.getVerticalScrollBar().getValue());

			}*/
		}
	}

	public void updateListeConnexion(JSONArray arrNoms){
		String CoName="";
		ArrayList<String> newNomsConnexions = new ArrayList<String>();
		for (int i = 0; i < arrNoms.length(); i++){//on ajoute les nouvelle connexion
			CoName = arrNoms.get(i).toString();
			if(!this.nom.equals(CoName)){//si le nom est différent de celui de cette connexion
				newNomsConnexions.add(CoName);
				if(!nomsConnexions.contains(CoName)){
					//System.out.println("TEST1 "+nomsConnexions);
					//System.out.println("TEST2 "+CoName);
					nomsConnexions.add(CoName);
					interfaceClient.SelectionDestinataire.addItem(CoName);
				}
			}
		}
		for (int i = 0; i < nomsConnexions.size(); i++){//on enlève les ancienne
			CoName = nomsConnexions.get(i);
			if(!newNomsConnexions.contains(CoName)){
				nomsConnexions.remove(CoName);
				if(!interfaceClient.SelectionDestinataire.getSelectedItem().equals(CoName)){
					interfaceClient.SelectionDestinataire.removeItem(CoName);
				}else{//si la connexion enlevé est celle sélectionner, on l'affiche temporairement en rouge
					//interfaceClient.SelectionDestinataire.setBackground(Color.RED);
					interfaceClient.SelectionDestinataire.setForeground(Color.RED);
					interfaceClient.SelectionDestinataire.setEnabled(false);
					interfaceClient.boutonEnvoyerMessage.setForeground(Color.RED);
					interfaceClient.boutonEnvoyerMessage.setEnabled(false);
					CoNameToDelete=CoName.toString();
					Runnable run = new Runnable() {//on bloque l'envoie de message pendant 1sec
						public void run() {
					        try {TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e) {System.out.println(" interrupted");}
							interfaceClient.SelectionDestinataire.setSelectedItem(serverName);
							interfaceClient.SelectionDestinataire.removeItem(CoNameToDelete);
							interfaceClient.SelectionDestinataire.setEnabled(true);
							interfaceClient.boutonEnvoyerMessage.setEnabled(true);
					        try {TimeUnit.MILLISECONDS.sleep(2000);} catch (InterruptedException e) {System.out.println(" interrupted");}
							//interfaceClient.SelectionDestinataire.setBackground(null);
							interfaceClient.SelectionDestinataire.setForeground(null);
							interfaceClient.boutonEnvoyerMessage.setForeground(null);
					        
					    }
					 };
					 new Thread(run).start();
				}
			}
		}
		
	}
	
	public void stopClient() {
		boolean stop = true;
		try {//on empèche quelqu'un d'utiliser serverRunning
			semStop.acquire();
			if(this.isClientRunning()){
				setClientRunning(false);
				stop=true;
			}
			semStop.release();
			if(stop){
				this.out.println("{ \"type\":\"stop\",\"infoStop\":\"Stop by client\"}");
				try {
					this.clientSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				interfaceClient.closeInterfaceInMS(4000, "Client stopped");
				if(!consoleClient.equals(null)?(consoleClient.isConsoleClientRunning):false){//si la console est ouverte et est actif
					t1.interrupt();
				}
			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
