package abstracts;

import general.F;
import gfx.Screen;
import surface.View;

public abstract class Thing extends Coordinate{
	protected double spriteScale;
	protected int x1, y1, x2, y2;	//for bounding box
	
	protected Thing(double x, double y, double spriteScale){
		super(x, y);
		this.spriteScale = spriteScale;
		x1 = y1 = 0;
		x2 = y2 = 16;
	}
	
	public Thing(double x, double y){
		this(x, y, 1);
	}
	
	public void setBoundingBox(int x1, int y1, int x2, int y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public void setYBoundingBox(int y1, int y2){
		this.y1 = y1;
		this.y2 = y2;
	}
	
	public void setXBoundingBox(int x1, int x2){
		this.x1 = x1;
		this.x2 = x2;
	}
	
	public int getX1() {
		return x1;
	}

	public int getX2() {
		return x2;
	}

	public int getY1() {
		return y1;
	}

	public int getY2() {
		return y2;
	}
	
	public int xCenter(){
		return (x1 + x2) / 2;
	}
	
	public int yCenter(){
		return (y1 + y2) / 2;
	}
	
	public double getSpriteScale(){
		return spriteScale;
	}
	
	public void setSpriteScale(double spriteScale){
		this.spriteScale = spriteScale;
	}
	
	public abstract int getSpriteSheetX();
	public abstract int getSpriteSheetY();

	public boolean collisionWith(Thing moving, int xOth, int yOth) {
		if(moving == this)
			return false;
		return x+x1 < xOth && xOth <= x+x2 &&
				y+y1 < yOth && yOth <= y+y2;
	}
	
	public boolean collisionWith(Thing moving) {
		if(moving == this)
			return false;
		if(moving.x+moving.x1 >= x+x2)	//its too far right
			return false;
		if(moving.y+moving.y1 >= y+y2)	//its too far down
			return false;
		if(moving.x+moving.x2 < x+x1)	//its too far left
			return false;
		if(moving.y+moving.y2 < y+y1)	//its too far right
			return false;
		return true;
	}

	public String getName() {
		String raw = getClass().getName();
		int dot = raw.lastIndexOf('.');
		return raw.substring(dot+1);
	}
	
	public void goTo(Thing t) {
		goTo(t.getX(), t.getY());
	}

	public void draw(View v, boolean flatProjection){
		if(flatProjection)
			Screen.drawFlat(this, v);
		else
			Screen.drawProjected(this, v, getDrawScale(), getDrawRotation());
	}
	
	public double distanceTo(Thing o){
		return F.distance(x+xCenter(), y+yCenter(), o.getX()+o.xCenter(), o.getY()+o.yCenter());
	}
	
	public abstract Thing copy();
	public abstract double getDrawRotation();
	public abstract double getDrawScale();
}
