package oldsynsim;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

public class Actin extends Molecule
{
  private boolean isStationary = false;

  public Actin(Point location) { this.r = 5;
    this.location = location;
    this.t = type.ACTIN; }

  public void update(List<Molecule> allMolecules)
  {
    for (Molecule m : allMolecules) {
      if (isNear(m)) {
        updateVelocity(m);
      }
      if ((m.t == type.OKT3) && (m.isNear(this)))
        this.isStationary = true;
    }
  }

  private void updateVelocity(Molecule m)
  {
    double dx = m.location.x - this.location.x;
    double dy = m.location.y - this.location.y;
    double d = m.location.distance(this.location);
    double k = 1.0D;

    if (m.t == type.ACTIN) k = -5.0D;
    else if (m.t == type.OKT3) k = 0.0D;
    Point tmp86_83 = this.velocity; tmp86_83.x = (int)(tmp86_83.x + k * dx / d);
    Point tmp107_104 = this.velocity; tmp107_104.y = (int)(tmp107_104.y + k * dy / d);
  }

  public void move()
  {
    if (Math.abs(this.velocity.x) > this.r * 2) this.velocity.x = (this.velocity.x / Math.abs(this.velocity.x) * this.r * 2);
    if (Math.abs(this.velocity.y) > this.r * 2) this.velocity.y = (this.velocity.y / Math.abs(this.velocity.y) * this.r * 2);

    this.location.x += this.velocity.x;
    this.location.y += this.velocity.y;
  }

  public void draw(Graphics2D g, double scale)
  {
    int x = (int)((this.location.x - this.r / 2) * scale);
    int y = (int)((this.location.y - this.r / 2) * scale);
    int w = (int)(this.r * scale);

    g.setColor(Color.GREEN);
    g.drawOval(x, y, w, w);
  }
}