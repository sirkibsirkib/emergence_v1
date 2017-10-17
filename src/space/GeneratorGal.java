package space;

import general.Colour;
import general.F;
import general.Grid;
import general.Noise;
import general.S;

public class GeneratorGal {
	public static final int observableUniverse = 800_000,
							planetsOnlyWithin = 90_000, 
							solarGaps = 23,
							solarGapWidth = observableUniverse / solarGaps,
							planetaryMaxDistance = solarGapWidth / 3,
							maxPlanets = 12,
							minusPlanets = 2;
	private int seed;
	
	public GeneratorGal(int seed){
		this.seed = seed;
	}
	
	public void genChunkAt(Grid g, double x, double y) {
		double snapX = F.snap(x, solarGapWidth);
		double snapY = F.snap(y, solarGapWidth);
		for(double i = snapX-(solarGaps*solarGapWidth); i < snapX+(solarGaps*solarGapWidth); i+= solarGapWidth){
			for(double j = snapY-(solarGaps*solarGapWidth); j < snapY+(solarGaps*solarGapWidth); j+= solarGapWidth){
				boolean closeEnoughForPlanets = F.distance(x, y, i, j) < planetsOnlyWithin;
				genSolarSystemAt(g, i, j, closeEnoughForPlanets);
			}
		}
	}
	
	private void genSolarSystemAt(Grid g, double x, double y, boolean genPlanetsToo) {
		if(Noise.getNoise(seed*2, x*2.433454, y*4.24243) + Noise.getNoise(seed*4, y*7.3454, x*5.123) < 1.4)
			return;
		Star newStar = addStarAt(g, x, y);
		if(!genPlanetsToo || newStar == null)
			return;
		
		double thisStarRelRadius = 1.0 * newStar.getRadius()/Star.MAX_RADIUS;
		int numPlanets = (int) (Noise.getNoise(seed*5, x*2.233454, y*4.1143)*thisStarRelRadius* (maxPlanets+minusPlanets)) - minusPlanets;
		for(int i = 0; i < numPlanets; i++){
			double distance = planetaryMaxDistance * (.5*Noise.getNoise(seed*2, x*1.454+i, y*4.1143) + .5);
			double angle = Noise.getNoise((seed + i)*3, (x+i)*4.254, y*5.13) * Math.PI*2 + 0.0001*S.game.ticks/distance;
			int pSeed = (int) (Math.cbrt(x) + ((int)(5*y)<<i));
			double pX = x + Math.cos(angle) * distance;
			double pY = y - Math.sin(angle) * distance;
//			if(!g.thingWithin(new Coordinate(pX, pY), 900))
				addPlanetAt(g, pX, pY, pSeed, newStar, i);
		}
	}
	
	private boolean addPlanetAt(Grid g, double x, double y, int seed, Star parentStar, int starNumber) {
		if(g.find(x, y) != null)
			return false;
		System.out.println(parentStar.getName() + ' ' + (char)(starNumber + 'A'));
		int size = (int) (Noise.getNoise(seed/1, seed*1.12, seed*.132)*24) + 7;
		Colour soil = earthColorGen(seed, seed<<3, seed<<4);
		g.add(new SpacePlanet(x, y, seed, size, soil, parentStar, starNumber));
		return true;
	}

	private Star addStarAt(Grid g, double x, double y) {
		if(g.find(x, y) != null)
			return null;
		int size = (int) (Math.pow(Noise.getNoise(seed/3, x, y)*46, 1.5) + 80);
		double temperature = (int) (Noise.getNoise(seed/3, x, y)*32_000) + 3_000;
		Star newStar = new Star(x, y, size, temperature);
		g.add(newStar);
		return newStar;
	}
	
	private Colour earthColorGen(int seed, double x, double y){
		int r = (int) (Noise.getNoise((int) (seed*.734), x, y) * 255);
		int g = (int) (Noise.getNoise((int) (seed*2.223443), x, y) * 190);
		int b = (int) (Noise.getNoise((int) (seed*3.24234), x, y) * 130);
		return new Colour(r, g, b);
	}
}
