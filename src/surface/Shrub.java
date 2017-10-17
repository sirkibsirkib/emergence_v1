package surface;

import abstracts.Thing;
import categories.PickUpAble;

public class Shrub extends Thing implements PickUpAble{
	public Shrub(double x, double y, double SpriteScale) {
		super(x, y, SpriteScale);
		setBoundingBox(4, 8, 12, 14);
	}
	
	Shrub(double x, double y) {
		this(x, y, 1);
	}
	
	public int getSpriteSheetX() {
		return 2;
	}
	
	public int getSpriteSheetY() {
		return 2;
	}

	public Thing copy() {
		return new Shrub(x, y);
	}
	
	public double getDrawRotation() {
		return 0;
	}
	
	public double getDrawScale() {
		return 1;
	}
}
