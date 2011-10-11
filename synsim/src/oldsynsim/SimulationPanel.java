package oldsynsim;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

class SimulationPanel extends JPanel
{
  private static final long serialVersionUID = 1L;
  private ArrayList<Molecule> molecules;
  private int scale;
  private Dimension winSize;

  public SimulationPanel(Dimension windowSize, int scale)
  {
    this.molecules = new ArrayList<Molecule>();
    this.scale = scale;
    this.winSize = windowSize;
    setup();
  }

  private void setup() {
    System.out.println("Setting up the panel...");
    int width = (int)(this.winSize.getWidth() / this.scale);
    int height = (int)(this.winSize.getHeight() / this.scale);
    Random r = new Random();
    double m = r.nextDouble();
    for (int i = 0; i < width; i += width / 50) {
      for (int j = 0; j < height; j += height / 50) {
        if (m > 0.2D) this.molecules.add(new Myosin(new Point(i, j))); else
          this.molecules.add(new Actin(new Point(i, j)));
        m = r.nextDouble();
      }
    }
    this.molecules.add(new Okt3(this.winSize, true));
    this.molecules.add(new Okt3(this.winSize, false));
  }

  public void update()
  {
    Molecule m;
    Iterator<Molecule> localIterator;
    for (localIterator = copy().iterator(); localIterator.hasNext(); m.update(this.molecules)) m = localIterator.next();
    for (localIterator = this.molecules.iterator(); localIterator.hasNext(); m.move()) m = localIterator.next(); 
  }

  public void savePaint(int i){
	  BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
      Graphics2D graphics2D = image.createGraphics();
      paint(graphics2D);
      try {
		ImageIO.write(image,"jpeg", new File("/home/akiva/Desktop/images/image"+i+".jpeg"));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  public void paint(Graphics g2)
  {
    Graphics2D g = (Graphics2D)g2;
    Molecule m;
    for (Iterator<Molecule> localIterator = this.molecules.iterator(); localIterator.hasNext(); m.draw(g, this.scale)) m = localIterator.next();
  }

  @SuppressWarnings("unchecked")
private ArrayList<Molecule> copy()
  {
    return (ArrayList<Molecule>)this.molecules.clone();
  }
}