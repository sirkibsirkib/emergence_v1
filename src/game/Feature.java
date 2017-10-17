package game;

public abstract class Feature{
	protected int x, y;
	protected double spriteScale = 1;
	
	Feature(int x, int y, double spriteScale){
		this.x = x;
		this.y = y;
		this.spriteScale = spriteScale;
	}
	
	public void goTo(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}


	Feature(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public double getSpriteScale(){
		return spriteScale;
	}
	
	public void setSpriteScale(double spriteScale){
		this.spriteScale = spriteScale;
	}
	
	public abstract int getSpriteSheetX();
	public abstract int getSpriteSheetY();
	public abstract String getName();
}
