package game;

public class Chunker {
	private Chunk[][] chunks;
	private Planet p;
	private int centerX, centerY;
	
	public Chunker(Planet p){
		this.p = p;
		chunks = new Chunk[3][3];
	}
	
	public void moveTo(int x, int y){
		if(x < centerX)
			chunkLeft();
		if(y < centerY)
			chunkUp();
		if(x >= centerX + Statics.CHUNK_WIDTH)
			chunkRight();
		if(x >= centerY + Statics.CHUNK_WIDTH)
			chunkDown();
	}
	
	private void chunkLeft() {
		for(int i = 2; i > 0; i--){
			for(int j = 0; j < 3; j++)
				chunks[i][j] = chunks[i-1][j];
		}
		for(int j = 0; j < 3; j++)
			chunks[0][j] = makeChunk();
	}

	private Chunk makeChunk(int x, int y){
		return new Chunk(x, y, p);
	}
	
	private boolean hasChunkAt(int x, int y){
		for(int )
	}
}
