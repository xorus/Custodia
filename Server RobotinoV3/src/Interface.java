import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
//https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html
public class Interface extends JFrame implements ActionListener {
	Container contentPane = getContentPane();//toute la fenêtre
	JPanel CenterPanel = new JPanel(new BorderLayout());//centre de la fenêtre
	JPanel MessagePanel = new JPanel(new BorderLayout());//JPanel Envoie message
	JLabel labelNbClics = new JLabel("0 clic");
	JButton bouton1 = new JButton("Cliquez moi !");
	JTextField textFieldMessage = new JTextField();
	JPanel MessageRecuPanel = new JPanel(new BorderLayout());//JPanel Reception message
	JPanel MessageEnvoiPanel = new JPanel(new BorderLayout());//JPanel Reception message
	JPanel MessageEnvoiDestinationPanel = new JPanel();//JPanel Bouton et choix destination

	JTextArea TextAreaMessageRecu = new JTextArea();
	JScrollPane ScrollMessageRecu = new JScrollPane(TextAreaMessageRecu);
	JButton boutonEnvoyerMessage = new JButton("Envoyer");
	JComboBox<String> SelectionDestinataire = new JComboBox<String>();
	int nbClics=0;

	public Interface(String nomFenetre) {
		super(nomFenetre);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400); // ou setBounds(…)
        this.setLocationRelativeTo(null);
		//pack(); // compacte le contenu de la fenêtre
		setVisible(true); // ou show() affiche la fenêtre 
		//MessageRecuPanel.setPreferredSize(new Dimension(300, 600));
		TextAreaMessageRecu.setSize(new Dimension(300, 600));
		MessageEnvoiPanel.setSize(new Dimension(600, 600));
		SelectionDestinataire.setPreferredSize(new Dimension(200,26));
		//MessagePanel.
		//ScrollMessageRecu.setBounds(10,10, 100, 100);
		ScrollMessageRecu.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		TextAreaMessageRecu.setEditable(false);
		TextAreaMessageRecu.setLineWrap(true);
		contentPane.add(CenterPanel, BorderLayout.CENTER);
		contentPane.add(labelNbClics, BorderLayout.NORTH);
		CenterPanel.add(bouton1, BorderLayout.WEST);
		CenterPanel.add(MessagePanel, BorderLayout.CENTER);
		MessagePanel.add(MessageEnvoiPanel, BorderLayout.SOUTH);
		MessagePanel.add(MessageRecuPanel, BorderLayout.CENTER);
		//MessageRecuPanel.add(TextAreaMessageRecu, BorderLayout.WEST);
		MessageRecuPanel.add(ScrollMessageRecu, BorderLayout.CENTER);
		MessageEnvoiPanel.add(textFieldMessage, BorderLayout.CENTER);
		MessageEnvoiPanel.add(MessageEnvoiDestinationPanel, BorderLayout.SOUTH);
		MessageEnvoiDestinationPanel.add(boutonEnvoyerMessage);
		MessageEnvoiDestinationPanel.add(SelectionDestinataire);
		
		bouton1.addActionListener(this);
		textFieldMessage.addActionListener(this);
		boutonEnvoyerMessage.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {//reception d'évenement(appuis bouton par exemple)
		System.out.println("ActionEvent: "+e.getActionCommand());
		if(e.getSource()==bouton1){
			System.out.println("Ne fait rien, implementer la fonction envoyerMessage()");

			this.nbClics+=1;
			if(nbClics>1){
				this.labelNbClics.setText(this.nbClics+" clics");
			}else{
				this.labelNbClics.setText(this.nbClics+" clic");
			}
		}else if(e.getSource()==boutonEnvoyerMessage || e.getSource()==textFieldMessage){//envoie message
			if(textFieldMessage.getText().length()>0 && SelectionDestinataire.isEnabled()){
				if(textFieldMessage.getText().length()>1){
					if(textFieldMessage.getText().substring(0, 2).equals("//")){// si le message commence par //, c'est une commande
						executerCommande(textFieldMessage.getText().substring(2, textFieldMessage.getText().length()));
					}else{envoyerMessage(textFieldMessage.getText());}
				}else{envoyerMessage(textFieldMessage.getText());}
				textFieldMessage.setText("");
			}
		}
		
	}
	public void executerCommande(String commande){
		System.out.println("Ne fait rien, implementer la fonction executerCommande()");
	}
	public void envoyerMessage(String message){
		System.out.println("Ne fait rien, implementer la fonction envoyerMessage()");
	}
	public void closeInterfaceInMS(int mSeconde,String message){
		try {
			TimeUnit.MILLISECONDS.sleep(mSeconde);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

	}

}
