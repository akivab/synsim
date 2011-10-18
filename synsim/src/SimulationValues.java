import java.util.Random;

public class SimulationValues {
	static boolean DEBUG = false;
	static boolean VISUALIZATION = false;
	
	static double M = 0.5;
	static double GENESIS_RATE = 0.005;
	static double PERCENT_MYOSIN = 1.0;
	static double MYOSIN_PERCENT_GENESIS = 0.5;
	static double ACTIN_PERCENT_GENESIS = 0.2;
	static double DECAY_RATE = 0.05;
	static double ACTIN_ON_MYOSIN = 0.05;
	static double MYOSIN_ON_ACTIN = 0.03;
	static double MYOSIN_TOP_VELOCITY = 0.3;
	static double ACTIN_TOP_VELOCITY = 1.2;
	static double REGEN = 0.01;
	
	static Random RAND = new Random(10);
	
	static int OKT3_ACTIN_GENESIS_RATE = 1;
	static int MAX_AFFECT = 20;
	static int NUM_STEPS = 1000;
	
	static String OUTPUT_DIR = "output/";
	static String INDEX_FILE_NAME = "index1.txt";
	static String ACTIN_FILE_NAME = "actin.csv";
	static String MYOSIN_FILE_NAME = "myosin.csv";
	
}
