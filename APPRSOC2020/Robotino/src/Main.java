// port of the c++ example
// without callbacks, as java does not support them directly
/**
 * @author Johann Pistorius
 * @author Thibaud Murtin
 */
public class Main
{
    public static void main(String[] args)
    {
    	//METTRE LES ADRESSES IP EN TABLEAU ET GERER LA CONNEXION
    	String hostname = System.getProperty("hostname", "193.48.125.37");
    	Robot r=new Robot(hostname);
    	r.run();
    	if(!r.isConnected()){
    		hostname = System.getProperty("hostname", "193.48.125.38");
    		r=new Robot(hostname);
    		r.run();
    	}
    }	
}