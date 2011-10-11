public class FloatPoint implements Cloneable{
	float x = 0;
	float y = 0;
	public String toString(){
		return "("+x+", "+y+")";
	}
	public float mag(){ 
		return (float) Math.sqrt(x*x + y*y);
	}
	public float distance(FloatPoint other){
		return (float) Math.sqrt(Math.pow(other.x-x,2)+Math.pow(other.y-y,2));
	}
	public FloatPoint(){}
	public FloatPoint(float x, float y){
		this.x = x;
		this.y = y;
	}
	public FloatPoint clone(){
		return new FloatPoint(this.x, this.y);
	}
}