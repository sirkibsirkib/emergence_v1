package surface;

import abstracts.Thing;
import general.ChangeLog;
import general.F;
import general.Noise;

public class GeneratorWorld {
	private int seed, heightLevels, seaLevel;
	private ChangeLog cl;
	private double heightScale,	//scales up space between cliffs
	heightPower; // >1 heights are accentuated. <1 valleys are accentuated
	
	public GeneratorWorld(int seed, ChangeLog cl){
		this.seed = seed;
		heightScale = F.mod(seed, 12)+ 8;
		heightPower = F.mod(seed >> 4, 6)/10D+ .8;
		heightLevels = 6;
		seaLevel = F.mod(seed >> 2, 12) - 8;
		this.cl = cl;
	}
	
	public Thing genThingAt(double x, double y, int[][] heights, int hx, int hy) {
		if(heights[hx][hy] <= seaLevel)
			return new Water(x, y);
		
		Thing cliff = genCliffAt(x, y, heights, hx, hy);
		if(cliff != null)
			return cliff;
		
		
		
		if(cl.getDataAt(x, y) == 0) return null;
		
		double n12 = Noise.getNoise(12, x, y);
		if(heights[hx][hy] < n12*5 && heights[hx][hy] > n12*5)
			return new Stone(x, y);
		
		if(heights[hx][hy] > Noise.getNoise(11, x, y)*11)
			return new Shrub(x, y, Noise.getNoise(12, x, y));
		
		return null;
	}
	
	public int getSeaLevel(){
		return seaLevel;
	}
	
	public Thing genCliffAt(double x2, double y2, int[][] heights, int hx, int hy) {
		int x = (int) x2;
		int y = (int) y2;
		if(hx == 0 || hy == 0)
			return null;
		if(hx == heights.length || hy == heights[0].length)
			return null;
		int me = heights[hx][hy];
		
		//exception cases
		if(!exceptionCaseCliff(heights, hx, hy, me)){
		//if(true){
			//crossings
			if(heights[hx-1][hy] == me && heights[hx+1][hy] == me && heights[hx][hy-1] == me && heights[hx][hy+1] == me){
				if(heights[hx-1][hy-1] > me && heights[hx+1][hy+1] > me && heights[hx-1][hy+1] == me && heights[hx+1][hy-1] == me)
					return new Cliff(x, y, 8, 4);
				if(heights[hx-1][hy+1] > me && heights[hx+1][hy-1] > me && heights[hx-1][hy-1] == me && heights[hx+1][hy+1] == me)
					return new Cliff(x, y, 8, 3);
			}
			
			//doubleHeights
			if(heights[hx+1][hy-1] > me + 1) //tr 2
				return new Cliff(x, y, 7, 2);
			if(heights[hx-1][hy-1] > me + 1) //tl 2
				return new Cliff(x, y, 8, 2);
			if(heights[hx+1][hy+1] > me + 1) //br 2
				return new Cliff(x, y, 7, 1);
			if(heights[hx-1][hy+1] > me + 1) //bl 2
				return new Cliff(x, y, 7, 1);
			
			//pinched corners
			//topright
			if(heights[hx+1][hy+1] > me && heights[hx][hy-1] > me) //tr closing right
				return new Cliff(x, y, 5, 3);
			if(heights[hx+1][hy] > me && heights[hx-1][hy-1] > me) //tr closing up
				return new Cliff(x, y, 5, 3);
			//bottomright
			if(heights[hx+1][hy] > me && heights[hx-1][hy+1] > me) //br closing bottom
				return new Cliff(x, y, 5, 4);
			if(heights[hx+1][hy-1] > me && heights[hx][hy+1] > me) //br closing right
				return new Cliff(x, y, 5, 4);
			//topleft
			if(heights[hx-1][hy] > me && heights[hx+1][hy-1] > me) //tl closing top
				return new Cliff(x, y, 4, 3);
			if(heights[hx-1][hy+1] > me && heights[hx][hy-1] > me) //tl closing left
				return new Cliff(x, y, 4, 3);
			//bottomleft
			if(heights[hx-1][hy] > me && heights[hx+1][hy+1] > me) //bl closing bottom
				return new Cliff(x, y, 4, 4);
			if(heights[hx-1][hy-1] > me && heights[hx][hy+1] > me) //bl closing left
				return new Cliff(x, y, 4, 4);
			
			// 3/4 inner corners
			if(heights[hx+1][hy] > me && heights[hx][hy-1] > me) //tr
				return new Cliff(x, y, 5, 3);
			if(heights[hx+1][hy] > me && heights[hx][hy+1] > me) //br
				return new Cliff(x, y, 5, 4);
			if(heights[hx-1][hy] > me && heights[hx][hy-1] > me) //tl
				return new Cliff(x, y, 4, 3);
			if(heights[hx-1][hy] > me && heights[hx][hy+1] > me) //bl
				return new Cliff(x, y, 4, 4);
			
			//crossing slopes
			if(heights[hx][hy+1] > me && heights[hx][hy-1] == me && heights[hx-1][hy-1] > me) //u cross r
				return new Cliff(x, y, 8, 3);
			if(heights[hx][hy+1] > me && heights[hx][hy-1] == me && heights[hx+1][hy-1] > me) //u cross r
				return new Cliff(x, y, 9, 3);
			
			// sides
			if(heights[hx+1][hy] > me)
				return leftCliff(x, y);
			if(heights[hx-1][hy] > me)
				return rightCliff(x, y);
			if(heights[hx][hy-1] > me)
				return bottomCliff(x, y);
			if(heights[hx][hy+1] > me)
				return topCliff(x, y);
			
			// exception side
			if(heights[hx+1][hy-1] > me && heights[hx+1][hy+1] > me)
				return leftCliff(x, y);
			if(heights[hx-1][hy-1] > me && heights[hx-1][hy+1] > me)
				return rightCliff(x, y);
			if(heights[hx-1][hy+1] > me && heights[hx+1][hy+1] > me)
				return topCliff(x, y);
			if(heights[hx-1][hy-1] > me && heights[hx+1][hy-1] > me)
				return bottomCliff(x, y);
			
			// 1/4 outer corners
			if(heights[hx+1][hy+1] > me)//bl corner
				return new Cliff(x, y, 4, 0);
			if(heights[hx+1][hy-1] > me)//tl corner
				return new Cliff(x, y, 4, 2);
			if(heights[hx-1][hy+1] > me)//tr corner
				return new Cliff(x, y, 6, 0);
			if(heights[hx-1][hy-1] > me)//br corner
				return new Cliff(x, y, 6, 2);
		}
		return null;
	}
	


	private Thing bottomCliff(int x, int y) {
		if(cliffBecomesStepsAt(x, y)){
			Thing slope = new Slope(x, y, 7, 4);
			return slope;
		}
		return new Cliff(x, y, 5, 2);
	}

	private Thing topCliff(int x, int y) {
		if(cliffBecomesStepsAt(x, y)){
			Thing slope = new Slope(x, y, 7, 3);
			return slope;
		}		
		return new Cliff(x, y, 5, 0);
	}

	private Thing rightCliff(int x, int y) {
		if(cliffBecomesStepsAt(x, y)){
			Thing slope = new Slope(x, y, 6, 4);
			return slope;
		}
		return new Cliff(x, y, 6, 1);
	}

	private Thing leftCliff(int x, int y) {
		if(cliffBecomesStepsAt(x, y)){
			Thing slope = new Slope(x, y, 6, 3);
			return slope;
		}
		return new Cliff(x, y, 4, 1);
	}
	
	private boolean exceptionCaseCliff(int[][] heights, int hx, int hy, int me) {
		int aboveMeCount = 0;
		int belowMeCount = 0;
		
		//square pinch
		if(heights[hx-1][hy-1] > me && heights[hx+1][hy-1] > me && heights[hx-1][hy+1] > me && heights[hx+1][hy+1] > me)
			return true;
		
		//diagonal pinches
		if(heights[hx-1][hy-1] > me && heights[hx+1][hy] > me && heights[hx][hy+1] > me) //tl
			return true;
		if(heights[hx+1][hy-1] > me && heights[hx-1][hy] > me && heights[hx][hy+1] > me) //tr
			return true;
		if(heights[hx-1][hy+1] > me && heights[hx+1][hy] > me && heights[hx][hy-1] > me) //bl
			return true;
		if(heights[hx+1][hy+1] > me && heights[hx-1][hy] > me && heights[hx][hy-1] > me) //br
			return true;
		
		if(heights[hx-1][hy] > me)
			aboveMeCount++;
		if(heights[hx+1][hy] > me)
			aboveMeCount++;
		if(heights[hx][hy+1] > me)
			aboveMeCount++;
		if(heights[hx][hy-1] > me)
			aboveMeCount++;
		
		if(heights[hx-1][hy] < me)
			belowMeCount++;
		if(heights[hx+1][hy] < me)
			belowMeCount++;
		if(heights[hx][hy+1] < me)
			belowMeCount++;
		if(heights[hx][hy-1] < me)
			belowMeCount++;
		
		//pinch case
		if(heights[hx][hy-1] > me && heights[hx][hy+1] > me)
			return true;
		if(heights[hx-1][hy] > me && heights[hx+1][hy] > me)
			return true;
		
		return aboveMeCount >= 3 || belowMeCount == 3;
	}
	
	private boolean cliffBecomesStepsAt(int x, int y) {
		boolean yes1 = Noise.getNoise(21, x, y) > .85;
		boolean yes2 = Noise.getNoise(7, x, y) > .8;
		return yes1 || yes2;
	}

	public int[][] genHeights(double x, double y) {
		int gridSize = 16;
		int intX = F.snap(x, gridSize);
		int intY = F.snap(y, gridSize);
		int heightWidth = World.CHUNK_SIZE*2*2/gridSize+1;
		int[][] heights = new int[heightWidth][heightWidth];
		int i2 = 0;
		for(int i = F.snap(intX-World.CHUNK_SIZE*2,gridSize); i < intX+World.CHUNK_SIZE*2; i += gridSize){
			int j2 = 0;
			for(int j = F.snap(intY-World.CHUNK_SIZE*2,gridSize); j < intY+World.CHUNK_SIZE*2; j += gridSize){
				heights[i2][j2] = getNoiseHeight(i, j);
				j2++;
			}
			i2++;
		}
		return heights;
	}

	public int getNoiseHeight(double x, double y){
		double choice = Noise.getNoise(seed, x/40.123D / heightScale, y/40.43D / heightScale);
		int height =  (int)(heightLevels * Math.pow(choice, heightPower));
		return height;
	}

	public void giveCl(ChangeLog cl) {
		this.cl = cl;
	}
	
	public int getHeightLevels(){
		return heightLevels;
	}

	public ChangeLog getChangeLog() {
		return cl;
	}
}
