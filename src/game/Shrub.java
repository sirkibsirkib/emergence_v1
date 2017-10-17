package game;

public class Shrub extends Feature{
	Shrub(int x, int y, double SpriteScale) {
		super(x, y, SpriteScale);
	}
	
	Shrub(int x, int y) {
		super(x, y, 1);
	}
	
	public int getSpriteSheetX() {
		return 2;
	}
	
	public int getSpriteSheetY() {
		return 2;
	}
	
	public String getName() {
		return "Shrub";
	}
}
