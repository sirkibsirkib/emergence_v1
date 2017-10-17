package game;

import gfx.SpriteSheet;

import java.util.ArrayList;
import java.util.List;

public class Room {
	private Game game;
	private List<Feature> ls;
	private int[][] heights;
	private int genX, genY;
	
	Room(Game game){
		this.game = game;
		ls = new ArrayList<>();
	}

	public Feature addToChunk(Feature f) {
		ls.add(f);
		return f;
	}

	public void drawAllSprites(SpriteSheet forest) {
		for(int i = 0; i < ls.size(); i++){
			game.getScreen().drawSprite(ls.get(i), forest);
		}
	}
	
	public void genChunkAt(int x, int y){
		int gridSize = 16;
		genX = x;
		genY = y;
		x = F.snap(x, gridSize);
		y = F.snap(y, gridSize);
		
		for(int i = x-game.getChunkSize(); i < x+game.getChunkSize(); i += gridSize){
			for(int j = y-game.getChunkSize(); j < y+game.getChunkSize(); j += gridSize){
				Feature generated = game.getPlanet().noiseFeature(i, j);
				//Feature generated = game.getPlanet().noiseHeightFeature(i, j);
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
	
	public int countFeaturesWithin(int x, int y, int radius){
		int result = 0;
		for(int i = 0 ; i < ls.size(); i++){
			Feature next = ls.get(i);
			if(F.manhattan(x, y, next.getX(), next.getY()) < radius)
				result++;
		}
		return result;
	}

	public void genChunkAt(Feature feature) {
		genChunkAt(feature.getX(), feature.getY());
	}

	public void clearChunk() {
		ls.clear();
	}

	public void bound(Feature f) {
		int pSize = game.getPlanet().getSize();
		if(f.getX() < 0)
			f.setX(f.getX() + pSize);
		else if(f.getX() >= pSize)
			f.setX(f.getX() - pSize);
		
		if(f.getY() < 0)
			f.setY(f.getY() + pSize);
		else if(f.getY() >= pSize)
			f.setY(f.getY() - pSize);
	}
}
