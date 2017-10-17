package game;

public class Stone extends Feature{	
	Stone(int x, int y, double spriteScale){
		super(x, y, spriteScale);
	}
	
	Stone(int x, int y){
		super(x, y, 1);
	}
	
	public int getSpriteSheetX() {
		return 0;
	}
	
	public int getSpriteSheetY() {
		return 1;
	}
	
	public String getName() {
		return "Shrub";
	}
}
