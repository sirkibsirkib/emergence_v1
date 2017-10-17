package abstracts;

import general.F;

public class Coordinate {
	protected double x;
	protected double y;
	
	public Coordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void goTo(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public void offsetX(double x){
		this.x += x;
	}
	
	public void offsetY(double y){
		this.y += y;
	}
	
	public double getX() {
		return x;
	}


	public void setX(double d) {
		this.x = d;
	}


	public double getY() {
		return y;
	}


	public void setY(double y) {
		this.y = y;
	}
	
	public double distanceTo(double x, double y) {
		return F.distance(this.x, this.y, x, y);
	}
	
	public double distanceTo(Coordinate o){
		return F.distance(x, y, o.getX(), o.getY());
	}
	
	public Coordinate getRelative(double direction, double distance){
		double cX = x + Math.cos(direction) * distance;
		double cY = y - Math.sin(direction) * distance;
		return new Coordinate(cX, cY);
	}
	
	public Coordinate getRelative(Coordinate other){
		double cX = x - other.getX();
		double cY = y - other.getY();
		return new Coordinate(cX, cY);
	}
}
