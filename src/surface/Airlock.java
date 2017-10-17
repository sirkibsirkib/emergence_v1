package surface;

import abstracts.Coordinate;
import abstracts.Solid;
import abstracts.Thing;

public class Airlock extends Solid{

	public Airlock(double x, double y) {
		super(x, y);
	}

	public void goTo(Coordinate c) {
		x = c.getX();
		y = c.getY();
	}

	public int getSpriteSheetX() {
		return 1;
	}

	public int getSpriteSheetY() {
		return 4;
	}

	public Thing copy() {
		return new Airlock(x, y);
	}

	public void checkForSpecialCollision(Solid other){
		//do nothing
	}
	
	public double getDrawRotation() {
		return 0;
	}
	
	public double getDrawScale() {
		return 1;
	}
}
