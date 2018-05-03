import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class Fenetre extends JFrame {

    private JLabel label2 = new JLabel();
    private JPanel panel = new JPanel();
    private JButton buttonR = new JButton("Right");
    private JButton buttonL = new JButton("Left");
    private JButton buttonF = new JButton("Forward");
    private JButton buttonB = new JButton("Backward");
    
    
    private Robot robot;
    public Fenetre(Robot r) {
    	super("ModeManuelleRobotino");
        this.robot = r;
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.buttonR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					robot.drive(0,50,0,true);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        
        this.buttonL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					robot.drive(0,-50,0,true);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        
        
        this.buttonL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					robot.drive(50,0,0,true);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
    
        
        this.buttonL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					robot.drive(-50,0,0,true);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        
        
        panel.add(this.buttonR);
        panel.add(this.buttonL);
        panel.add(this.buttonF);
        panel.add(this.buttonB);
        
        this.setContentPane(this.panel);
        this.setVisible(true);

    }
}
