package surface;

import general.Colour;
import general.F;
import general.Game;
import general.S;
import ship.Human;
import abstracts.Environment;
import abstracts.Thing;

public class World extends Environment{
	public static final int CHUNK_SIZE = 320;
	private int size;
	Colour soil;
	private GeneratorWorld gen;
	private double chunkX, chunkY, xInSpace, yInSpace;
	
	public World(GeneratorWorld gen, int size, Colour soil, double xInSpace, double yInSpace, Lander lander){
		super(gen.getChangeLog());
		this.size = size;
		this.soil = soil;
		this.gen = gen;
		chunkX = Double.MAX_VALUE;
		chunkY = Double.MAX_VALUE;
		this.xInSpace = xInSpace;
		this.yInSpace = yInSpace;
		v = new View(lander);
		checkNewChunk();
		newLander(lander);
	}
	
	public boolean isLocatedInSpaceAt(double x, double y){
		return xInSpace == x && yInSpace == y;
	}
	
	//FIX
	public void emergeFromLander(Human h, Lander l){
		h.goTo(l);
		addCrewMember(h);
		S.input.stopMovementForTicks(20);
		v = new View(h);
	}
	public void enterLander(Human h, Lander l){
		removeCrewmember(h);
		l.getShip().enterAirlock(h);
		S.input.stopMovementForTicks(20);
		v = new View(l);
	}
	
	public void newLander(Lander lander){
		grid.addSolid(lander);
		v = new View(lander);
	}
	
	public Colour getSoil() {
		return soil;
	}

	public int getSize() {
		return size;
	}
	
	private void populateGridChunk(double x2, double y2){
		int gridSize = 16;
		int x = F.snap(x2, gridSize);
		int y = F.snap(y2, gridSize);
		
		int[][] heights = gen.genHeights(x, y);
		
		int i2 = 0;
		for(int i = x-CHUNK_SIZE; i < x+CHUNK_SIZE; i += gridSize){
			int j2 = 0;
			for(int j = y-CHUNK_SIZE; j < y+CHUNK_SIZE; j += gridSize){
				Thing generated = gen.genThingAt(i, j, heights, i2, j2);
				if(generated != null)
					grid.add(generated);
				j2++;
			}
			i2++;
		}
	}

	public void genChunkAt(Thing f) {
		populateGridChunk(f.getX(), f.getY());
		for(int i = 0; i < S.fleet.size(); i++){
			Lander next = S.fleet.get(i).getLander();
			if(next != null && S.fleet.get(i).getLocation() == this)
				grid.add(next);
		}
		
//		for(int i = 0; i < S.crew.size(); i++){
//			Human next = S.crew.get(i);
//			if(next.getLocation() == this)
//				grid.add(next);
//		}
	}

	public void render() {
		for(int i = 0; i < grid.size(); i++){
			if(grid.get(i) != null) //FIX THIS
				grid.get(i).draw(v, true);
		}
		for(int i = 0; i < crew.size(); i++)
			crew.get(i).draw(v, true);
		for(int i = 0; i < S.fleet.size(); i++){
			Lander next = S.fleet.get(i).getLander();
			if(next != null && S.fleet.get(i).getLocation() == this)
				next.draw(v, true);
		}
			
	}

	public void checkNewChunk() {
		if(F.distance(v.getX(), v.getY(), chunkX, chunkY) > CHUNK_SIZE/2){
			grid.clear();
			genChunkAt(v.getFollowing());
			chunkX = v.getX();
			chunkY = v.getY();
		}
	}

	public Colour getSoilAt(double d, double e) {
		if(gen.getNoiseHeight(d, e) <= gen.getSeaLevel())
			return new Colour(0, 50, 120);
		return getSoil();
	}
}
