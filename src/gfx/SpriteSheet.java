package gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	public int width, height;
	String path;
	public static final int ALPHA_01 = 0x7f007f;
	public static final int ALPHA_02 = 0xff00ff;
	public int[] tilePixels, screenPixels;
	int gridSize, gridPower;
	
	public SpriteSheet(String path, int gridSize){
		this.gridSize = gridSize;
		gridPower = calcGridPower(gridSize);
		this.screenPixels = Screen.screenPixels;
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(image == null) {
			System.out.println("failed to find spritesheet at" + path);
			return;
		}
		
		this.path = path;
		this.width = image.getWidth();
		this.height = image.getHeight();
		tilePixels = image.getRGB(0, 0, width, height, null, 0, width);
	}
		
	private int calcGridPower(int gridSize) {
		switch(gridSize){
		case 4:		return 2;
		case 8:		return 3;
		case 16:	return 4;
		case 32:	return 5;
		case 64:	return 6;
		case 128:	return 7;
		}
		return 0;
	}
}
