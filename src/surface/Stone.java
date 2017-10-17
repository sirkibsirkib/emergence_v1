package surface;

import abstracts.Solid;
import abstracts.Thing;
import categories.PickUpAble;

public class Stone extends Solid implements PickUpAble{	
	public Stone(double x, double y, double spriteScale){
		super(x, y, spriteScale);
		setBoundingBox(4, 6, 12, 14);
	}
	
	public Stone(double x, double y){
		this(x, y, 1);
	}
	
	public int getSpriteSheetX() {
		return 0;
	}
	
	public int getSpriteSheetY() {
		return 1;
	}

	public Thing copy() {
		return new Stone(x, y);
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
