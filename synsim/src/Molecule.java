import java.awt.Dimension;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Molecule{
	static float M = 0.5f;
//	static float G = 0.1f;
	static int GENESIS_RATE = 1;
	static double DECAY_RATE = 0.05;
	static float ACTIN_ON_MYOSIN = 0.05f;
	static float MYOSIN_ON_ACTIN = 0.03f;
	static float MYOSIN_TOP_VELOCITY = 0.3f;
	static float ACTIN_TOP_VELOCITY = 1.2f;
	static float REGEN = 0.01f;
	
	static float boundary = 20; // boundary of force
	boolean toRemove; // whether to remove molecule from simulation
	float radius = 1; // radius of the molecule
	
	int MAX_AFFECT = 20;

	Logger logger;
	FloatPoint pos = new FloatPoint(); // molecule position
	FloatPoint velocity = new FloatPoint(); // molecule velocity

	Dimension dim; // window dimension
	Random rand; // random number generator
	Type t; // type of molecule

	/**
	 * Constructor for molecule object
	 * @param p float point for position
	 * @param d dimension of window
	 * @param logger logger of the simulation
	 */
	public Molecule(FloatPoint p, Dimension d, Logger logger){
		this.dim = d;
		this.pos = p;
		this.logger = logger;
		this.rand = new Random();
	}
	
	/**
	 * Checks whether a position at (x,y) is valid given dimension of simulation
	 * @param x x-position
	 * @param y y-position
	 * @return true if (x,y) is in dimensions specified
	 */
	protected boolean isValid(float x, float y){
		return x > 0 && x < dim.width && y > 0 && y < dim.height;
	}
	
	/**
	 * Updates the molecule's position
	 */
	public void move(List<Molecule> all){
		logger.log(Level.INFO, String.format("Moving molecule %s", toString()));
		if(t != Type.OKT3){
			pos.x += velocity.x;
			pos.y += velocity.y;
			double tmp = rand.nextDouble();
			if(!SynapseSimulation.debug && tmp < DECAY_RATE){
				if(Type.ACTIN == t || tmp*100 < DECAY_RATE) toRemove = true;
				if(Type.ACTIN == t){
					// generateActin around this guy
					if(Math.random() < 0.3)
						generateActin(all, pos.x, pos.y, boundary/2, 3);
				}
			}
		}
		else{
			generateActin(all);
		}
	}
	
	public void generateActin(List<Molecule> all, float cx, float cy, float bound, int gen_rate){
		float x, y;
		int w = dim.width, l = dim.height;
		double r;
		Molecule m;
		for(int i = 0; i < gen_rate; i++){
			r = (rand.nextDouble() + 0.5) * Math.PI * 2;
			x = (float) (rand.nextDouble() * bound * Math.cos(r) + cx);
			y = (float) (rand.nextDouble() * bound * Math.sin(r) + cy);
			
			m = new Actin(x, y, w, l, logger);
			all.add(m);
		}
	}
	/**
	 * Generates Actin around OKT3
	 * @param all
	 */
	public void generateActin(List<Molecule> all){
		generateActin(all, this.pos.x, this.pos.y, boundary, GENESIS_RATE);
	}
	
	/**
	 * Updates the molecule's position given a list of all the molecules
	 * @param all a list of all the molecules
	 */
	public void update(List<Molecule> all){
		logger.log(Level.INFO, String.format("Updating molecule %s", toString()));
		this.toRemove = outOfFrame();
		if(t == Type.OKT3){
			return;
		}
		int acount = 0, mcount = 0;
		for(Molecule m : all){
			if(m.isNear(this)){
				if(m.t == Type.ACTIN && acount++ < MAX_AFFECT || m.t == Type.MYOSIN && mcount++ < MAX_AFFECT)
				m.updateVelocity(this);
			}
		}
		limitVelocity();
	}
	
	/**
	 * Checks if a molecule is near this one, using boundary
	 * @param other other molecule to check if near
	 * @return whether other molecule is near
	 */
	public boolean isNear(Molecule other){
		return !this.equals(other) && other.pos.distance(this.pos) < boundary;
	}
	
	/**
	 * Checks if a molecule is out of the frame
	 * @return true if out of frame
	 */
	public boolean outOfFrame(){
		return !isValid(pos.x, pos.y);
	}
	
	/**
	 * Updates the velocity of a molecule given the force applied by another molecule.
	 * @param m other molecule applying force
	 */
	public void updateVelocity(Molecule m){
		FloatPoint vel = getVelocity(m,this); 
		m.velocity.x += vel.x;
		m.velocity.y += vel.y;
		
		// terminal velocities
		limitVelocity();
	}
	
	/**
	 * Limits the velocity of a molecule if it is going too fast 
	 * @param m Molecule to limit velocity of
	 */
	public void limitVelocity(){
		float f = MYOSIN_TOP_VELOCITY;
		if(t == Type.ACTIN) f = ACTIN_TOP_VELOCITY;
		if(Math.abs(velocity.x) > f) velocity.x = f * Math.signum(velocity.x);
		if(Math.abs(velocity.y) > f) velocity.y = f * Math.signum(velocity.y);
	}
	
	/**
	 * Gets the force of one molecule on other
	 * @param f1 molecule exerting force
	 * @param f2 molecule taking force
	 * @return force exerted on a molecule
	 */
	public static FloatPoint getVelocity(Molecule f1, Molecule f2){
		//System.out.println(f1);
		//System.out.println(f2);
		float x1 = f1.pos.x, y1 = f1.pos.y;
		float x2 = f2.pos.x, y2 = f2.pos.y;
		float d = (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
		float x3 = M * (x1-x2)/d;
		float y3 = M * (y1-y2)/d;
		if(d < 2)
		{
			x3 = M * (x1-x2) / 2;
			y3 = M * (y1-y2) / 2;
		}
		if(f2.t == Type.MYOSIN && f1.t == Type.ACTIN){ x3 *= -MYOSIN_ON_ACTIN; y3 *= -MYOSIN_ON_ACTIN; }
		else if(f1.t == Type.MYOSIN && f2.t == Type.ACTIN){ x3 *= -ACTIN_ON_MYOSIN; y3 *= -ACTIN_ON_MYOSIN; }
		else if (f1.t == Type.MYOSIN && f2.t == Type.MYOSIN){ x3 = 0; y3  = 0; }
		return new FloatPoint(x3 , y3);
	}

	public String toString(){
		return "" + t + " " + pos;
	}
}