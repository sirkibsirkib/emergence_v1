package gfx;


import general.Colour;
import general.F;
import general.S;
import space.GeneratorGal;
import space.SpacePlanet;
import space.SpaceShip;
import surface.View;
import abstracts.Coordinate;
import abstracts.StellarBody;
import abstracts.Thing;

public abstract class Screen {	
	public static int[] screenPixels;
	private static final double WORLD_CURVE = 1.3,
			SHADOW_HEIGHT = .4;
	private static final int PROJECTED_LINEAR_DIVIDER = 30_000;
	private static boolean flatProjection = false;
	
	public static void init(int[] given) {
		screenPixels = given;
	}
	
	public static void clearScreen(Thing focus){
		int screenWidth = S.WIDTH;
		int screenHeight = S.HEIGHT;
		
		if(S.location() == null){
			for(int i = 0; i < screenWidth; i++)
				for(int j = 0; j < screenHeight; j++)
					screenPixels[i + (screenWidth*j)] = 0;
		}else{
			if(S.selected().getVehicle() == null)
				tileBackground((int)focus.getX(), (int)focus.getY());
			else
				tileBackground((int) (focus.getX() - S.vehicle().getView().getX()),
						(int) (focus.getY() - S.vehicle().getView().getY()));
		}
	}
	
	private static void tileBackground(int xOffset, int yOffset){
		SpriteSheet ss = S.forest;
		int tileX = 7 << 4;
		int tileY = 0 << 4;
		
		for(int i = 0; i < S.WIDTH; i++){
			for(int j = 0; j < S.HEIGHT; j++){
				int tilePixX = tileX + F.mod(i + xOffset, 32);
				int tilePixY = tileY + F.mod(j + yOffset, 32);
				int tileColour = ss.tilePixels[tilePixX + (tilePixY)*ss.width] & 0x00ffffff;
				Colour actual = new Colour(tileColour).average(S.location().getSoil());
				screenPixels[i + (S.WIDTH*j)] = actual.getHex();
			}
		}
	}
	
	public static void drawProjected(StellarBody t, View v){
		double sX = t.getX();
		double sY = t.getY();
		
		double drawDistance, drawRadius;
		double distance = F.distance(v.getX(), v.getY(), sX, sY);
		double radiusHelp = t.getRadius() *.06 / Math.pow(distance, .23);
		
		drawDistance = 30*Math.pow(distance, .07) + distance / PROJECTED_LINEAR_DIVIDER;
		drawRadius = (int) (radiusHelp + 9_000_000D * t.getRadius() / Math.pow((distance+1_500), 2));
		if(Math.sqrt(10*distance) < drawDistance)
			drawDistance = Math.sqrt(10*distance);
		
		Colour col = t.getColourAt(0, 0);
		boolean uniformColour = t.uniformColour();
		if(uniformColour)
			col = distanceDim(col, distance, GeneratorGal.observableUniverse*.2, GeneratorGal.observableUniverse * .7);
		double relX = (sX - v.getX())*drawDistance/distance;
		double relY = (sY - v.getY())*drawDistance/distance;
		int screenX = (int) (relX + S.WIDTH/2);
		int screenY = (int) (relY + S.HEIGHT/2);
		int startI = F.max((int) -drawRadius, -screenX);
		int startJ = F.max((int) -drawRadius, -screenY);
		int endI = F.min(-screenX+S.WIDTH-1, (int)drawRadius);
		int endJ = F.min(-screenY+S.HEIGHT-1, (int)drawRadius);
		
		for(int i = startI; i <= endI; i++){
			for(int j = startJ; j <= endJ; j++){
				if(F.distance(i, j, 0, 0) >= drawRadius + .2)
					continue;
				int screenPixelIndex = (screenY + j)*S.WIDTH + screenX + i;
				
				if(!uniformColour){
					double scaleFactor = SpacePlanet.WORLD_SCALE_UP * t.getRadius();
					int colX = F.snap(5*F.nPow(i/drawRadius, WORLD_CURVE)*scaleFactor, 16);
					int colY = F.snap(5*F.nPow(j/drawRadius, WORLD_CURVE)*scaleFactor, 16);
					col = t.getColourAt(colX, colY);
					col = distanceDim(col, distance, GeneratorGal.planetsOnlyWithin*.15, GeneratorGal.planetsOnlyWithin*.5);
					
					if(S.vehicle() != null){
						double distToShadow = F.manhattan(i*(1 + SHADOW_HEIGHT) + screenX, j*(1 + SHADOW_HEIGHT) + screenY, S.WIDTH/2, S.HEIGHT/2);
						if(distToShadow < 4){
							col = Colour.BLACK;
							if(distToShadow <= 1 && t.clearAt(colX, colY))
								S.vehicle().setOrbit(new Coordinate(colX, colY), t);
						}
					}
				}
				screenPixels[screenPixelIndex] = col.getHex();
			}
		}
	}
	
	public static Colour distanceDim(Colour original, double distance, double startDim, double endDim){
		if(distance < startDim)
			return original;
		if(distance >= endDim)
			return Colour.BLACK;
		double scale = endDim - startDim;
		double progression = distance - startDim;
		return original.darken(1-(progression / scale));
		
	}
	
	public static void drawShip(SpaceShip s, double direction, double scale, View v){
		SpriteSheet ss = S.forest;
		int tileXPixel = 0<<ss.gridPower;
		int tileYPixel = 4;
		switch(s.getSpriteCode()){
		case 'w': tileYPixel = 5; if(!S.gamePaused) direction += F.rdg(.02)-.01;	break;
		case 'a': tileYPixel = 7; break;
		case 's': tileYPixel = 6; if(!S.gamePaused) direction += F.rdg(.02)-.01;	break;
		case 'd': tileYPixel = 8; break;
		}
		
		tileYPixel = tileYPixel<<ss.gridPower;
		int screenX = S.WIDTH/2;
		int screenY = S.HEIGHT/2;
		double drawScale = scale;
		if(v.getFollowing() != s){
			double distance = F.distance(v.getX(), v.getY(), s.getX(), s.getY());
			if(distance > 6_000)
				return;
			double drawDistance = 30*Math.pow(distance, .07) + distance / PROJECTED_LINEAR_DIVIDER;
			if(Math.sqrt(10*distance) < drawDistance)
				drawDistance = Math.sqrt(10*distance);
			drawScale = 580D * scale /(distance+900) + .06;
			double relX = (s.getX() - v.getX())*drawDistance/distance;
			double relY = (s.getY() - v.getY())*drawDistance/distance;
			
			screenX = (int) (relX + S.WIDTH/2);
			screenY = (int) (relY + S.HEIGHT/2);
		}else{
			Coordinate stopAt = s.getStopCoordinate();
			drawDot(v, stopAt, new Colour(F.rng(256), F.rng(256), F.rng(256)));
		}
		drawRotatedSprite(ss, screenX, screenY, tileXPixel, tileYPixel, direction, drawScale);
	}
	
	private static void drawDot(View v, Coordinate at, Colour colour) {
		int screenX,  screenY;
		if(flatProjection){
			screenX = (int) ((at.getX() - v.getX())/99 + 8 + S.WIDTH/2);
			screenY = (int) ((at.getY() - v.getY())/99 + 8 + S.HEIGHT/2);
		}else{
			double distance = F.distance(v.getX(), v.getY(), at.getX(), at.getY());
			double drawDistance = 30*Math.pow(distance, .07) + distance / PROJECTED_LINEAR_DIVIDER;
			if(Math.sqrt(20*distance) < drawDistance)
				drawDistance = Math.sqrt(20*distance);
			double relX = (at.getX() + 8 - v.getX())*drawDistance/distance;
			double relY = (at.getY() + 8 - v.getY())*drawDistance/distance;
			
			screenX = (int) (relX + S.WIDTH/2);
			screenY = (int) (relY + S.HEIGHT/2);
		}
		if(screenX >= 0 && screenX < S.WIDTH && screenY >= 0 && screenY < S.HEIGHT){
			int screenPixelIndex = screenY*S.WIDTH + screenX;
			screenPixels[screenPixelIndex] = colour.getHex();
		}
	}

	public static void drawFromSheet(SpriteSheet ss, int sheetX, int sheetY, int width, int height, int color, int screenX, int screenY){
		int endI = F.min(-screenX+S.WIDTH, width);
		int endJ = F.min(-screenY+S.HEIGHT, height);
		for(int i = F.max(0, -screenX); i < endI; i++){
			for(int j = F.max(0, -screenY); j < endJ; j++){
				int tileColour = ss.tilePixels[sheetX+i + (sheetY+j)*ss.width] & 0x00ffffff;
				if(tileColour == SpriteSheet.ALPHA_01 || tileColour == SpriteSheet.ALPHA_02)
					continue;
				tileColour = color;
				int screenPixelIndex = (screenY + j)*S.WIDTH + screenX + i;
				screenPixels[screenPixelIndex] = tileColour;
			}
		}
	}

	public static void drawFlat(Thing feature, View v) {
		SpriteSheet ss = S.forest;
		if(feature == null) //weird bug
			return;
		double fX = feature.getX();
		double fY = feature.getY();
		double spriteScale = feature.getSpriteScale();
		int screenX = (int) (fX - v.getX() + S.WIDTH/2);
		int screenY = (int) (fY - v.getY() + S.HEIGHT/2);
		int tileXPixel = feature.getSpriteSheetX()<<ss.gridPower;
		int tileYPixel = feature.getSpriteSheetY()<<ss.gridPower;
		
		if(spriteScale == 1)
			drawUnscaledSprite(ss, screenX, screenY, tileXPixel, tileYPixel);
		else
			drawScaledSprite(ss, screenX, screenY, tileXPixel, tileYPixel, spriteScale);
	}

	public static void drawScaledSprite(SpriteSheet ss, int screenX, int screenY, int tileXPixel, int tileYPixel, double spriteScale) {
		if(spriteScale <= 0)
			return;
		int endI = F.min(-screenX+S.WIDTH, (int)(ss.gridSize*spriteScale));
		int endJ = F.min(-screenY+S.HEIGHT, (int)(ss.gridSize*spriteScale));
		for(int i = F.max(0, -screenX); i < endI; i++){
			for(int j = F.max(0, -screenY); j < endJ; j++){
				int tileColour = ss.tilePixels[tileXPixel+(int)(i/spriteScale) + (tileYPixel+(int)(j/spriteScale))*ss.width] & 0x00ffffff;
				if(tileColour == SpriteSheet.ALPHA_01 || tileColour == SpriteSheet.ALPHA_02)
					continue;
				int screenPixelIndex = (screenY + j)*S.WIDTH + screenX + i;
				screenPixels[screenPixelIndex] = tileColour;
			}
		}
	}

	private static void drawUnscaledSprite(SpriteSheet ss, int screenX, int screenY, int tileXPixel, int tileYPixel) {
		int endI = F.min(-screenX+S.WIDTH, (int)(ss.gridSize));
		int endJ = F.min(-screenY+S.HEIGHT, (int)(ss.gridSize));
		for(int i = F.max(0, -screenX); i < endI; i++){
			for(int j = F.max(0, -screenY); j < endJ; j++){
				int tileColour = ss.tilePixels[tileXPixel+i + (tileYPixel+j)*ss.width] & 0x00ffffff;
				if(tileColour == SpriteSheet.ALPHA_01 || tileColour == SpriteSheet.ALPHA_02)
					continue;
				int screenPixelIndex = (screenY + j)*S.WIDTH + screenX + i;
				screenPixels[screenPixelIndex] = tileColour;
			}
		}
	}
	
	public static void drawRotatedSprite(SpriteSheet ss, int screenX, int screenY, int tileXPixel, int tileYPixel, double rotation, double scale) {
		scale = scale/.8;
		//offset the sprite to compensate for origin of sprite at (0,0)
		double offsetDir = rotation+Math.PI*5/4;
		screenX -= -Math.cos(offsetDir)*8*scale+.5;
		screenY -= Math.sin(offsetDir)*8*scale-.5;
		
		int endI = F.min(-screenX+S.WIDTH, (int)(ss.gridSize*scale));
		int endJ = F.min(-screenY+S.HEIGHT, (int)(ss.gridSize*scale));
		for(int i = F.max(0, -screenX); i < endI; i++){
			for(int j = F.max(0, -screenY); j < endJ; j++){
				double distance = F.distance(0, 0, i, j);
				double angleTo = Math.atan(1.0*j/i);
				
				int rotX = (int) (Math.cos(rotation+angleTo) * distance*.7);
				if(rotX + screenX < 0 || rotX + screenX >= S.WIDTH)
					continue;
				int rotY = (int) (-Math.sin(rotation+angleTo) * distance*.7);
				if(rotY + screenY < 0 || rotY + screenY >= S.HEIGHT)
					continue;
				int tileColour = ss.tilePixels[tileXPixel+(int)(i/scale) + (tileYPixel+(int)(j/scale))*ss.width] & 0x00ffffff;
				if(tileColour == SpriteSheet.ALPHA_01 || tileColour == SpriteSheet.ALPHA_02)
					continue;
				int screenPixelIndex = (screenY + rotY)*S.WIDTH + screenX + rotX;
				screenPixels[screenPixelIndex] = tileColour;
			}
		}
	}

	public static void darkenBox(int x1, int y1, int x2, int y2) {
		for(int i = x1; i < x2; i++){
			if(i >= S.WIDTH || i < 0)
				continue;
			for(int j = y1; j < y2; j++){
				if(j >= S.HEIGHT || j < 0)
					continue;
				int screenPixelIndex = (j)*S.WIDTH + i;
				if(screenPixelIndex >= screenPixels.length || screenPixelIndex < 0)
					continue;
				Colour darkened = new Colour(screenPixels[screenPixelIndex]).darken(.3);
				int pixelColor = darkened.getHex();
				screenPixels[screenPixelIndex] = pixelColor;
			}
		}
	}

	public static void drawProjected(Thing s, View v, double scale, double rotation) {
		SpriteSheet ss = S.forest;
		int tileXPixel = s.getSpriteSheetX()<<ss.gridPower;
		int tileYPixel = s.getSpriteSheetY()<<ss.gridPower;
		int screenX = S.WIDTH/2;
		int screenY = S.HEIGHT/2;
		
		double drawScale = scale;
		double distance = F.distance(v.getX(), v.getY(), s.getX(), s.getY());
		if(distance > 6_000)
			return;
		double drawDistance = 30*Math.pow(distance, .07);
		if(Math.sqrt(20*distance) < drawDistance)
			drawDistance = Math.sqrt(20*distance);
		drawScale = 150D * scale /(distance+150) + .02;
		double relX = (s.getX() - v.getX())*drawDistance/distance;
		double relY = (s.getY() - v.getY())*drawDistance/distance;
		
		screenX = (int) (relX + S.WIDTH/2);
		screenY = (int) (relY + S.HEIGHT/2);
		drawRotatedSprite(ss, screenX, screenY, tileXPixel, tileYPixel, rotation, drawScale);
	}
}
