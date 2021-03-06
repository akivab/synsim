import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class SynapseSimulation {
	static int WIDTH = 100;
	static int HEIGHT = 100;
	static int NUM_MOLECULES = WIDTH*10;
	static boolean debug;
	List<Molecule> all;
	Logger logger;
	
	
	public SynapseSimulation(Logger logger){
		this.all = new ArrayList<Molecule>();
		this.logger = logger;
		debug = false;
		initialize();
		
	}
	
	public SynapseSimulation(Logger logger, boolean init){
		this.all = new ArrayList<Molecule>();
		this.logger = logger;
		if(init){ initialize(); debug = !init; }
		else testInit();
	}
	
	public void initialize(){
		Molecule mol;
		try{
			File dir = new File(SimulationValues.OUTPUT_DIR);
			dir.mkdirs();
			FileWriter outFile = new FileWriter(SimulationValues.OUTPUT_DIR + SimulationValues.INDEX_FILE_NAME);
			PrintWriter out = new PrintWriter(outFile);
			out.println("Molecule force constant: " + SimulationValues.M);
			out.println("Chance (out of 1) of decay per frame: " + SimulationValues.DECAY_RATE);
			out.println("Genesis rate: " + SimulationValues.OKT3_ACTIN_GENESIS_RATE + " (overall: " + SimulationValues.GENESIS_RATE + ")");
			out.println("Force (relative to Actin-Actin) of Myosin on Actin: " + SimulationValues.MYOSIN_ON_ACTIN);
			out.println("Force (relative to Actin-Actin) of Actin on Myosin: " + SimulationValues.ACTIN_ON_MYOSIN);
			out.println("Top Myosin Velocity: " + SimulationValues.MYOSIN_TOP_VELOCITY);
			out.println("Top Actin Velocity: " + SimulationValues.ACTIN_TOP_VELOCITY);
			out.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}

		logger.log(Level.INFO, "Initializing simulation setup");
		for(int i = 0; i < NUM_MOLECULES; i++){
			if(SimulationValues.RAND.nextFloat() < SimulationValues.PERCENT_MYOSIN){
				mol = new Myosin(SimulationValues.RAND.nextInt(WIDTH), SimulationValues.RAND.nextInt(HEIGHT), 
						WIDTH, HEIGHT, logger);
			}
			else{
				mol = new Actin(SimulationValues.RAND.nextInt(WIDTH), SimulationValues.RAND.nextInt(HEIGHT), 
						WIDTH, HEIGHT, logger);
			}
			all.add(mol);
		}
		for(int i = 1; i <= 3; i+=2){
			mol = new Okt3(WIDTH/4 * i, HEIGHT/2, WIDTH, HEIGHT, logger);
			all.add(mol);
		}
	}
	
	public void testInit(){
		Molecule m1 = new Actin(1, 10, WIDTH, HEIGHT, logger);
		Molecule m2 = new Myosin(30, 5, WIDTH, HEIGHT, logger);
		Molecule m3 = new Actin(2, 10, WIDTH, HEIGHT, logger);
		Molecule m4 = new Actin(20, 10, WIDTH, HEIGHT, logger);
		Molecule m5 = new Myosin(30,15, WIDTH, HEIGHT, logger);
		all.add(m1);
		all.add(m2);
		all.add(m3);
		all.add(m4);
		all.add(m5);
	}
	
	public void step(){
		logger.log(Level.INFO, "Stepping the simulation one step further");
		ArrayList<Molecule> copy = new ArrayList<Molecule>();
		copy.addAll(all);
		
		for(Molecule m : copy)
			m.move(all);
		
		for(Iterator<Molecule> itr = all.iterator(); itr.hasNext(); )
			if(itr.next().toRemove) itr.remove();
		
		for(Molecule m : copy)
			m.update(all);
				
		if(!debug)
			generateMolecules();
	}
	
	public void generateMolecules(){
		for(int i = 0; i < SimulationValues.GENESIS_RATE * all.size(); i++){
			float x = (float) (SimulationValues.RAND.nextDouble() * WIDTH);
			float y = (float) (SimulationValues.RAND.nextDouble() * HEIGHT);
			double r = SimulationValues.RAND.nextDouble();
			if(r < SimulationValues.ACTIN_PERCENT_GENESIS) all.add(new Actin(x,y, WIDTH, HEIGHT, logger));
			else if (r < SimulationValues.MYOSIN_PERCENT_GENESIS) all.add(new Myosin(x,y, WIDTH, HEIGHT, logger));
		}
	}
	
	public void savePaint(int i, Dimension d){
		  BufferedImage image = new BufferedImage((int) d.getWidth(), (int) d.getHeight(), BufferedImage.TYPE_INT_RGB);
	      Graphics2D graphics2D = image.createGraphics();
	      drawAll(graphics2D, d);
	      try {
			ImageIO.write(image,"jpeg", new File(SimulationValues.OUTPUT_DIR + i + ".jpeg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
	
	public void drawAll(Graphics g, Dimension d){
		logger.log(Level.INFO, "Drawing all the molecules");
		ArrayList<Molecule> copy = new ArrayList<Molecule>();
		copy.addAll(all);
		g.setColor(Color.white);
		g.fillRect(0,0, d.width, d.height);

		for(Molecule m : copy){
			Color color = Color.blue;
			if(m.t == Type.MYOSIN){
				color = Color.green;
			}
			else if(m.t == Type.OKT3){
				color = Color.red;
				m.radius = Molecule.boundary/4;
			}
			
			g.setColor(color);
			float sx = d.width / ((float) WIDTH);
			float sy = d.height / ((float) HEIGHT);
			
			g.fillOval((int)(sx * (m.pos.x-m.radius/2)), (int)(sy * (m.pos.y-m.radius/2)), (int) (sx * m.radius)+1, (int) (sy * m.radius)+1);
		}
	}
}
