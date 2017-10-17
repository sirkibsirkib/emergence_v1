package space;

import general.ChangeLog;
import general.F;
import general.Grid;
import general.S;
import gfx.StringWriter;
import abstracts.Environment;
import abstracts.StellarBody;
import abstracts.Thing;

public class Galaxy extends Environment{
	private static final int CHUNK_SIZE = GeneratorGal.observableUniverse/12;
	private static final int SEED = 173983;
	private GeneratorGal gen;
	private double chunkX, chunkY;
	
	public Galaxy(ChangeLog cl){
		super(cl);
		grid = new Grid();
		gen = new GeneratorGal(SEED);
		chunkX = Double.MAX_VALUE;
		chunkY = Double.MAX_VALUE;
	}
	
	public void render() {
		if(S.vehicle() != null)
			S.vehicle().setOrbit(null, null);
		for(int i = 0; i < grid.size(); i++){
			if(grid.get(i) instanceof Star || grid.get(i) instanceof SpacePlanet)
				grid.get(i).draw(v, false);
		}
		for(int i = 0; i < grid.size(); i++){
			if(grid.get(i) instanceof SpaceShip)
				grid.get(i).draw(v, false);
		}
	}

	private void sortGridByDistance(Thing origin) {
		for(int i = 0; i < grid.size()-1; i++){
			for(int j = 0; j < grid.size()-1-i; j++){
				if(grid.get(j).distanceTo(origin) < grid.get(j+1).distanceTo(origin))
					grid.swapWithNext(j);
			}
		}
	}
	
	public void jumpToAPlanet(){
		SpaceShip t = S.vehicle().getMrShip();
		SpacePlanet x;
		do{
			t.goTo(F.rdg(200_000_000)-100_000_000, F.rdg(200_000_000)-100_000_000);
			checkNewChunk();
			x = getRandomPlanet();
		}while(x == null);
		t.goTo(x);
	}
	
	private SpacePlanet getRandomPlanet(){
		int offset = F.rng(grid.size()-1);
		for(int i = 0; i< grid.size(); i++){
			Thing next = grid.get((i + offset) % grid.size());
			if(next instanceof SpacePlanet)
				return ((SpacePlanet) next);
		}
		return null;
	}
	
	public void genChunkAt(Thing t){
		double snapX = F.snap(t.getX(), 1000);
		double snapY = F.snap(t.getY(), 1000);
		gen.genChunkAt(grid, snapX, snapY);
	}

	public SpacePlanet getPlanetInRange(Thing s, int relativeRange) {
		SpacePlanet found = null;
		for(int i = 0; i < grid.size(); i++){
			Thing next = grid.get(i);
			if(next instanceof SpacePlanet){
				found = (SpacePlanet) next;
				if(F.distance(s.getX(), s.getY(), found.getX(), found.getY()) < found.getRadius()*relativeRange){
					return found;
				}
			}
		}
		return null;
	}

	public void tryLand(SpaceShip ship) {
		SpacePlanet landOn = getPlanetInRange(ship, 10);
		if(landOn != null && ship.getSpeed() <= 2)
			if(landOn.tryLand(ship))
				grid.remove(ship);
	}
	
	public void advertiseLandingOption(SpaceShip ship){
		SpacePlanet near = getPlanetInRange(ship, 90);
		StellarBody landOn = ship.getShip().getOrbiting();
		if(near != null && landOn == null)
			StringWriter.writeStringToScreen("grav", 100, 5, 0xff22ff);
		if(landOn != null)
			StringWriter.writeStringToScreen(near.getName(), 100, 5, 0xdd22ff);
	}

	public void checkNewChunk() {
		if(F.distance(v.getX(), v.getY(), chunkX, chunkY) > CHUNK_SIZE/2 || S.game.ticks % 900 == 0){
			grid.clear();
			genChunkAt(v.getFollowing());
			chunkX = v.getX();
			chunkY = v.getY();
			
			for(int i = 0; i < S.fleet.size(); i++){
				grid.add(S.fleet.get(i).getMrShip());
			}
		}
		sortGridByDistance(v.getFollowing());
	}

	public void tick() {
		for(int i = 0; i < S.fleet.size(); i++){
			if(S.location() == null){
				SpaceShip ship = S.fleet.get(i).getMrShip();
				ship.tick();
				SpacePlanet near = getPlanetInRange(ship, 90);
				if(ship.getSpeed() < 10){
					if(near != null)
						ship.fallToward(near);
					if(S.fleet.get(i).getLandingCoordinate() != null)
						ship.noSlowerThan(.2);
				}
			}
		}
		if(S.game.ticks % 30 == 0)
			sortGridByDistance(v.getFollowing());
	}
}
