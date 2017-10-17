package ship;

import general.ChangeLog;
import general.S;
import gfx.Screen;
import gfx.SpriteSheet;
import space.SpaceShip;
import surface.Airlock;
import surface.Chair;
import surface.InvisibleWall;
import surface.Lander;
import surface.View;
import surface.World;
import abstracts.Coordinate;
import abstracts.Environment;
import abstracts.StellarBody;

public class Ship extends Environment{
	private double fuel;
	private Airlock al;
	private double SCALE = 16;
	private World location;
	private StellarBody orbiting;
	private SpaceShip mrShip;
	private Lander lander;
	private Coordinate landingCoordinate;
	
	public Ship(double x, double y, ChangeLog cl){
		super(cl);
		mrShip = new SpaceShip(this, x, y);
		location = null;
	}
	
	public void setOrbit(Coordinate c, StellarBody w){
		landingCoordinate = c;
		orbiting = w;
	}
	
	public Coordinate getLandingCoordinate(){
		return landingCoordinate;
	}
	
	public StellarBody getOrbiting(){
		return orbiting;
	}
	
	public void init(){
		fuel = 500;
		v = new View(S.selected());
		al = new Airlock(-6*SCALE, 2*SCALE);
		grid.add(al);
		grid.add(new Chair(5*SCALE, -1*SCALE));
		invisibleWallsFromSprite();
	}
	
	public void setLocation(World l){
		for(int i = 0; i < crew.size(); i++)
			crew.get(i).setLocation(l);
		location = l;
	}
	
	public World getLocation(){
		return location;
	}
	
	private void invisibleWallsFromSprite() {
		int spriteX = 0;
		int spriteY = 9<<4;
		for(int i = 0; i < 16; i++){
			for(int j = 0; j < 16; j++){
				int col = S.forest.tilePixels[spriteX + i + S.forest.width*(spriteY + j)] & 0x00ffffff;
				if(col == 0x2e262e || col == SpriteSheet.ALPHA_01 || col == SpriteSheet.ALPHA_02){
					double cX = (i-8)*SCALE;
					double cY = (j-8)*SCALE;
					grid.add(new InvisibleWall(cX, cY, SCALE));
				}
					
			}
		}
	}

	public void draw() {
		Screen.drawScaledSprite(S.forest, (int)(S.WIDTH/2-v.getX() - SCALE*8), (int)(S.HEIGHT/2-v.getY() - SCALE*8), 0, 9<<4, SCALE);
		for(int i = 0; i < crew.size(); i++)
			crew.get(i).draw(v, true);
		for(int i = 0; i < grid.size(); i++)
			grid.get(i).draw(v, true);
	}

	public double getFuel() {
		return fuel;
	}
	
	public void setFuel(double fuel){
		this.fuel = fuel; 
	}

	public void decreaseFuel(double d) {
		fuel = fuel > d ? fuel - d : 0;
	}

	public Airlock getAirlock() {
		return al;
	}
	
	public SpaceShip getMrShip(){
		return mrShip;
	}
	
	public Lander getLander(){
		return lander;
	}

	public Lander giveLander(Lander lander) {
		this.lander = lander;
		return lander;
	}

	public void leaveAirlock(Human h) {
		if(location != null){
			h.setVehicle(null);
			location.emergeFromLander(h, lander);
			removeCrewmember(h);
		}
	}

	public void enterAirlock(Human h) {
		h.goTo(getAirlock());
		addCrewMember(h);
		h.setVehicle(this);
		setView(h);
	}
}
