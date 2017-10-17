package space;

import general.Colour;
import general.NameGen;
import general.Noise;
import gfx.Screen;
import surface.View;
import abstracts.StellarBody;

public class Star extends StellarBody{	
	public static final int MAX_RADIUS = (int) (Math.pow(46, 1.5) + 80);
	private double temperature;
	private Colour plasma;
	private String name;
	private int seed;
	
	public Star(double x, double y, int radius, double temperature) {
		super(x, y, radius);
		seed = (int) (Math.cbrt(x) + y + radius);
		name = NameGen.get(seed);
		plasma = calcPlasmaColor(temperature);
	}
	
	private Colour calcPlasmaColor(double temperature) {
		int r = temperature < 29_000 ? 0xff : 00;
		int g = temperature > 12_000 ? 0xff : 00;
		int b = temperature > 17_000 ? 0xff : 00;
		return new Colour(r, g, b);
	}
	
	public Colour getPlasmaColour(){
		return plasma;
	}

	public String getName(){
		return name;
	}

	public int getSpriteSheetX() {
		return 0;
	}

	public int getSpriteSheetY() {
		return 0;
	}

	public Star copy() {
		return new Star(x, y, radius, temperature);
	}

	public Colour getColourAt(double x, double y) {
		return plasma;
	}

	public boolean uniformColour() {
		return true;
	}
	
	@Override
	public void draw(View v, boolean flatProjection){
		Screen.drawProjected(this, v);
	}

	public double getDrawRotation() {
		return 0;
	}

	public double getDrawScale() {
		return 1;
	}

	public boolean clearAt(int colX, int colY) {
		return false;
	}
}
