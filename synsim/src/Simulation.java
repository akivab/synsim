import java.awt.Dimension;
import java.awt.Graphics;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Simulation {
	
	public static void HELP(){
		String toprint = "Usage: java -jar synsim.jar <arg1=val1> <arg2=val2>\n";
		toprint += "\n";
		toprint += "Possible arguments:";
		toprint += "\n\tGENESIS_RATE:\t(0-1000)";
		toprint += "\tdefault: " + SimulationValues.GENESIS_RATE + "\n";
		toprint += "\tPERCENT_MYOSIN:\t(0-100)";
		toprint += "\tdefault: " + SimulationValues.PERCENT_MYOSIN + "\n";
		toprint += "\tMYOSIN_PERCENT_GENESIS (0-100):\tPercent myosin";
		toprint += "\tdefault: " + SimulationValues.MYOSIN_PERCENT_GENESIS + "\n";
		toprint += "\tACTIN_PERCENT_GENESIS (0-100):\tPercent actin";
		toprint += "\tdefault: " + SimulationValues.ACTIN_PERCENT_GENESIS + "\n";
		toprint += "\tACTIN_ON_MYOSIN (0-100):\tActin on myosin";
		toprint += "\tdefault: " + SimulationValues.ACTIN_PERCENT_GENESIS + "\n";
		toprint += "\tMYOSIN_ON_ACTIN (0-100):\tMyosin on actin";
		toprint += "\tdefault: " + SimulationValues.MYOSIN_ON_ACTIN + "\n";
		toprint += "\tMYOSIN_TOP_VELOCITY (0-?)\tMyosin top velocity";
		toprint += "\tdefault: " + SimulationValues.MYOSIN_TOP_VELOCITY + "\n";
		toprint += "\tACTIN_TOP_VELOCITY (0-?)\tActin top velocity";
		toprint += "\tdefault: " + SimulationValues.ACTIN_TOP_VELOCITY + "\n";
		toprint += "\tDECAY_RATE (0-100)\tActin/Myosin decay rate";
		toprint += "\tdefault: " + SimulationValues.DECAY_RATE + "\n";
		toprint += "\tOUTPUT_DIR\tOutput directory";
		toprint += "\tdefault: " + SimulationValues.OUTPUT_DIR + "\n";
		toprint += "\tNUM_STEPS (0-?)\tNumber of steps in simulation";
		toprint += "\tdefault: " + SimulationValues.NUM_STEPS + "\n";
		toprint += "\tMYOSIN_FILE_NAME";
		toprint += "\tdefault: " + SimulationValues.MYOSIN_FILE_NAME + "\n";
		toprint += "\tACTIN_FILE_NAME";
		toprint += "\tdefault: " + SimulationValues.ACTIN_FILE_NAME + "\n";
		System.out.println(toprint);
	}
	public static void PARSE(String args[]) {
		for (String arg : args) {
			// VARIABLES TO SET:
			// GENESIS_RATE (out of 1000, default 5)
			// PERCENT_MYOSIN (out of 100, default 100)
			// DECAY_RATE (out of 100, default 5)
			// MYOSIN_PERCENT_GENESIS (out of 100, default 50)
			// ACTIN_PERCENT_GENESIS (out of 100, default 20);
			// MYOSIN_ON_ACTIN (out of 100, default 3),
			// ACTIN_ON_MYOSIN (out of 100, default 5),
			// MYOSIN_TOP_VELOCITY (out of 100, default 30)
			// ACTIN_TOP_VELOCITY (out of 100, default 120)
			// MAX_EFFECT (default 20)
			StringTokenizer str = new StringTokenizer(arg, "=");
			String argument = "";
			String value = "";
			if (str.hasMoreTokens()) {
				argument = str.nextToken();
				if (str.hasMoreTokens()) {
					value = str.nextToken();
					int val = 0;
					try {
						val = Integer.parseInt(value);
					} catch (Exception e) {
					}
					if (argument.equals("GENESIS_RATE"))
						SimulationValues.GENESIS_RATE = ((double) val) / 1000;
					else if (argument.equals("PERCENT_MYOSIN"))
						SimulationValues.PERCENT_MYOSIN = ((double) val) / 100;
					else if (argument.equals("MYOSIN_PERCENT_GENESIS"))
						SimulationValues.MYOSIN_PERCENT_GENESIS = ((double) val) / 100;
					else if (argument.equals("ACTIN_PERCENT_GENESIS"))
						SimulationValues.ACTIN_PERCENT_GENESIS = ((double) val) / 100;
					else if (argument.equals("ACTIN_ON_MYOSIN"))
						SimulationValues.ACTIN_ON_MYOSIN = ((double) val) / 100;
					else if (argument.equals("MYOSIN_ON_ACTIN"))
						SimulationValues.MYOSIN_ON_ACTIN = ((double) val) / 100;
					else if (argument.equals("MYOSIN_TOP_VELOCITY"))
						SimulationValues.MYOSIN_TOP_VELOCITY = ((double) val) / 100;
					else if (argument.equals("ACTIN_TOP_VELOCITY"))
						SimulationValues.ACTIN_TOP_VELOCITY = ((double) val) / 100;
					else if (argument.equals("REGEN"))
						SimulationValues.REGEN = ((double) val) / 100;
					else if (argument.equals("DECAY_RATE"))
						SimulationValues.DECAY_RATE = ((double) val) / 100;
					else if (argument.equals("OUTPUT_DIR"))
						SimulationValues.OUTPUT_DIR = value;
					else if (argument.equals("ACTIN_FILE_NAME"))
						SimulationValues.ACTIN_FILE_NAME = value;
					else if (argument.equals("MYOSIN_FILE_NAME"))
						SimulationValues.MYOSIN_FILE_NAME = value;
					else if (argument.equals("NUM_STEPS"))
						SimulationValues.NUM_STEPS = val;
				}
			}
		}
	}

	public static void main(String args[]) {
		if (args.length < 1)
			SimulationValues.VISUALIZATION = true;
		else if (args[0].equals("--help")){
			HELP();
			System.exit(0);
		}
		else{
			PARSE(args);
		}
		Logger logger = getLogger();

		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension windowSize = new Dimension(500, 500);

		
		SimulationPanel s = new SimulationPanel(logger);
		if(SimulationValues.VISUALIZATION){
			window.getContentPane().add(s);
			window.setSize(windowSize);
			window.setVisible(true);
		}
		int i = 0;
		int percent_complete=-1;
		while (i < SimulationValues.NUM_STEPS) {
			s.sim.step();
			if(i % (SimulationValues.NUM_STEPS/100) == 0){
				percent_complete++;
				System.out.print("\r" + percent_complete + "% complete");
			}
			if (SimulationValues.VISUALIZATION) {
				s.sim.savePaint(i, windowSize);
				window.repaint();
			}
			i++;
			if(i == SimulationValues.NUM_STEPS)
				try {
					WRITE_DOWN_POSITIONS(s.sim);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static synchronized void WRITE_DOWN_POSITIONS(SynapseSimulation sim) throws IOException {
		FileWriter actinFile = new FileWriter(SimulationValues.OUTPUT_DIR
				+ SimulationValues.ACTIN_FILE_NAME);
		FileWriter myosinFile = new FileWriter(SimulationValues.OUTPUT_DIR
				+ SimulationValues.MYOSIN_FILE_NAME);

		PrintWriter actinOut = new PrintWriter(actinFile);
		PrintWriter myosinOut = new PrintWriter(myosinFile);
		int actinCount = 0, myosinCount = 0;
		for (Molecule m : sim.all)
			if (m.t == Type.ACTIN) {
				actinOut.println(actinCount + "," + m.pos.x + "," + m.pos.y
						+ "," + m.velocity.x + "," + m.velocity.y);
				actinCount++;
			} else if (m.t == Type.MYOSIN) {
				myosinOut.println(myosinCount + "," + m.pos.x + "," + m.pos.y
						+ "," + m.velocity.x + "," + m.velocity.y);
				myosinCount++;
			}
		actinOut.close();
		myosinOut.close();
	}

	public static Logger getLogger() {
		Logger logger = null;
		try {
			LogManager lm = LogManager.getLogManager();
			FileHandler fh = new FileHandler("simulation.log");
			logger = Logger.getLogger("Simulation");

			lm.addLogger(logger);
			logger.setLevel(Level.OFF);
			fh.setFormatter(new XMLFormatter());

			logger.addHandler(fh);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return logger;
	}
}

class SimulationPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public SynapseSimulation sim;

	public SimulationPanel(Logger logger) {
		this.sim = new SynapseSimulation(logger, !SimulationValues.DEBUG);
	}

	public void paint(Graphics g) {
		this.sim.drawAll(g, this.getSize());
	}
}
