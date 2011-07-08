import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

public class Actin extends Molecule{
	public Actin(Dimension dim, Point p){
		this(p);
		this.dim = dim;
	}
	public Actin(Point location){
		this.r = 5;
		this.location = location;
		this.t = type.ACTIN;
	}
	@Override
	public void update(List<Molecule> allMolecules) {
		this.toRemove = outOfFrame();
		for(Molecule m : allMolecules){
			if(this.isNear(m)){
				this.updateVelocity(m);
			}
		}
		
	}

	private void updateVelocity(Molecule m){
		double dx = m.location.x - location.x;
		double dy = m.location.y - location.y;
		double d = m.location.distance(location);
		double k = 1;
		
		if(m.t == type.ACTIN) k = -5;
		else if(m.t == type.OKT3) k = 0;
		
		velocity.x += k * dx / d;
		velocity.y += k * dy / d;
		
	}
	
	@Override
	public void move() {
		if(Math.abs(velocity.x) > r * 2) velocity.x = velocity.x/Math.abs(velocity.x) * r * 2;
		if(Math.abs(velocity.y) > r * 2) velocity.y = velocity.y/Math.abs(velocity.y) * r * 2;
		location.x += velocity.x;
		location.y += velocity.y;
		}

	@Override
	public void draw(Graphics2D g, double scale) {
		int x = (int) ((location.x - r/2) * scale);
		int y = (int) ((location.y - r/2) * scale);
		int w = (int) (r * scale);
		
		g.setColor(Color.GREEN);
		g.drawOval(x, y, w, w);
	}

}