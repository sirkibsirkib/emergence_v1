package game;

import java.util.ArrayList;
import java.util.List;

public class Planet {
	private int type, seed, size, defaultHue;
	private List<Double> noiseScales;
	
	Planet(int type, int seed, int size){
		noiseScales = new ArrayList<>();
		this.type = type;
		this.seed = seed;
		this.size = size;
		defaultHue = seed & (0xffffff);
	}

	
	/*
	 * TODO: multiple noise combination functions
	 * descending: combined with ratio ...4,2,1
	 * equivalent: combined with 1,1,1....
	 * proportional: combined with ratios equivalent to their size (as it is now)
	 */
	
	public Feature noiseFeature(int x, int y) {
		double choice = getChoice(0, x, y, 11, 2);
		
		if(choice > .85){
			return new Stone(x, y);
		}
		if(choice > .55){
			return new Shrub(x, y, getChoice(0, x, y, 9, 2));
		}
		return null;
	}
	
	public int soilColorAt(int x, int y){
		return defaultHue;
	}


	private double getChoice(int offset, int x, int y,int... noiseScaleIndices){
		double choice = 0;
		double multiplicity = 0;
		for(int i = 0; i < noiseScaleIndices.length; i++){
			double noiseScale = getNoiseScale(noiseScaleIndices[i]);
			multiplicity += noiseScale + 5;
			choice += (ImprovedNoise.getNoise(seed + offset,
					1.0*x/noiseScale,
					1.0*y/noiseScale))*noiseScale;
		}
		return choice / multiplicity;
	}
	
	

	private double getNoiseScale(int i) {
		while(noiseScales.size() < i+1){
			int size = noiseScales.size();
			if(size == 0)
				noiseScales.add(((seed & 0b111) + 6)/10D);
			else{
				double factorIncrease = 1.4 + ((seed & 0xff << 3) % 5)/10D;
				noiseScales.add(noiseScales.get(noiseScales.size()-1) * factorIncrease + .3);
			}
		}
		return noiseScales.get(i);
	}

	public int defaultHue() {
		return defaultHue;
	}


	public Feature noiseHeightFeature(int x, int y) {
		int myHeight = getNoiseHeight(x, y);
		if(myHeight < getNoiseHeight(x, y-1))
			return new DownCliff(x, y);
		if(myHeight > getNoiseHeight(x, y-1))
			return new UpCliff(x, y);
		return null;
	}
	
	private int getNoiseHeight(int x, int y){
		int height =  (int)(8* getChoice(0, x, y, 11, 2));
		System.out.println(height);
		return height;
	}


	public int getSize() {
		return size;
	}
}
