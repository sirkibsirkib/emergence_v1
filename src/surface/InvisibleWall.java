package surface;

import abstracts.Solid;
import abstracts.Thing;

public class InvisibleWall extends Solid{
	private double scale;
	
	public InvisibleWall(double x, double y, double scale) {
		super(x, y);
		setBoundingBox(0, 0, (int)(scale), (int)(scale));
	}

	public void checkForSpecialCollision(Solid o) {
		//donothing
	}

	public int getSpriteSheetX() {
		return 0;
	}

	public int getSpriteSheetY() {
		return 0;
	}

	public Thing copy() {
		return new InvisibleWall(x, y, scale);
	}
	
	public void draw(View v, boolean flatProjection){
		//do nothing
	}
	
	public double getDrawRotation() {
		return 0;
	}
	
	public double getDrawScale() {
		return 1;
	}
}
