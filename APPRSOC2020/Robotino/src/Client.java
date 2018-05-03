import java.awt.Color;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;











//import org.json.*;
import org.json.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable{
	public String ipServer;
	public String ip="0.0.0.0";
	public int portServer;
	public String nom;
	protected ConsoleClient consoleClient;
	public String serverName = "Unknown";
	public PrintWriter out;
	public BufferedReader in;
	public Socket clientSocket;
	public boolean isClientRunning=true;
	protected Thread t1;//thread de la console
	public ArrayList<String> nomsConnexions = new ArrayList<String>();//nom des client connect� aux server
	public Semaphore semStop = new Semaphore(1);
	
	public Client(String ipServer, int port,String nom) {
		this.ipServer=ipServer;
		this.portServer=port;
		this.nom=nom;
		try {
			this.ip = InetAddress.getLocalHost ().getHostAddress ();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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
						if(!inLine.equals("commande re�u")){
							this.out.println("commande re�u");
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
			//Fin du client si la connexion n'� pas d�but�
			
		}
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
				//System.out.println(""+this.nom+"\tgetOutputStream: "+clientSocket.getOutputStream()); ?
				//TimeUnit.MILLISECONDS.sleep(500);
				ip=clientSocket.getLocalSocketAddress().toString().substring(1, clientSocket.getLocalSocketAddress().toString().length());
				System.out.println(""+this.nom+"\tAdresse de la socket: "+ip);
				out.println("{\"type\":\"Init\",\"infoInit\":\"Client-->Server  demande de connexion\", \"clientName\": \""+this.nom+"\", \"clientType\":\"autre\",\"mdp\":\"123\"}");
				//System.out.println("TEST6 "+nomsConnexions);
				String initInLine = in.readLine();
				this.decodeurJson(initInLine);
				//System.out.println("TEST7 "+nomsConnexions+"  "+initInLine);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				nbTentative++;
				if(nbTentative>9){
					//e.printStackTrace();
					System.out.println(""+this.nom+"\tConnexion non trouv�, abandon de la conexion");
				}else{
					System.out.println(""+this.nom+"\tServer introuvable, tentative n�"+nbTentative);
				}
			}
		}
		return connexionTrouve;
	}

	public boolean isClientRunning() {
		return isClientRunning;
	}

	public void setClientRunning(boolean isClientRunning) {
		this.isClientRunning = isClientRunning;
	}
	public void envoieMessage(String message,String dName,String dIP) {
		try{
			//client.out.println(inLine.substring(5, inLine.length()));
			String messageJSON = "{ \"type\":\"message\",\"infoMessage\":{\"texte\":\""+message+"\",\"destinataire\":{\"name\":\""+dName+"\",\"IP\":\""+dIP+"\"},\"expediteur\":{\"name\":\""+this.nom+"\",\"IP\":\""+this.ip+"\"}}}";
			this.out.println(messageJSON);
			
		}catch(IndexOutOfBoundsException e){
			System.out.println(e);
		}catch(java.lang.NullPointerException e){
			System.out.println(e);
		}
	}
	
	
	//{ "type":"testType"}
	//{ "type":"start","infoStart":{"info1":"2"}}
	//tj { "type":"message","infoMessage":{"texte":"teste du message","destinataire":{"dName":"Server Robotion v1","dIP":"0.0.0.0"},"expediteur":{"eName":"C1","eIP":"0.0.0.0"}}}
	//{"type":"requeteAllConnexionName","ConnexionsNames": ["All","Server Robotion v1","C1_70218"]}
	public boolean decodeurJson(String j) {//recoint un message en JSON non identifi�
		try{
			JSONObject JSON = new JSONObject(j);
			String type = JSON.getString("type");
			System.out.println(""+this.nom+"\tType:"+type);
			if(type.equals("init")){
				JSONinit(j);
			}else if(type.equals("requeteAllConnexionName")){//le client re�oit les nom des connexion
				JSONrequeteAllConnexionName(j);
				//System.out.println(arrNoms.get(1).toString());
				//nomsConnexions.clear();
				
			}else if(type.equals("message")){
				JSONmessage(j);
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
		return true;
	}
	public void JSONinit(String j) throws JSONException{
		JSONObject JSON = new JSONObject(j);
		String infoInit = JSON.getString("infoInit");
		serverName = JSON.getString("serverName");
		nomsConnexions.add("All");
		nomsConnexions.add(serverName);
		//interfaceClient.SelectionDestinataire.addItem("All");
		//interfaceClient.SelectionDestinataire.addItem(serverName);
		//interfaceClient.SelectionDestinataire.setSelectedItem(serverName);
		System.out.println(""+this.nom+"\tinfoInit: "+infoInit);
	}
	public void JSONrequeteAllConnexionName(String j) throws JSONException{
		JSONObject JSON = new JSONObject(j);
		JSONArray arrNoms = JSON.getJSONArray("ConnexionsNames");
		this.updateListeConnexion(arrNoms);
	}
	public void JSONmessage(String j) throws JSONException{
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
		//if(eName.equals(this.nom) ||dName.equals(this.nom) || ( dName.equals("All"))){// && !eName.equals(this.nom))){
			//System.out.println(""+this.nom+"\t"+eName+" "+eIP+": "+texte);
			//affichage du message dans l'interface
			////this.interfaceClient.TextAreaMessageRecu.append(eName+"-->"+dName+": "+texte+"\n");
			/*System.out.println(interfaceClient.ScrollMessageRecu.getVerticalScrollBar().getValue());
			System.out.println(interfaceClient.ScrollMessageRecu.getVerticalScrollBar().getMaximum());
			System.out.println(interfaceClient.ScrollMessageRecu.getVerticalScrollBar().getHeight());
			//si la scroll bare est au minimun, on la descend
			if(interfaceClient.ScrollMessageRecu.getVerticalScrollBar().getValue()+interfaceClient.ScrollMessageRecu.getVerticalScrollBar().getHeight()==interfaceClient.ScrollMessageRecu.getVerticalScrollBar().getMaximum()){
				interfaceClient.ScrollMessageRecu.getVerticalScrollBar().setValue(interfaceClient.ScrollMessageRecu.getVerticalScrollBar().getMaximum()-interfaceClient.ScrollMessageRecu.getVerticalScrollBar().getHeight());
				System.out.println(" "+interfaceClient.ScrollMessageRecu.getVerticalScrollBar().getValue());
			}*/
		//}
	}
	public void updateListeConnexion(JSONArray arrNoms) throws JSONException{
		String CoName="";
		ArrayList<String> newNomsConnexions = new ArrayList<String>();
		for (int i = 0; i < arrNoms.length(); i++){//on ajoute les nouvelle connexion
			CoName = arrNoms.get(i).toString();
			if(!this.nom.equals(CoName)){//si le nom est diff�rent de celui de cette connexion
				newNomsConnexions.add(CoName);
				if(!nomsConnexions.contains(CoName)){
					nomsConnexions.add(CoName);
				}
			}
		}
		for (int i = 0; i < nomsConnexions.size(); i++){//on enl�ve les ancienne
			CoName = nomsConnexions.get(i);
			if(!newNomsConnexions.contains(CoName)){
				nomsConnexions.remove(CoName);
			}
		}
		
	}
	
	public void stopClient() {
		boolean stop = true;
		try {//on emp�che quelqu'un d'utiliser serverRunning
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
				//interfaceClient.closeInterfaceInMS(4000, "Client stopped");
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