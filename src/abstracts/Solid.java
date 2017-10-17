package abstracts;


public abstract class Solid extends Thing {
	protected boolean isSolid;
	
	public Solid(double x, double y, double spriteScale){
		super(x, y, spriteScale);
		isSolid = true;
	}
	
	public Solid(double x, double y, boolean isSolid) {
		this(x, y, 1);
		this.isSolid = isSolid;
	}
	
	public Solid(double x, double y) {
		this(x, y, 1);
	}

	public boolean collidesWith(Solid o) {
		if(o.getX1() + o.getX() > x2 + x) return false;
		if(o.getX2() + o.getX() < x1 + x) return false;
		if(o.getY1() + o.getY() > y2 + y) return false;
		if(o.getY2() + o.getY() < y1 + y) return false;
		return true;
	}
	
	public boolean isSolid(){
		return isSolid;
	}
	
	public void setIsSolid(boolean isSolid){
		this.isSolid = isSolid;
	}
	
	//abstracts
	
	public abstract void checkForSpecialCollision(Solid other);

}
