package surface;

import ship.Ship;
import abstracts.Solid;
import abstracts.Thing;

public class Lander extends Solid{
	private Ship ship;

	public Lander(Ship ship, double x, double y) {
		super(x, y);
		this.ship = ship;
		setBoundingBox(4, 8, 12, 14);
	}
	
	public Ship getShip(){
		return ship;
	}

	public int getSpriteSheetX() {
		return 0;
	}

	public int getSpriteSheetY() {
		return 4;
	}

	public Thing copy() {
		return new Lander(ship, x, y);
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
