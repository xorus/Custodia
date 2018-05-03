import java.util.ArrayList;

/**
 * @author Johann Pistorius
 * @author Thibaud Murtin
 */
public class Main
{
    public static void main(String[] args)
    {
    	
    	/*int port=50007;
		String ipServer="192.168.56.1";//iplocal
		ipServer="193.48.125.70";
		ClientRobotino c1 = new ClientRobotino(ipServer,port,"Robotino1_"+(int)(Math.random() * (100000)));
		new Thread(c1).start();*/
    	
    	ArrayList<String> hostname=new ArrayList<String>();
    	hostname.add("193.48.125.37");
    	hostname.add("193.48.125.38");
    	for(String host:hostname) {
    		Robot r=new Robot(System.getProperty("hostname", host));
    		r.start();
    		if(r.isConnected()) {
    			r.run();
    			//Fenetre win=new Fenetre(r);
    			break;
    		}
    	}
    	
    	/*String hostname = System.getProperty("hostname", "193.48.125.37");
    	Robot r=new Robot(hostname);
    	r.run();
    	if(!r.isConnected()){
    		hostname = System.getProperty("hostname", "193.48.125.38");
    		r=new Robot(hostname);
    		r.run();
    	}*/
    }	
}