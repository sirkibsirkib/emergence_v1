package surface;

import abstracts.Solid;
import abstracts.Thing;

public class Water extends Solid{

	public Water(double x, double y) {
		super(x, y);
	}

	@Override
	public void checkForSpecialCollision(Solid other) {
		//none
	}
	
	public int getSpriteSheetX() {
		return 5;
	}

	public int getSpriteSheetY() {
		return 1;
	}

	public Thing copy() {
		return new Water(x, y);
	}

	public double getDrawRotation() {
		return 0;
	}

	public double getDrawScale() {
		return 1;
	}
}
