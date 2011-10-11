package oldsynsim;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

public class Myosin extends Molecule
{
  public Myosin(Point location)
  {
    this.location = location;
    this.r = 5;
    this.t = type.MYOSIN;
  }

  public void update(List<Molecule> allMolecules) {
    for (Molecule m : allMolecules)
      if (isNear(m))
        updateVelocity(m);
  }

  private void updateVelocity(Molecule m)
  {
    double dx = m.location.x - this.location.x;
    double dy = m.location.y - this.location.y;
    double d = m.location.distance(this.location) / 2.0D;
    double k = 1.0D;

    if (m.t != type.ACTIN) k = -0.5D;
    Point tmp74_71 = this.velocity; tmp74_71.x = (int)(tmp74_71.x + k * dx / d);
    Point tmp95_92 = this.velocity; tmp95_92.y = (int)(tmp95_92.y + k * dy / d);
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

    g.setColor(Color.BLUE);
    g.drawOval(x, y, w, w);
  }
}