import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class InterfaceServer extends Interface{
	ServerRobotino serverRobotino;
	
	public InterfaceServer(ServerRobotino s) {
		super("fenêtre server "+s.getNom());
		this.serverRobotino=s;
		
	}

	public void actionPerformed(ActionEvent e) {//reception d'évenement(appuis bouton par exemple)
		super.actionPerformed(e);
		
	}
	public void executerCommande(String commande){
		super.textFieldMessage.setText("");
		String[] partOfCommande = commande.split(" ");
		System.out.println(commande+"  :"+partOfCommande[0]);
		if(partOfCommande[0].equals("stop")){
			this.TextAreaMessageRecu.append("\nServer stopped by command\n");
			serverRobotino.stopServer();
		}else if(partOfCommande[0].equals("all")||partOfCommande[0].equals("All")){
			serverRobotino.envoieMessage(commande.substring(4, commande.length()), "All", "0.0.0.0");
			
		}else if((partOfCommande[0].equals("cr")||partOfCommande[0].equals("commandeRobotino"))&&partOfCommande.length>3){
			if(partOfCommande[1].equals("av")||partOfCommande[1].equals("avancer")){
				serverRobotino.envoiRequete("{\"type\":\"commandeRobotino\",\"commande\":\"avancer\",\"infoCommande\":{\"posX\":\""+partOfCommande[2]+"\",\"posY\":\""+partOfCommande[3]+"\"}}", SelectionDestinataire.getSelectedItem().toString(), "0.0.0.0");
			}else{
				this.TextAreaMessageRecu.append("Incorrect argument:"+partOfCommande[1]+"\n");
			}
		}else{
			this.TextAreaMessageRecu.append("Incorrect command:"+partOfCommande[0]+"\n");
		}
	}
	public void envoyerMessage(String message){
		serverRobotino.envoieMessage(message, SelectionDestinataire.getSelectedItem().toString(), "0.0.0.0");
	}

}
