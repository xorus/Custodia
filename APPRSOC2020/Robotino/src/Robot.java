import rec.robotino.com.Bumper;
import rec.robotino.com.Com;
import rec.robotino.com.Motor;
import rec.robotino.com.OmniDrive;

import static java.lang.Math.*;

/**
 * @author Johann Pistorius
 * @author Thibaud Murtin
 */
public class Robot implements Runnable
{
    protected final String hostname;
    protected final Com com;
    protected final Motor motor1;
    protected final Motor motor2;
    protected final Motor motor3;
    protected final OmniDrive omniDrive;
    protected final Bumper bumper;
    protected final float[] startVector = new float[]
    {
        200.0f, 0.0f
    };
    protected boolean connectionStatus;

    public Robot(String hostname)
    {
        this.hostname = hostname;
        com = new Com();
        motor1 = new Motor();
        motor2 = new Motor();
        motor3 = new Motor();
        omniDrive = new OmniDrive();
        bumper = new Bumper();
    }

    public void start()
    {
        System.out.println("Robot started.");
        
        try
        {
            System.out.println("Initializing...");
            init();
            System.out.println("Connecting...");
            connect(hostname);
            System.out.println("Connected.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            disconnect();
        }

        System.out.println("Done.");
    }

    protected void init()
    {
        motor1.setComId(com.id());
        motor1.setMotorNumber(0);

        motor2.setComId(com.id());
        motor2.setMotorNumber(1);

        motor3.setComId(com.id());
        motor3.setMotorNumber(2);

        omniDrive.setComId(com.id());

        bumper.setComId(com.id());
    }

    protected void connect(String hostname)
    {
        com.setAddress(hostname);
        com.connect();
    }

    protected void disconnect()
    {
        com.disconnect();
    }
    
    protected boolean isConnected(){
    	return com.isConnected();
    }
    public void run() {
    	/*System.out.println("Driving...");
        drive();*/
        try {
			drive(100,0,-30,true);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /*
    protected void circle() throws InterruptedException{
    	float[] dir;
        float a = 0.0f;
        long startTime = System.currentTimeMillis();
        int millisecondsElapsed = 0;
        while (!Thread.interrupted() && com.isConnected() && false == bumper.value())
        {
			long elapsedTime = System.currentTimeMillis() - startTime;
        
            //rotate by 360 degrees every 10 seconds
            dir = rotate(startVector, a);
            a = 360.0f * elapsedTime / 10000;
            
            
            omniDrive.setVelocity(dir[0], dir[1], 0);
        }
    }*/
    protected void drive(int x, int y, int angle, boolean status) throws InterruptedException{

        while (!Thread.interrupted() && com.isConnected() && false == bumper.value() && status == true)
        {
            omniDrive.setVelocity(x, y, angle);
        }
    }
    /**
     * Rotate a 2 dimensional vector
     * 
     * @param in Input vector
     * @param deg Rotation in degrees
     * @return Output vector
     */
    private float[] rotate(float[] in, float deg)
    {
        final float pi = 3.14159265358979f;

        float rad = 2 * pi / 360.0f * deg;

        float[] out = new float[2];
        out[0] = (float) (cos(rad) * in[0] - sin(rad) * in[1]);
        out[1] = (float) (sin(rad) * in[0] + cos(rad) * in[1]);
        return out;
    }
}