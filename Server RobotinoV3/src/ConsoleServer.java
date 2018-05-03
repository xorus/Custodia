import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

//permet d'interagir directement avec le server
//Commande disponible:
//	stop
/**
 * Classe contenant une console en ligne de commande pour l'envoie et la reception de commandes
 * @author prospere
 *
 */
public class ConsoleServer implements Runnable {
	private ServerRobotino serverRobotino;
	

	public ConsoleServer(ServerRobotino s) {
		serverRobotino=s;
	}
	
	/**
	 * Execute la lecture de la console et des buffers. PErmet l'affichage et l'envoie de commandes.
	 * Actuellement implémenté:
	 * "send $message" 	Envoie un message texte au client
	 * "stop"			Envoie la commande d'arrêt du serveur
	 */
	@Override
	public void run() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		 try {
			 while(this.serverRobotino.isServerRunning()){
					if(in.ready()){
						String inLine = in.readLine();
						System.out.println("  Commande Server: "+inLine);
						if (inLine.equals("stop")){
							System.out.println("  Server stopped by commande");
							this.serverRobotino.stopServer();
						}else if(inLine.length()>4){
							if(inLine.substring(0, 5).equals("send ")){
								System.out.println("  Envoi du message : "+inLine.substring(5, inLine.length()));
								String message = "{ \"type\":\"message\",\"infoMessage\":{\"texte\":\""+inLine.substring(5, inLine.length())+"\",\"destinataire\":{\"dName\":\"All\",\"dIP\":\"0.0.0.0\"},\"expediteur\":{\"eName\":\""+this.serverRobotino.getNom()+"\",\"eIP\":\""+this.serverRobotino.getIp()+":"+this.serverRobotino.getPortServeur()+"\"}}}";

								this.serverRobotino.envoiRequete(message, "All", "0.0.0.0");
								/*for (int i = this.serverRobotino.getConnexions().size()-1; i >= 0; i--) {
									try{
										System.out.println("Message envoyé à la connexion n°"+i);
										this.serverRobotino.getConnexions().get(i).getOut().println(inLine.substring(5, inLine.length()));
									}catch(IndexOutOfBoundsException|java.lang.NullPointerException e){
										System.out.println(e);
									}
	
								}*/
							}
							
						}
					}else{
						TimeUnit.MILLISECONDS.sleep(100);
					}
					
				}//Fermeture connxion
			 /*while(this.serverRobotino.isServerRunning()){
					//if(in.){
				 		String line = in.readLine();
						System.out.println("  Commande: "+line);
						System.out.println("  "+line.substring(0, 5));
						if (line.equals("stop")){
							this.serverRobotino.stopServer();
						}else if(line.substring(0, 5).equals("send ")){
							System.out.println("  Envoi du message : "+line.substring(5, line.length()-1));
							
						}
					//}else{
						TimeUnit.MILLISECONDS.sleep(100);
					//}
					
				}//Fermeture connxion*/
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("  Fermeture console");

	}

}
