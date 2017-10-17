package game;

public class DownCliff extends Feature{
	DownCliff(int x, int y, double SpriteScale) {
		super(x, y, SpriteScale);
	}
	
	DownCliff(int x, int y) {
		super(x, y, 1);
	}
	
	public int getSpriteSheetX() {
		return 5;
	}
	
	public int getSpriteSheetY() {
		return 2;
	}
	
	public String getName() {
		return "Cliff";
	}
}
