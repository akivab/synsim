package oldsynsim;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;
import java.util.Random;

public class Okt3 extends Molecule
{
  private int border;
  private double genesisRate;

  public Okt3(Point location, int r, int border, int genesisRate)
  {
    initialize(location, r, border, genesisRate);
  }

  public Okt3(Dimension board, boolean left) {
    Point location = new Point((int)((left ? 1 : 3) * board.getWidth() / 4.0D), (int)(board.getHeight() / 2.0D));
    int r = (int)Math.min(board.getWidth() / 20.0D, board.getHeight() / 20.0D);
    int border = r;
    double genesisRate = 1.0D;

    initialize(location, r, border, genesisRate);
  }

  public void initialize(Point location, int r, int border, double genesisRate) {
    this.location = location;
    this.r = r;
    this.border = border;
    this.genesisRate = genesisRate;
    this.t = type.OKT3;
  }

  public void update(List<Molecule> allMolecules)
  {
    Random rand = new Random();

    if (rand.nextDouble() < this.genesisRate) {
      double randNum = rand.nextDouble();
      int x = (int)(Math.cos(randNum * 3.141592653589793D * 2.0D) * this.border);
      int y = (int)(Math.sin(randNum * 3.141592653589793D * 2.0D) * this.border);
      x += this.location.x;
      y += this.location.y;
      allMolecules.add(new Actin(new Point(x, y)));
    }
  }

  public void move()
  {
  }

  public void draw(Graphics2D g, double scale)
  {
    int x = (int)((this.location.x - this.r / 2) * scale);
    int y = (int)((this.location.y - this.r / 2) * scale);
    int w = (int)(this.r * scale);

    g.setColor(Color.RED);
    g.fillOval(x, y, w, w);
  }
}