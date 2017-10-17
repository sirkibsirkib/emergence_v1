package surface;

import abstracts.Thing;

public class Chair extends Thing {

	public Chair(double x, double y) {
		super(x, y);
	}

	public int getSpriteSheetX() {
		return 2;
	}

	public int getSpriteSheetY() {
		return 4;
	}

	public Thing copy() {
		return new Chair(x, y);
	}

	public double getDrawRotation() {
		return 0;
	}

	public double getDrawScale() {
		return 1;
	}
}
