package game;

import java.util.List;

public class Chunk {
	int x, y;
	Planet p;
	
	List<Feature> ls;
	
	Chunk(int x, int y, Planet p){
		this.x = x;
		this.y = y;
		this.p = p;
		x = F.snap(x, Statics.GRID_SIZE);
		y = F.snap(y, Statics.GRID_SIZE);
		
		for(int i = 0; i < Statics.CHUNK_WIDTH; i += Statics.GRID_SIZE){
			for(int j = 0; j < Statics.CHUNK_WIDTH; j += Statics.GRID_SIZE){
				Feature generated = p.noiseFeature(x + i, y + j);
				if(generated != null)
					ls.add(generated);
			}
		}
	}
	
	public Feature getFeatureAt(int x, int y){
		for(int i = 0 ; i < ls.size(); i++){
			Feature next = ls.get(i);
			if(next.getX() == x && next.getY() == y)
				return next;
		}
		return null;
	}
	
	public boolean isChunkAt(int x, int y){
		return x == this.x && y == this.y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public boolean isWithinChunk(int x, int y){
		return x >= this.x &&
				y >= this.y &&
				x < this.x+Statics.CHUNK_WIDTH &&
				y < this.y+Statics.CHUNK_WIDTH;
	}

	public Feature addFeature(Feature f) {
		ls.add(f);
		return f;
	}
	
	public int getNumberOfFeatures(){
		return ls.size();
	}
	
	public Feature get(int index){
		return ls.get(index);
	}
	
	public boolean isOnScreen(int x1, int y1, int x2, int y2){
		return x < x1 &&
				y < y1 &&
				x+Statics.CHUNK_WIDTH >= x2 &&
				y+Statics.CHUNK_WIDTH >= y2;
	}
}
