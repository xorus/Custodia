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
	public void envoyerMessage(String message){
		client.envoieMessage(message, SelectionDestinataire.getSelectedItem().toString(), "0.0.0.0");
	}

}
