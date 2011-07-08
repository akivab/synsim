import java.util.Random;
import java.util.List;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Graphics2D;
public class Okt3 extends Molecule{
	private int border;
	private double genesisRate; // speckles/frame generated
	
	public Okt3(Point location, int r, int border, int genesisRate){
		initialize(location, r, border, genesisRate);
	}
	
	public Okt3(Dimension board, boolean left){
		Point location = new Point((int)((left?1:3) * board.getWidth() / 4), (int)(board.getHeight() / 2));
		int r = (int) Math.min(board.getWidth() / 20, board.getHeight() / 20);
		int border = r;
		double genesisRate = 1;
		this.dim = board;
		initialize(location, r, border, genesisRate);
	}
	
	public void initialize(Point location, int r, int border, double genesisRate){
		this.location = location;
		this.r = r;
		this.border = border;
		this.genesisRate = genesisRate;
		this.t = type.OKT3;
	}
	
	@Override
	public void update(List<Molecule> allMolecules) {
		Random rand = new Random();

		if(rand.nextDouble() < genesisRate){
			double randNum = rand.nextDouble();
			int x = (int) (Math.cos(randNum * Math.PI * 2) * border);
			int y = (int) (Math.sin(randNum * Math.PI * 2) * border);
			x += location.x;
			y += location.y;
			allMolecules.add(new Actin(dim, new Point(x,y)));
		}
	}

	@Override
	public void move() {
		return;
	}

	@Override
	public void draw(Graphics2D g, double scale) {
		int x = (int) ((location.x - r/2) * scale);
		int y = (int) ((location.y - r/2) * scale);
		int w = (int) (r * scale);
		
		g.setColor(Color.RED);
		g.fillOval(x, y, w, w);
	}
}
