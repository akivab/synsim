import java.awt.Dimension;
import java.util.logging.Logger;

public class Okt3 extends Molecule{
	public Okt3(float x, float y, int w, int l, Logger logger){
		super(new FloatPoint(x,y), new Dimension(w,l), logger);
		this.t = Type.OKT3;
	}
}