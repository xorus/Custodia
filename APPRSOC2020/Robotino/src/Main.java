// port of the c++ example
// without callbacks, as java does not support them directly

public class Main
{
    public static void main(String[] args)
    {
        String hostname = System.getProperty("hostname", "193.48.125.37");
        new Robot(hostname).run();
    }	
}