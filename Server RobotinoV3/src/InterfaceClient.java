import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class InterfaceClient extends Interface{
	Client client;
	
	public InterfaceClient(Client c) {
		super("fenêtre client "+c.nom);
		this.client=c;
		
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
			client.stopClient();
		}else if(partOfCommande[0].equals("all")||partOfCommande[0].equals("All")){
			client.envoieMessage(commande.substring(4, commande.length()), "All", "0.0.0.0");
			
		}else if((partOfCommande[0].equals("cr")||partOfCommande[0].equals("commandeRobotino"))&&partOfCommande.length>3){
			if(partOfCommande[1].equals("av")||partOfCommande[1].equals("avancer")){
				client.out.println("{\"type\":\"commandeRobotino\",\"commande\":\"avancer\",\"infoCommande\":{\"posX\":\""+partOfCommande[2]+"\",\"posY\":\""+partOfCommande[3]+"\"}}");
			}else{
				this.TextAreaMessageRecu.append("Incorrect argument:"+partOfCommande[1]+"\n");
			}
		}else{
			this.TextAreaMessageRecu.append("Incorrect command:"+partOfCommande[0]+"\n");
		}
	}
	public void envoyerMessage(String message){
		client.envoieMessage(message, SelectionDestinataire.getSelectedItem().toString(), "0.0.0.0");
	}

}
