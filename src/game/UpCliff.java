package game;

public class UpCliff extends Feature{
	UpCliff(int x, int y, double SpriteScale) {
		super(x, y, SpriteScale);
	}
	
	UpCliff(int x, int y) {
		super(x, y, 1);
	}
	
	public int getSpriteSheetX() {
		return 5;
	}
	
	public int getSpriteSheetY() {
		return 0;
	}
	
	public String getName() {
		return "Cliff";
	}
}
