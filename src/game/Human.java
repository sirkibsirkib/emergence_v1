package game;

public class Human extends Feature{
	private int SPEED = 3;
	
	Human(int x, int y, double SpriteScale) {
		super(x, y, SpriteScale);
	}
	
	Human(int x, int y) {
		super(x, y, 1);
	}

	public int getSpriteSheetX() {
		return 0;
	}

	public int getSpriteSheetY() {
		return 2;
	}

	public void up() {
		y -= SPEED;
	}
	
	public void down() {
		y += SPEED;
	}
	
	public void left() {
		x -= SPEED;
	}
	
	public void right() {
		x += SPEED;
	}

	public String getName() {
		return "Human";
	}

	public void setSpriteScale(double spriteScale) {
		this.spriteScale = spriteScale;
	}
}
