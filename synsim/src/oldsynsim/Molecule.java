package oldsynsim;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

public abstract class Molecule
  implements Cloneable
{
  public Point location;
  public Point velocity = new Point(0, 0);
  public int r;
  public type t;

  public abstract void update(List<Molecule> paramList);

  public boolean isNear(Molecule other)
  {
    if (this.t == type.OKT3) {
      return other.location.distance(this.location) < this.r / 2;
    }
    return other.location.distance(this.location) < this.r * 10;
  }
  public abstract void move();

  public String toString() { return this.t + " " + this.location;  }

  public abstract void draw(Graphics2D paramGraphics2D, double paramDouble);
}
