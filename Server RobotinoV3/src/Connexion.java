import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe qui attends les message d'une connection. C'est le Socket, le in et le out.
 * Ecoute ce qui se passe sur la socket et permet de récuperer les messages.
 * Elle passe les informations server<->socket avec une couche de gestion entre les deux
 * @author prospere
 *
 */
public class Connexion implements Runnable{
	private String name;
	private String ipClient;
	private boolean isStarting = false;
	private boolean isRunning = true;
	private boolean isStoping = false;

	private String type;
	private ServerRobotino serverRobotino;
	private Socket socketClient;
	private PrintWriter out;
	private BufferedReader in;
	
	public Connexion(ServerRobotino s,Socket soClient) {
	
		serverRobotino=s;
		socketClient=soClient;
		
	}

	@Override
	public void run() {//Thread du server qui récupère les message des Socket d'un client
		String initJSON="";
		try {
			this.out = new PrintWriter(socketClient.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
			this.ipClient = socketClient.getRemoteSocketAddress().toString().substring(1, socketClient.getRemoteSocketAddress().toString().length());
			System.out.println("ipClient: "+ipClient);
			String inLine =in.readLine();
			//System.out.println("test888"+inLine);
			//out.println(part1+part2+part3);

			/*out.println(part1);
			out.println(part2);
			out.println(part3);*/
			System.out.println("New connexion; "+inLine);
			if(inLine.startsWith("{")){//classique,on passe à la suite
				initJSON = inLine;
				System.out.println("initJSON: "+initJSON);
				JSONObject JSON = new JSONObject(initJSON);
				this.type = JSON.getString("type");
				System.out.println("Type:"+type);//type = init
				String info1 = JSON.getString("infoInit");
				System.out.println("info1: "+info1);
				//JSONObject JSON = new JSONObject(startJSON);
				this.name = JSON.getString("clientName");
				this.type = JSON.getString("clientType");
				this.out.println("{ \"type\":\"init\",\"infoInit\":\"Server->Client Connexion accepté\",\"serverName\":\""+this.serverRobotino.getNom()+"\"}");

				try {
					serverRobotino.semStop.acquire();//le server ne peut plus modifier is ServerRunning
					if(this.serverRobotino.isServerRunning()){
						serverRobotino.addConnexion(this);
						isStarting= true;
					}
					serverRobotino.semStop.release();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			
			}else if(inLine.startsWith("GET /favicon.ico HTTP/1.1")){//test web
				//ne rien faire
				isStoping=true;
				this.socketClient.close();
			}else if(inLine.startsWith("POST / HTTP/1.1")){//||inLine.startsWith("GET ")){//test web
				String part1 =("HTTP/1.1 200 OK\r\nContent-Length: 1500\r\nDate: Mon, 24 Nov 2014 10:21:21 GMT\r\n Content-Type: text/html\r\n\r\n");
				String part2 =("<!DOCTYPE HTML><html><head><title>test11</title><body><h1>Connexion aux server en cour</h1>");//</body></html>");
				String part3 = ("<h1>Connexion aux server en cour2</h1></body></html>");
				type="web";
				System.out.println(inLine);
				boolean decodageTerminé=false;
				boolean stopConnexion=false;
				String Host="";
				String Origin="";
				String Referer="";
				String nomUtilisateur="";
				String mdpUtilisateur="";
				while(!(decodageTerminé||stopConnexion)){
					if(inLine.startsWith("Host: ")){
						System.out.println(inLine);
						Host=inLine;
					}else if(inLine.startsWith("Origin: ")){
						System.out.println(inLine);
						Origin=inLine;
					}else if(inLine.startsWith("Referer: ")){
						System.out.println(inLine);
						if(Origin==""){// on ferme cette connexion car elle n'est pas importante
							stopConnexion=true;
							decodageTerminé=true;
						}
						Referer=inLine;
					}else if(inLine.startsWith("Accept-Encoding: ")){
						if(Origin==""){// on ferme cette connexion car elle n'est pas importante
							stopConnexion=true;
							decodageTerminé=true;
						}
						Referer=inLine;
					}else if(inLine.startsWith("------WebKitFormBoundary")){
						inLine = in.readLine();
						if(inLine.startsWith("Content-Disposition: form-data; name=\"nomUtilisateur\"")){
							inLine = in.readLine();
							inLine = in.readLine();
							System.out.println("nomUtilisateur: "+inLine);
							nomUtilisateur=inLine;
						}else if(inLine.startsWith("Content-Disposition: form-data; name=\"mdpUtilisateur\"")){
							inLine = in.readLine();
							inLine = in.readLine();
							decodageTerminé=true;
							System.out.println("mdpUtilisateur: "+inLine);
							mdpUtilisateur=inLine;
						}
					}
					inLine = in.readLine();
					//System.out.println(inLine);
				}
				if(decodageTerminé){
					//String part1 =("HTTP/1.1 200 OK\r\nCache-Control: no-cache, private\r\nContent-Length: 400\r\nDate: Mon, 24 Nov 2014 10:21:21 GMT\r\n Content-Type: text/html\r\n\r\n");
					/*while(this.serverRobotino.isServerRunning()&&(inLine.equals("commande reçu")||!inLine.startsWith("{"))){
						inLine = in.readLine();//récupération des informations au déput de la connexion connexion
						System.out.println(inLine);
					}*/

					name="web_"+nomUtilisateur;
					out.println(part1+part2+part3);
					try {//ajout dans la liste de connexion
						serverRobotino.semStop.acquire();//le server ne peut plus modifier is ServerRunning
						if(this.serverRobotino.isServerRunning()){
							serverRobotino.addConnexion(this);
							isStarting= true;
						}
						serverRobotino.semStop.release();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					this.ipClient = socketClient.getLocalAddress().toString();
					String part4 = ("<form method=\"post\"enctype=\"multipart/form-data\" action=\"\"><h5>Start Ad-Placer!</h5>");
					String part5 = ("nomUtilisateur: <input type=\"text\" max=10 name=\"nomUtilisateur\" >");
					part5 += ("<br>mdpUtilisateur:<input type=\"text\" max=10 name=\"mdpUtilisateur\" >");
					String part6 = ("<button type=\"submit\" value=\"Start\">test</button></form>");
					
					int i=0;
					out.println(part4+part5+part6);
					while(i<50){
						try{TimeUnit.MILLISECONDS.sleep(10);}catch (InterruptedException e1) {}
						out.println("test"+i+"");

						i++;
					}
				}
			}else{
				while(this.serverRobotino.isServerRunning()&&(inLine.equals("commande reçu")||!inLine.startsWith("{"))){
					inLine = in.readLine();//récupération des informations au déput de la connexion connexion
					System.out.println("message inconu: "+inLine);
				}
				System.out.println("fin message inconu: "+inLine);
			}
			/*while(this.serverRobotino.isServerRunning()&&(inLine.equals("commande reçu")||!inLine.startsWith("{"))){
				inLine = in.readLine();//récupération des informations au déput de la connexion connexion
				System.out.println(inLine);
				//out.println("HTTP/1.1 200 OK\r\nCache-Control: no-cache, private\r\nContent-Length: 107\r\nDate: Mon, 24 Nov 2014 10:21:21 GMT\r\n\r\n");
				//out.println("<html><head><title>test11</title><body><h1>Connexion aux server en cour2</h1></body></html>");
				//out.println("</pre><h1>Connexion aux server en cour2</h1><pre>");

			}*/

			/*System.out.println("\tAdresse de la socket: "+socketClient.getLocalSocketAddress());
			System.out.println("\tnormalement égal à ipClient: "+ipClient);
			System.out.println("\tInit connexion: "+startJSON);*/


			while(this.serverRobotino.isServerRunning()&&(!isStoping)){
				//if(in.ready()){
					isRunning=false;
					inLine = in.readLine();
					isRunning=true;
					if(!inLine.equals("commande reçu")){
						this.out.println("commande reçu");
						System.out.println("C\tgetIntputStreamServer: "+inLine);
						this.serverRobotino.decodeurJson(inLine);
					}
				//}else{
					//TimeUnit.MILLISECONDS.sleep(100);
				//}
				
			}//Fermeture connxion
			//out.println("Connexion closed by server stoped");
			System.out.println("\tConnexion closed by server stoped");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//out.println("Connexion closed by IOException");
			//e.printStackTrace();
			System.out.println("\tConnexion "+this.name+" closed by IOException");

		}catch(java.lang.NullPointerException e){
			//e.printStackTrace();
			System.out.println("\tConnexion "+this.name+" closed by NullPointerException");
		}catch(org.json.JSONException e){
			System.out.println("erreur decodage JSON: "+e);
			System.out.println("JSON: "+initJSON);
		}
		isRunning=false;
		if(!isStoping){
			this.stopConnexion();
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIpClient() {
		return ipClient;
	}

	public void setIpClient(String ipClient) {
		this.ipClient = ipClient;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void stopConnexion(){
		isStoping=true;
		if(isStarting){
			try {
				//serverRobotino.removeSocket(this.socketClient);
				//serverRobotino.removeThread(Thread.currentThread());
				serverRobotino.removeConnexion(this);
				int i= 0;
				//out.println("Connexion closed by server stoped");
				while(this.isRunning && i<500){
					try {
						TimeUnit.MILLISECONDS.sleep(20);
						i++;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				this.out.println("Connexion closed by server stopped");
				if(!socketClient.isClosed()){
					this.socketClient.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.println("Connexion stoppé sans avoir pu commencé");
		}
		/*Thread.currentThread().interrupt();
		try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		

	}
	public PrintWriter getOut() {
		return out;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}

	public boolean isStoping() {
		return isStoping;
	}

	public void setStoping(boolean isStoping) {
		this.isStoping = isStoping;
	}
	
	/*public void decodeurJson(String j) {
		try{
			JSONObject JSON = new JSONObject(j);
			String type = JSON.getString("type");
			System.out.println("Type:"+type);
			if(type.equals("start")){
				String info1 = JSON.getJSONObject("infoStart").getString("info1");
				System.out.println("info1: "+info1);
			}else if(type.equals("message")){
				String texte = JSON.getJSONObject("infoMessage").getString("texte");
				String destinataire = JSON.getJSONObject("infoMessage").getString("destinataire");
				String expediteur = JSON.getJSONObject("infoMessage").getString("expediteur");
				System.out.println("Message de: "+expediteur);
				System.out.println("pour: "+destinataire);
				System.out.println(texte);
				if(destinataire.equals("Server")){
					System.out.println("\t "+expediteur+": "+texte);
					
				}else{
					
				}
				
			}*/
			
			
			
	
			/*JSONArray arr = JSON.getJSONArray("posts");
			for (int i = 0; i < arr.length(); i++)
			{
			    String post_id = arr.getJSONObject(i).getString("post_id");
	
			}*/
		/*}catch(org.json.JSONException e){
			System.out.println("erreur decodage JSON: "+e);
			System.out.println("JSON: "+j);
		}
	}*/
	
}
