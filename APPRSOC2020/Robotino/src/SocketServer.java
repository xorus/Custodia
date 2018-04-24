import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketServer {


	   public static void main(String[] args) {
		   	 int port = 6000; //port
		   	 String ip = "127.0.0.1"; //ip server
	         try {
	             Socket soc = new Socket("127.0.0.1", port); //mettre ip server
	             System.out.println("connexion autorisée port : " + port);
	         
	         
	         //mettre le client en ecoute du serveur
	         //recevoir json pour la classe ParseJson
	             
	         //todo envoyer bumper
	             
	             
	         } catch (UnknownHostException e) {
	            e.printStackTrace();
	         }catch (IOException e) {
	            //Si une exception de ce type est levée
	            //c'est que le port n'est pas ouvert ou n'est pas autorisé
	         }
	    }
}
