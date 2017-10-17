package space;

import general.ChangeLog;
import general.Colour;
import general.S;
import gfx.Screen;
import ship.Ship;
import surface.GeneratorWorld;
import surface.Lander;
import surface.View;
import surface.World;
import abstracts.Coordinate;
import abstracts.StellarBody;

public class SpacePlanet extends StellarBody{
	public static int WORLD_SCALE_UP = 16;
	private Star parentStar;
	private Colour soil;
	private String name;
	private int seed, starNumber;
	private GeneratorWorld gen;
	
	public SpacePlanet(double x, double y, int seed, int radius, Colour soil, Star parentStar, int starNumber) {
		super(x, y, radius);
		this.parentStar = parentStar;
		this.soil = soil;
		this.seed = seed;
		this.starNumber = starNumber;
		name = parentStar.getName() + ' ' + (char)('A' + starNumber);
		ChangeLog thisLog = S.logger.getLogAt('p', (int)x, (int)y);
		gen = new GeneratorWorld(seed, thisLog);
	}
	
	public String getName(){
		return name;
	}
	
	public Star getParentStar(){
		return parentStar;
	}

	public World generateWorld(Lander lander) {
		World preloaded = S.getWorldAlreadyLoaded(x, y);
		if(preloaded != null){
			preloaded.newLander(lander);
			return preloaded;
		}
		
		World thisWorld = new World(gen, (int)(radius*1000), soil, x, y, lander);
		return thisWorld;
	}

	public int getSpriteSheetX() {
		return 0;
	}

	public int getSpriteSheetY() {
		return 0;
	}

	public SpacePlanet copy() {
		return new SpacePlanet(x, y, seed, radius, soil, parentStar, starNumber);
	}
	
	public Colour getColourAt(double atX, double atY) {
		int h = gen.getNoiseHeight(atX, atY);
		if(h <= gen.getSeaLevel())
			return new Colour(0, 20, 100);
		return soil.darken((3 + h)/(gen.getHeightLevels() + 3D));
	}
	
	@Override
	public void draw(View v, boolean flatProjection){
		Screen.drawProjected(this, v);
	}
	
	public boolean uniformColour() {
		return false;
	}

	public double getDrawRotation() {
		return 0;
	}

	public double getDrawScale() {
		return 1;
	}

	public boolean tryLand(SpaceShip sShip) {
		Ship s = sShip.getShip();
		Coordinate lc = s.getLandingCoordinate();
		if(lc == null)
			return false;
		
		sShip.stop();
		int xLandingSpot = (int)lc.getX();
		int yLandingSpot = (int)lc.getY();
		World w = generateWorld(sShip.getShip().giveLander(new Lander(sShip.getShip(), xLandingSpot, yLandingSpot)));
		sShip.getShip().setLocation(w);
		sShip.stop();
		System.out.println("LAND AT " + xLandingSpot + " " + yLandingSpot);
		S.game.land(sShip.getShip(), w);
		return true;
	}

	public boolean clearAt(int atX, int atY) {
		int h = gen.getNoiseHeight(atX, atY);
		return !(h <= gen.getSeaLevel());
	}
}
