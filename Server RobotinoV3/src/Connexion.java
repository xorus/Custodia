import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.DatatypeConverter;

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
			if(inLine.startsWith("{")){//connexion classique en java,on passe à la suite
				initJSON = inLine;
				System.out.println("initJSON: "+initJSON);
				JSONObject JSON = new JSONObject(initJSON);
				this.type = JSON.getString("clientType");
				System.out.println("clientType:"+type);//type = init
				String info1 = JSON.getString("infoInit");
				System.out.println("info1: "+info1);
				//JSONObject JSON = new JSONObject(startJSON);
				this.name = JSON.getString("clientName");
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
			
			}else if(inLine.startsWith("GET /favicon.ico HTTP/1.1")&&false){//test web
				//ne rien faire
				isStoping=true;
				this.socketClient.close();
			}else if((inLine.startsWith("POST / HTTP/1.1")||inLine.startsWith("GET "))&&false){//test web
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
				/*String part1 =("HTTP/1.1 200 OK\r\nContent-Length: 100\r\nDate: Mon, 24 Nov 2014 10:21:21 GMT\r\n Content-Type: text/html\r\n\r\n");
				String part2 =("<!DOCTYPE HTML><html><head><title>test11</title><body><h1>Connexion aux server en cour</h1>");//</body></html>");
				String part3 = ("<h1>Connexion aux server en cour2</h1></body></html>");
				
				//out.println(part1+part2+part3);*/
				while(this.serverRobotino.isServerRunning()&&(inLine.equals("commande reçu")||!inLine.startsWith("Sec-WebSocket-Key: "))){
					inLine = in.readLine();//récupération des informations au déput de la connexion connexion
					System.out.println("message inconu: "+inLine);
				}
				//String delims = "[: ]";
				String[] parse = inLine.split(": ");
				String key = (parse[1].toString());
				System.out.println("Sec-WebSocket-Key: "+key);
				System.out.println("message: "+inLine);
				try {
					System.out.println("newSWSK: "+DatatypeConverter.printBase64Binary(MessageDigest.getInstance("SHA-1").digest((key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8"))).toString());
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				}
				byte[] response;
				try {
					response = ("HTTP/1.1 101 Switching Protocols\r\n"
					        + "Connection: Upgrade\r\n"
					        + "Upgrade: websocket\r\n"
					        + "Sec-WebSocket-Accept: "
					        + DatatypeConverter
					        .printBase64Binary(
					                MessageDigest
					                .getInstance("SHA-1")
					                .digest((key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11")
					                        .getBytes("UTF-8")))
					        + "\r\n\r\n").getBytes("UTF-8");
				    //System.out.println("reponse: "+response.toString());
				    socketClient.getOutputStream().write(response, 0, response.length);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			            //.getBytes("UTF-8");
				inLine = in.readLine();//récupération des informations au déput de la connexion connexion
				System.out.println("message inconu: "+inLine);inLine = in.readLine();//récupération des informations au déput de la connexion connexion
				System.out.println("message inconu: "+inLine);
			    //out.println(response.toString());
		        String message = decodeWebSocketMessage();
				while(this.serverRobotino.isServerRunning()&&(!message.startsWith("{"))){
					//inLine = in.readLine();//récupération des informations au déput de la connexion connexion
					System.out.println("message non JSON: "+message);

			        message = decodeWebSocketMessage();
					
				}//on a le message d'init
				initJSON = message;
				System.out.println("initJSON: "+initJSON);
				JSONObject JSON = new JSONObject(initJSON);
				this.type = JSON.getString("clientType");
				System.out.println("clientType:"+type);
				String info1 = JSON.getString("infoInit");
				System.out.println("info1: "+info1);
				if(this.type.equals("autre")){
					this.type="web";
				}
				this.name = JSON.getString("clientName");

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
				//    !!!! a réparer
				//this.out.println("{ \"type\":\"init\",\"infoInit\":\"Server->Client Connexion accepté\",\"serverName\":\""+this.serverRobotino.getNom()+"\"}");

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


			while(this.serverRobotino.isServerRunning()&&(!isStoping)){//lecture des nouveau message
				if(this.type.contains("web")){//connexion web
					//this.encodeWebSocketMessage("{\"type\":\"test\"}");
					isRunning=false;
					inLine = decodeWebSocketMessage();
					isRunning=true;
					if(!inLine.equals("commande reçu")){
						//this.out.println("commande reçu");
						System.out.println("C\tgetIntputStreamServer: "+inLine);
						this.serverRobotino.decodeurJson(inLine);
					}
				}else{//connexion java
					isRunning=false;
					inLine = in.readLine();
					isRunning=true;
					if(!inLine.equals("commande reçu")){
						//this.out.println("commande reçu");
						System.out.println("C\tgetIntputStreamServer: "+inLine);
						this.serverRobotino.decodeurJson(inLine);
					}
				}
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
	public void encodeWebSocketMessage(String message){
		try {
			//byte en byte [-128..127]  byte en int [0..255]
			String requete = "";
			//byte[] bytesMessage = "{\"test\":\"test\"}".getBytes("UTF-8");
			byte[] bytesMessage = message.getBytes("UTF-8");
			ArrayList<Byte> bytes = new ArrayList<Byte>();
			bytes.add((byte) 129);
			//bytes.add((byte) 128);
			//ArrayList<Byte> bytes = new ArrayList<Byte>();
			double messageLength=bytesMessage.length;
			int masked=0;
			byte[] bytesRequete;//requete+message
			int bytesRequeteIndex=0;
			if(messageLength<126){//longueur du message compris sur 7 bits
				bytes.add((byte) (128*masked+messageLength));
				bytesRequete= new byte[bytesMessage.length+2+0+masked*4];
			}else if(messageLength<(2<<16 - 1)){//longueur du message compris sur 16 bits
				bytes.add((byte) (128*masked+126));
				double messageLengthTemp=messageLength;
				int quotient=0;
				for(int i = 2;i>0;i--){
					quotient=(int) (messageLengthTemp/(Math.pow(256, i-1)));
					System.out.println(i+": "+quotient+" = "+messageLengthTemp+"/"+(Math.pow(256, i-1)));
					messageLengthTemp-=quotient*Math.pow(256, i-1);
					bytes.add((byte)quotient);
				}
				bytesRequete= new byte[bytesMessage.length+2+2+masked*4];
			}else{//longueur du message compris sur 64 bits
				bytes.add((byte) (128*masked+127));
				double messageLengthTemp=messageLength;
				int quotient=0;
				for(int i = 8;i>0;i--){
					quotient=(int) (messageLengthTemp/(Math.pow(256, i-1)));
					System.out.println(i+": "+quotient+" = "+messageLengthTemp+"/"+(Math.pow(256, i-1)));
					messageLengthTemp-=quotient*Math.pow(256, i-1);
					bytes.add((byte)quotient);
				}
				bytesRequete= new byte[bytesMessage.length+2+8+masked*4];
			}
			 if (masked==1) {//inutilisé
				 byte maskingKey[] = new byte[4];
				 maskingKey[0] = (byte) 120;//random
				 maskingKey[1] = (byte) 120;//random
				 maskingKey[2] = (byte) 120;//random
				 maskingKey[3] = (byte) 120;//random
				 bytes.add(maskingKey[0]);
				 bytes.add(maskingKey[1]);
				 bytes.add(maskingKey[2]);
				 bytes.add(maskingKey[3]);
	            for (int i = 0; i < bytesMessage.length; i++){
	            	bytesMessage[i] = (byte) ((maskingKey[i % 4] & 0xFF)^bytesMessage[i]);
	            }
			}
			for (int i = 0; i < bytes.size(); i++) {
				bytesRequete[bytesRequeteIndex]=bytes.get(i);
				bytesRequeteIndex++;
			}
			for (int i = 0; i < bytesMessage.length; i++) {
				bytesRequete[bytesRequeteIndex]=bytesMessage[i];
				bytesRequeteIndex++;
			}
			//bytesRequete
			/*bytes.add((byte)bytesMessage[0]);
			bytes.add((byte)bytesMessage[1]);
			bytes.add((byte)bytesMessage[2]);
			bytes.add((byte)bytesMessage[3]);
			System.out.println("test bytes: "+bytes);
			//out.write();
			byte[] b = new byte[0];
			byte[] b2 = bytes.toArray(new byte[0]);*/
			/*System.out.println("test bytes Messages: "+bytesMessage[0]);
			System.out.println("test bytes Messages: "+bytesMessage[1]);
			System.out.println("test bytes Messages: "+bytesMessage[2]);
			System.out.println("test bytes Messages: "+bytesMessage[3]);*/
			//System.out.println("test bytes: "+bytes);
			try {
				socketClient.getOutputStream().write(bytesRequete);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	
			
			//this.out.println(requete); 
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	//https://tools.ietf.org/html/rfc6455#section-5.2
	public String decodeWebSocketMessage() throws IOException{//code trouvé: https://stackoverflow.com/questions/18368130/how-to-parse-and-validate-a-websocket-frame-in-java
		String message="";
		InputStream buf = socketClient.getInputStream();
        message = "";

        // Fin + RSV + OpCode byte
        byte b = (byte) buf.read();
		System.out.println("byte inconu: "+b);
        boolean fin = ((b & 0x80) != 0);
        boolean rsv1 = ((b & 0x40) != 0);
        boolean rsv2 = ((b & 0x20) != 0);
        boolean rsv3 = ((b & 0x10) != 0);
        //frame.opcode = (byte)(b & 0x0F);

        // TODO: add control frame fin validation here
        // TODO: add frame RSV validation here

        // Masked + Payload Length
        b = (byte) buf.read();
        boolean masked = ((b & 0x80) != 0);
        int payloadLength = (0x7F & b);
        int byteCount = 0;
        if (payloadLength == 0x7F) {
            // 8 byte extended payload length
            byteCount = 8;
            payloadLength = 0;
        }
        else if (payloadLength == 0x7E) {
            // 2 bytes extended payload length
            byteCount = 2;
            payloadLength = 0;
        }

        // Decode Payload Length
        while (byteCount-- > 0){
        	b = (byte) buf.read();
            System.out.println("payloadLength1: "+payloadLength+", "+((b & 0xFF) << (8 * byteCount))+", "+(b & 0xFF)+", "+(8 * byteCount));
        	 payloadLength += (b & 0xFF) << (8 * byteCount);
        }

        // TODO: add control frame payload length validation here

        byte maskingKey[] = null;
        if (masked) {
            // Masking Key
            maskingKey = new byte[4];
            buf.read(maskingKey,0,4);
            /*maskingKey[0] = (byte) buf.read();
            maskingKey[1] = (byte) buf.read();
            maskingKey[2] = (byte) buf.read();
            maskingKey[3] = (byte) buf.read();*/
        }

        // TODO: add masked + maskingkey validation here

        // Payload itself
		System.out.println("payloadLength: "+payloadLength);
        byte[] payload = new byte[payloadLength];//texte codé si maked==true
        buf.read(payload,0,payloadLength);
        //buf.get(frame.payload,0,payloadLength);

        // Demask (if needed)
        if (masked){
            for (int i = 0; i < payload.length; i++){
                payload[i] ^= (maskingKey[i % 4] & 0xFF);
            }
        }
		message=new String(payload, "UTF-8");
		return message;
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
