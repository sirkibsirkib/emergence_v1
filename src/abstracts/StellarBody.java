package abstracts;

import general.Colour;

public abstract class StellarBody extends Thing {
	protected int radius;
	
	public StellarBody(double x, double y, int radius) {
		super(x, y);
		this.radius = radius;
	}

	@Override
	public int getSpriteSheetX() {
		return 0;
	}

	@Override
	public int getSpriteSheetY() {
		return 0;
	}

	public abstract StellarBody copy();

	public int getRadius() {
		return radius;
	}
	
	public abstract Colour getColourAt(double x, double y);
	
	public abstract boolean uniformColour();

	public abstract boolean clearAt(int colX, int colY);
}
