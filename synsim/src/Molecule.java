import java.awt.Dimension;
import java.awt.Point;
import java.util.List;
import java.awt.Graphics2D;
public abstract class Molecule implements Cloneable{
	public Point location;
	public Point velocity = new Point(0,0);
	public int r;
	public Dimension dim;
	public type t;
	public boolean toRemove;

	public abstract void update(List<Molecule> allMolecules);
	public boolean isNear(Molecule other){
		if(t == type.OKT3)
			return other.location.distance(this.location) < this.r / 2;
		else
			return other.location.distance(this.location) < this.r * 10;
	}
	public boolean outOfFrame(){
		return (location.x  < 0 || location.x > dim.getWidth() ||
				location.y < 0 || location.y > dim.getHeight());
	}
	public abstract void move();
	public String toString(){
		return "" + t + location;
	}
	public abstract void draw(Graphics2D g, double scale);
}

enum type{
	OKT3,
 	ACTIN,
	MYOSIN;
}
