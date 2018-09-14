import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Classe réunissant les différentes fonctions permettant la gestion du serveur
 * @author prospere
 *
 */
public class ServerRobotino {
	private String ip;  
	private String nom;
	//private BaseDeDonnee bd;
	private int portServeur;//50003 si ne marche pas
	public InterfaceServer interfaceServer;
	//public static Thread t;
	//private static InterfaceCommunication instance = null;
	private ServerSocket socketServer = null;
	static private final String IPV4_REGEX = "(([0-1]?[0-9]{1,2}\\.)|(2[0-4][0-9]\\.)|(25[0-5]\\.)){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))";
	//private ArrayList<Socket> socketCilents = new ArrayList<Socket>();
	//private ArrayList<Thread> inputThreads = new ArrayList<Thread>();
	private ArrayList<Connexion> connexions = new ArrayList<Connexion>();
	private ArrayList<Connexion> connexionsRobotino = new ArrayList<Connexion>();
	public String CoNameToDelete="";//utilisé pour bloqué le choix de connexion si celle-ci disparaissait


	
	private Thread t1;
	private boolean serverRunning = true;
	private boolean serverFullyStopped = false;
	public Semaphore semStop = new Semaphore(1);
	//static private Pattern PATTERN = Pattern.compile(IPV4_REGEX);
	//private static final Logger LOGGER = Logger.getLogger( InterfaceCommunication.class.getName() );
	//private static FileHandler fileTxt;
	
	public ServerRobotino(int port) {
		try {
			this.setPortServeur(port);
			ip = InetAddress.getLocalHost ().getHostAddress ();
			nom = "Server Robotion v1";
			socketServer = new ServerSocket(getPortServeur());
			System.out.println("IP: "+this.ip+":"+this.getPortServeur());
			t1 = new Thread(new ConsoleServer(this));
			t1.start();
			this.interfaceServer = new InterfaceServer(this);
			interfaceServer.SelectionDestinataire.addItem("All");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.waitNewConnexion(); //On commence a couter
		this.stopServer(); // Une fois fini d'ecouter, on s'arrête
        try {TimeUnit.MILLISECONDS.sleep(100);} catch (InterruptedException e) {System.out.println(" interrupted");}
	}
	public void waitNewConnexion(){
		try {
			System.out.println("Server lancé");

			while(serverRunning){
				//if(socketServer!=null){
					//Socket socketClient;
					Socket socketClient = socketServer.accept();
					//socketCilents.add(socketClient);
					//System.out.println("connexion accepté: "+socketClient);
					if(serverRunning){
		            	new Thread(new Connexion(this,socketClient)).start();
					}else{
						new PrintWriter(socketClient.getOutputStream(), true).println("Connexion canceled cause server is stopping");
						socketClient.close();
					}
		            //inputThreads.add(t);
		            //t.start();
		            //System.out.println(t);
		            //t = new Thread(new MessageEntrant(socket,abonne));

					/*System.out.println("getIntputStreamServer: "+in.readLine());
					TimeUnit.MILLISECONDS.sleep(600);//(long) delay);
					System.out.println("getIntputStreamServer2: "+in.readLine());*/

				/*}else{
					System.out.println("Attente lancement server...: ");
					TimeUnit.MILLISECONDS.sleep(100);//(long) delay);
				}*/
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Arrêt d'écoute de nouvelle connexion");
			//e.printStackTrace();//affiche erreur en cas d'arrêt forcé
		}
	}
	public boolean isNoConnexionWithSameName(String coName) {
		boolean boo = true;
		for (int i = 0; i < this.connexions.size(); i++) {
			if (connexions.get(i).getName().equals(coName)) {
				boo = false;
			}
		}
		if (this.getNom().equals(coName)||"All".equals(coName)) {
			boo = false;
		}
		//System.out.println("boo: "+boo);

		return boo;
	}
	
	public ArrayList<Connexion> getConnexions() {
		return this.connexions;
	}
	public void setConnexions(ArrayList<Connexion> connexions) {
		this.connexions = connexions;
	}

	public synchronized void addConnexion(Connexion connexion) {
		if(isNoConnexionWithSameName(connexion.getName())){
			this.connexions.add(connexion);
			if(connexion.getType().equals("robotino")){
				this.connexionsRobotino.add(connexion);
			}
			String j2="{\"type\":\"requeteAllConnexionName\","+this.returnJSONAllConnexionName()+"}";
			this.envoiRequete(j2,"All","0.0.0.0");//envoi à toute les connexions la liste de nom des connexion
			interfaceServer.SelectionDestinataire.addItem(connexion.getName());
		}else{
			connexion.getOut().println("{ \"type\":\"stop\",\"infoStop\":\"Connexion with same name already exist\"}");
			connexion.stopConnexion();
		}
	}
	public synchronized void removeConnexion(Connexion connexion) {
		this.connexions.remove(connexion);
		if(connexion.getType().equals("robotino")){
			this.connexionsRobotino.remove(connexion);
		}
		String j2="{\"type\":\"requeteAllConnexionName\","+this.returnJSONAllConnexionName()+"}";
		this.envoiRequete(j2,"All","0.0.0.0");//envoi à toute les connexions la liste de nom des connexion
		this.removeInterfaceListConnexion(connexion.getName());
		
	}/*
	public void removeSocket(Socket socket) {
		this.socketCilents.remove(socket);
	}
	public void removeThread(Thread thread) {
		this.inputThreads.remove(thread);
	}*/
	
	public void stopServer(){
		boolean stop = false;
		try {//on empèche quelqu'un d'utiliser serverRunning
			semStop.acquire();
			if(this.serverRunning){
				stop=true;
				this.serverRunning=false;
			}
			semStop.release();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(stop){
			//Sync
			System.out.println("Server en cour d'arrêt...");
			
			for (int i = this.connexions.size()-1; i >= 0; i--) {
				try{
					System.out.println("Stop connexion n°"+i);
					if(!connexions.get(i).isStoping()){
						connexions.get(i).stopConnexion();
					}
				}catch(IndexOutOfBoundsException|java.lang.NullPointerException e){//catch si on essaye de fermer une connexion qui s'est enlevé de la liste
					System.out.println(e);
				}

			}
			while(this.connexions.size()>0){
				System.out.println("\n"+this.connexions.size()+" connexion en cour");
				//System.out.println(this.inputThreads.size()+" thread en cour");
				//System.out.println(this.socketCilents.size()+" socket cleint en cour");
				try {
					TimeUnit.MILLISECONDS.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				socketServer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(this.connexions.size()+" connexion en cour");
			//System.out.println(this.inputThreads.size()+" thread en cour");
			//System.out.println(this.socketCilents.size()+" socket client en cour");
			System.out.println("Server Arrêté");
			interfaceServer.closeInterfaceInMS(2000, "Server stopped");
			serverFullyStopped=true;
		}else{
			System.out.println("Server already stopping");
			while(!serverFullyStopped){
				//System.out.println("Server while");
		        try {TimeUnit.MILLISECONDS.sleep(50);} catch (InterruptedException e) {System.out.println(" interrupted");}
			}
		}
		
	}
	public boolean isServerRunning() {
		return serverRunning;
	}
	public void setServerRunning(boolean serverRunning) {
		this.serverRunning = serverRunning;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public boolean envoiRequete(String j,String dName, String dIP){//envoi de requête JSON à une connexion
		boolean messageEnvoye = false;
		for (int i = this.connexions.size()-1; i >= 0; i--) {
			try{
				System.out.println("test envoi connexion n°"+i+" dName: "+dName);
				if(connexions.get(i).getName().equals(dName)){
					System.out.println("envoi connexion n°"+i);
					messageEnvoye=true;
					if(connexions.get(i).getType().contains("web")){
						connexions.get(i).encodeWebSocketMessage(j);
					}else{
						connexions.get(i).getOut().println(j);
					}
				}else if(dName.equals("All")){
					System.out.println("envoi connexion n°"+i);
					messageEnvoye=true;
					if(connexions.get(i).getType().contains("web")){
						connexions.get(i).encodeWebSocketMessage(j);
					}else{
						connexions.get(i).getOut().println(j);
					}
				}
			}catch(IndexOutOfBoundsException|java.lang.NullPointerException e){//catch si on essaye d'envoyer une requête à un client qui s'est enlevé de la liste
				System.out.println("Connexion "+i+" introuvable, "+e);
			}
		}
		for (int i = this.connexionsRobotino.size()-1; i >= 0; i--) {
			try{
				System.out.println("test envoi connexion robotino n°"+i+" dName: "+dName);
				if(dName.equals("AllRobotino")){
					System.out.println("envoi connexion robotino n°"+i);
					messageEnvoye=true;
					connexionsRobotino.get(i).getOut().println(j);
				}
			}catch(IndexOutOfBoundsException|java.lang.NullPointerException e){//catch si on essaye d'envoyer une requête à un client qui s'est enlevé de la liste
				System.out.println("connexionsRobotino "+i+" introuvable, "+e);
			}
		}
		return messageEnvoye;
	}

	public String returnJSONAllConnexionName() {
		String JSON = "\"ConnexionsNames\": [\"All\",\""+this.getNom()+"\"";
		for (int i = 0; i < this.connexions.size(); i++) {
			JSON+=",\""+this.connexions.get(i).getName()+"\"";
		}
		JSON+="]";
		//System.out.println("JSON: "+JSON);
		return JSON;
	}
	public void decodeurJson(String j) {
		try{
			//returnJSONAllConnexionName();
			JSONObject JSON = new JSONObject(j);
			String type = JSON.getString("type");
			System.out.println("Type:"+type);
			if(type.equals("init")){//inutilisé ici, uniquement au début de la classe connexion
				//String info1 = JSON.getJSONObject("infoStart").getString("info1");
				//System.out.println("info1: "+info1);
			}else if(type.equals("stop")){//client stop connexion
				String infoStop = JSON.getString("infoStop");
				System.out.println("infoStop: "+infoStop);
			}else if(type.equals("requeteAllConnexionName")){//le client demande les nom des connexion
				String j2="{\"type\":\"requeteAllConnexionName\","+this.returnJSONAllConnexionName()+"}";
				String eName = JSON.getJSONObject("expediteur").getString("name");
				this.envoiRequete(j2,eName,"0.0.0.0");
			}else if(type.equals("commandeRobotino")){
				//{"type":"commandeRobotino","commande":"setPositions","data":{"x":242,"y":817,"angle":{"degree":-86.75265720967708,"radian":-1.5141195031628618}},"destinataire":{"name":"Server Robotino v1","ip":"193.48.125.70:50007"},"expediteur":{"name":"C1","ip":"0.0.0.0"}}
				String commande = JSON.getString("commande");
				this.envoiRequete(j, "AllRobotino", "0.0.0.0:0");
				if(commande.equals("setPositions")){//le donne une nouvelle position au robot
					System.out.println("AAA\tsetPositions: "+j);
					System.out.println("commandeRobotino:"+commande);
					String eName = JSON.getJSONObject("expediteur").getString("name");
					//this.envoiRequete("{\"type\":\"log\",\"log\",\"Demande setPosition bien reçu\"}",eName,"0.0.0.0:0");
				}else if(type.equals("avancer")){
					//String dName = JSON.getJSONObject("infoCommande").getJSONObject("destinataire").getString("name");
					//String dIP = JSON.getJSONObject("infoCommande").getJSONObject("destinataire").getString("IP");
					//this.envoiRequete(j, dName, dIP);
					this.envoiRequete(j, "All", "0.0.0.0:0");
				}
			}else if(type.equals("message")){// si on reçoit un message
				String texte = JSON.getJSONObject("infoMessage").getString("texte");
				String dName = JSON.getJSONObject("infoMessage").getJSONObject("destinataire").getString("name");
				String dIP = JSON.getJSONObject("infoMessage").getJSONObject("destinataire").getString("IP");
				String eName = JSON.getJSONObject("infoMessage").getJSONObject("expediteur").getString("name");
				String eIP = JSON.getJSONObject("infoMessage").getJSONObject("expediteur").getString("IP");
				//String expediteur = JSON.getJSONObject("infoMessage").getString("expediteur");
				System.out.println("Message de: "+eName+" "+eIP);
				System.out.println("pour: "+dName+" "+dIP);
				System.out.println(texte);
				System.out.println("");
				if(dName.equals(this.getNom())){//si message pour le server
					System.out.println(""+this.getNom()+"\t"+eName+" "+eIP+": "+texte);
					this.envoiRequete(j,eName,eIP);
					//affichage du message dans l'interface
					this.interfaceServer.TextAreaMessageRecu.append(eName+"-->"+dName+": "+texte+"\n");
				}else if(dName.equals("All")){//si message pour tout le monde
					this.interfaceServer.TextAreaMessageRecu.append(eName+"-->"+dName+": "+texte+"\n");
					//this.envoiRequete(j,eName,eIP);
					this.envoiRequete(j,dName,dIP);
				}else{//si message pour un client
					this.envoiRequete(j,eName,eIP);
					this.envoiRequete(j,dName,dIP);
				}
				/*if(dName.equals(this.nom)){//si message pour le server
					System.out.println(eName+" "+eIP+": "+texte);
					this.envoiRequete(j,eName,eIP);
				}else{
					if(dName.equals("All") && !eName.equals(this.getNom())){//si message pour tout les client et ne vient pas du serveur
						System.out.println(eName+" "+eIP+": "+texte);
						this.envoiRequete(j,"All",dIP);
					}else if(!dName.equals("All") && !eName.equals(this.getNom())){//si message pour un client et ne venant pas du serveur
						//System.out.println(eName+" "+eIP+": "+texte);//message non destiné au server
						this.envoiRequete(j,dName,dIP);
						this.envoiRequete(j,eName,eIP);
					}else{//si message pour un seul client venant du server
						if(!this.envoiRequete(j,dName,dIP)){//aucun destinataire trouve
							//this.envoiRequete(j,"All",dIP);
						}
					}
				}*/
				
			}
			
			/*JSONArray arr = JSON.getJSONArray("posts");
			for (int i = 0; i < arr.length(); i++)
			{
			    String post_id = arr.getJSONObject(i).getString("post_id");
	
			}*/
		}catch(org.json.JSONException e){
			System.out.println("erreur decodage JSON: "+e);
			System.out.println("JSON: "+j);
		}
		
	}

	public void removeInterfaceListConnexion(String CoName){
		if(!interfaceServer.SelectionDestinataire.getSelectedItem().equals(CoName)||!interfaceServer.boutonEnvoyerMessage.isEnabled()){//si la connexion terminé n'est pas celle selectioné ou que le bouton est bloqué est bloqué
			interfaceServer.SelectionDestinataire.removeItem(CoName);
		}else{//si la connexion enlevé est celle sélectionner, on l'affiche temporairement en rouge
			//interfaceClient.SelectionDestinataire.setBackground(Color.RED);
			interfaceServer.SelectionDestinataire.setForeground(Color.RED);
			interfaceServer.SelectionDestinataire.setEnabled(false);
			interfaceServer.boutonEnvoyerMessage.setForeground(Color.RED);
			interfaceServer.boutonEnvoyerMessage.setEnabled(false);
			CoNameToDelete=CoName.toString();
			Runnable run = new Runnable() {//on bloque l'envoie de message pendant 1sec
				public void run() {
			        try {TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e) {System.out.println(" interrupted");}
			        interfaceServer.SelectionDestinataire.setSelectedItem("All");
			        interfaceServer.SelectionDestinataire.removeItem(CoNameToDelete);
			        interfaceServer.SelectionDestinataire.setEnabled(true);
			        interfaceServer.boutonEnvoyerMessage.setEnabled(true);
			        try {TimeUnit.MILLISECONDS.sleep(2000);} catch (InterruptedException e) {System.out.println(" interrupted");}
					//interfaceClient.SelectionDestinataire.setBackground(null);
			        interfaceServer.SelectionDestinataire.setForeground(null);
			        interfaceServer.boutonEnvoyerMessage.setForeground(null);
					CoNameToDelete="";
			        
			    }
			 };
			 new Thread(run).start();
		}
	}

	public void envoieMessage(String message,String dName,String dIP) {
		try{
			//client.out.println(inLine.substring(5, inLine.length()));
			String messageJSON = "{ \"type\":\"message\",\"infoMessage\":{\"texte\":\""+message+"\",\"destinataire\":{\"name\":\""+dName+"\",\"IP\":\""+dIP+"\"},\"expediteur\":{\"name\":\""+this.nom+"\",\"IP\":\""+this.ip+":"+this.getPortServeur()+"\"}}}";
			this.envoiRequete(messageJSON,dName,dIP);
			System.out.println(""+this.getNom()+"\t"+this.getNom()+" "+this.ip+":"+this.getPortServeur()+": "+message);
			//affichage du message dans l'interface
			this.interfaceServer.TextAreaMessageRecu.append(this.getNom()+"-->"+dName+": "+message+"\n");
			
		}catch(IndexOutOfBoundsException|java.lang.NullPointerException e){
			System.out.println(e);
		}
	}
	public int getPortServeur() {
		return portServeur;
	}
	public void setPortServeur(int portServeur) {
		this.portServeur = portServeur;
	}
	

}
