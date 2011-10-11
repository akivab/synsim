import java.awt.Dimension;
import java.awt.Graphics;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

import javax.swing.*;
public class Simulation {
	static boolean DEBUG = false;
	public static void main(String args[]){
		Logger logger = getLogger();
		
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension windowSize = new Dimension(500,500);
		
		SimulationPanel s = new SimulationPanel(logger);
		window.getContentPane().add(s);
		window.setSize(windowSize);
		window.setVisible(true);
		int i = 0;
		while(i < 1000){
			s.sim.step();
			s.sim.savePaint(i++, windowSize);
			window.repaint();
		}
	}
	
	public static Logger getLogger(){
		Logger logger = null;
		try {
		      LogManager lm = LogManager.getLogManager();
		      FileHandler fh = new FileHandler("simulation.log");
		      logger = Logger.getLogger("Simulation");

		      lm.addLogger(logger);
		      logger.setLevel(Level.OFF);
		      fh.setFormatter(new XMLFormatter());

		      logger.addHandler(fh);
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
		return logger;
	}
}

class SimulationPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	public SynapseSimulation sim;
	
	public SimulationPanel(Logger logger){
		this.sim = new SynapseSimulation(logger, !Simulation.DEBUG);
	}
		
	public void paint(Graphics g){
		this.sim.drawAll(g, this.getSize());
	}
}
