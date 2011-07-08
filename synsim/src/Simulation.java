import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.*;
public class Simulation {
	public static void main(String args[]){
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension windowSize = new Dimension(500,500);
		int scale = 1;
		SimulationPanel s = new SimulationPanel(windowSize, scale);
		window.getContentPane().add(s);
		window.setSize(windowSize);
		window.setVisible(true);
		while(true){
			s.update();
			window.repaint();
		}
	}
}

class SimulationPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private ArrayList<Molecule> molecules;
	private int scale;
	private Dimension winSize;
	public SimulationPanel(Dimension windowSize, int scale){
		this.molecules = new ArrayList<Molecule>();
		this.scale = scale;
		this.winSize = windowSize;
		setup();
	}
	
	private void setup(){
		System.out.println("Setting up the panel...");
		int width = (int) (winSize.getWidth() / scale);
		int height = (int) (winSize.getHeight() / scale);
		Random r = new Random();
		double m = r.nextDouble();
		for(int i = 0; i < width; i += width / 50){
			for(int j = 0; j < height; j+= height/50){
				if(m > .2) molecules.add(new Myosin(winSize, new Point(i,j)));
				else molecules.add(new Actin(winSize, new Point(i,j)));
				m = r.nextDouble();
			}
		}
		molecules.add(new Okt3(winSize, true));
		molecules.add(new Okt3(winSize, false));
	}
	
	public void update(){
		//System.out.println("Updating molecules...");
		for(Molecule m : copy()){ m.update(molecules); }//System.out.println(m); }
		//System.out.println("Moving molecules...");
		for(Molecule m : molecules) m.move();
		for(Iterator<Molecule> itr = molecules.iterator(); itr.hasNext(); )
			if(itr.next().toRemove) itr.remove();
	}
	
	public void paint(Graphics g2){
		Graphics2D g = (Graphics2D) g2;
		for(Molecule m : molecules) m.draw(g, scale);
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<Molecule> copy(){
		return (ArrayList<Molecule>) molecules.clone();
	}
}
