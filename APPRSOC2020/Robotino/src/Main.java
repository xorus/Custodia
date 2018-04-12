import java.util.ArrayList;

/**
 * @author Johann Pistorius
 * @author Thibaud Murtin
 */
public class Main
{
    public static void main(String[] args)
    {
    	ArrayList<String> hostname=new ArrayList<String>();
    	hostname.add("193.48.125.37");
    	hostname.add("193.48.125.38");
    	for(String host:hostname) {
    		Robot r=new Robot(System.getProperty("hostname", host));
    		r.start();
    		if(r.isConnected()) {
    			r.run();
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