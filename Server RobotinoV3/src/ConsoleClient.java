import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

//permet d'interagir directement avec le server
//Commande disponible:
//	stop
public class ConsoleClient implements Runnable {
	private Client client;
	public boolean isConsoleClientRunning = true;
	

	public ConsoleClient(Client c) {
		client=c;
	}

	@Override
	public void run() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		 try {
			 while(this.client.isClientRunning()){
					if(in.ready()){
						String inLine = in.readLine();
						System.out.println("  Commande Client: "+inLine);
						if (inLine.equals("stop")){
							System.out.println("  Connexion stopped by commande");
							isConsoleClientRunning=false;
							this.client.stopClient();
						}else if(inLine.length()>4 ? inLine.substring(0, 5).equals("send "):false){//si inLine.length()>4 vrai, alors inLine.substring(0, 5).equals("send "), sinon false
							System.out.println("  Envoi du message : "+inLine.substring(5, inLine.length()));
							client.envoieMessage(inLine.substring(5, inLine.length()), "All", "0.0.0.0");

							
						}else if(inLine.length()>2 ? inLine.substring(0, 3).equals("tj "):false){//testJSON
							System.out.println("  testJSON : "+inLine.substring(3, inLine.length()));
							try{
								this.client.decodeurJson(inLine.substring(3, inLine.length()));
							}catch(IndexOutOfBoundsException|java.lang.NullPointerException e){
								System.out.println(e);
							}

						}
						


					}else{
						TimeUnit.MILLISECONDS.sleep(100);
					}
					
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("  InterruptedException in console");
		}
		System.out.println("  Fermeture console");

	}

}
