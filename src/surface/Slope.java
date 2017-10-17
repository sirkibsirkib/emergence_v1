package surface;

import abstracts.Solid;
import abstracts.Thing;

public class Slope extends Thing{
	int spriteX;
	int spriteY;
	
	public Slope(double x, double y, int spriteX, int spriteY) {
		super(x, y);
		this.spriteX = spriteX;
		this.spriteY = spriteY;
		boundBySprite();
	}

	private void boundBySprite() {
		if(spriteX == 5 && spriteY == 0) //top
			setYBoundingBox(2, 7);
		if((spriteX == 4 || spriteX == 5) && spriteY == 4) //topcorners
			setYBoundingBox(0, 7);
	}

	public int getSpriteSheetX() {
		return spriteX;
	}
	
	public int getSpriteSheetY() {
		return spriteY;
	}

	@Override
	public Thing copy() {
		return new Slope(x, y, spriteX, spriteY);
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
