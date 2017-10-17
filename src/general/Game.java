package general;

/* 
 * TODO:
 * collisions. bounding boxes
 * universe screen
 * movement in space
 * zooming space debris
 * bounded planets
 * approaching and landing on planets
 * planet exteriors
 * landing sites & correlation
 * saving and loading changeLogs
 * flushing old changes from changeLog
 * multiple characters
 * tabbing between characters
 * chunk loading
 * ship screen
 * more shit on planet surfaces
 * ground textures
 * inventory per character
 * menus, interactive UI
 * saving & loading game progress
 * ship interior
 * crafting recipe mechanism
 * functional crafting
 * bag space
 * physical object placement from inventory
 * NPCs
 * dialogue
 * text rendering on dialogue boxes
 * weather
 * trees
 * mining
 * prospecting
 * comms chatter
 */

import gfx.Screen;
import gfx.SpriteSheet;
import gfx.StringWriter;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import ship.Human;
import ship.Ship;
import space.SpaceShip;
import surface.World;

public class Game extends Canvas implements Runnable{
	private static final String NAME	= "Emergence";
	private static final long serialVersionUID = -5603131917885836790L;
	public static boolean debug = false;
	
	private boolean running = false;
	private BufferedImage image = new BufferedImage(S.WIDTH, S.HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] screenPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	private JFrame frame;
	private static final int FPS = 30;
	private static final int UPS = 30;
	public long ticks;
	
	public Game() {
		S.game = this;
		setMinimumSize(new Dimension(S.WIDTH*S.SCALE, S.HEIGHT*S.SCALE));
		setMaximumSize(new Dimension(S.WIDTH*S.SCALE, S.HEIGHT*S.SCALE));
		setPreferredSize(new Dimension(S.WIDTH*S.SCALE, S.HEIGHT*S.SCALE));
		prepareJFrame();
	}

	private void prepareJFrame() {
		frame = new JFrame(NAME);
		setFocusTraversalKeysEnabled(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void init(){
		Screen.init(screenPixels);
		
		S.forest = new SpriteSheet("/spriteSheet.png", 16);
		S.letters = new SpriteSheet("/letters.png", 5);
		S.input = new InputHandler();
		
		Ship ship = S.fleet.add(new Ship(4800, 5990, S.logger.getLogAt('s', 0, 1)));
		ship.addCrewMember(S.addMember(new Human(-8, -8, ship)));
		ship.addCrewMember(S.addMember(new Human(8, -8, ship)));
		ship.addCrewMember(S.addMember(new Human(-8, 8, ship)));
		SpaceShip mrShip = ship.getMrShip();
		S.galaxy.add(mrShip);
		ship.init();
		S.selectDown();
		S.focusViews();
		updateChunks();
		S.galaxy.jumpToAPlanet();
		
		ship = S.fleet.add(new Ship(mrShip.getX(), mrShip.getY() + 100, S.logger.getLogAt('s', 0, 1)));
		ship.addCrewMember(S.addMember(new Human(0, 0, ship)));
		mrShip = ship.getMrShip();
		S.galaxy.add(mrShip);
		ship.init();
	}
	
	public void goToSpace(){
		if(S.location() == null || !S.selected().getPiloting() || S.vehicle() == null)
			return;
		S.location().getGrid().remove(S.vehicle().getLander());
		S.selected().setPiloting(true);
		S.vehicle().setLocation(null);
		S.galaxy.add(S.vehicle().getMrShip());
		S.galaxy.setView(S.vehicle().getMrShip());
		updateChunks();
	}
	
	public void land(Ship ship, World w){
		if(S.location() != null)
			return;
		S.selected().getPiloting();
		ship.setLocation(w);
		updateChunks();
	}

	public synchronized void go() {
		running = true;
		ticks = 0;
		new Thread(this).start();
	}
	
	public synchronized void stop() {
		running = false;
	}
	
	public void run() {
		long lastTick = System.nanoTime();
		long lastRender = lastTick;
		double nsPerTick = 1_000_000_000D/UPS;
		double nsPerRender = 1_000_000_000D/FPS;
		init();
		while(running){
			try {Thread.sleep(3);} catch (InterruptedException e) {e.printStackTrace();}
			long now = System.nanoTime();
			if(now - lastTick > nsPerTick){
				tick();
				lastTick = now; 
			}
			if(now - lastRender > nsPerRender){
				render();
				lastRender = now; 
			}
		}
	}
	
	public void tick() {
		tickInput();
		updateChunks();
		if(!S.gamePaused)
			S.galaxy.tick();
		if((ticks+1) % 200 == 0) S.logger.saveAlteredLogs();
		S.input.tick();
		ticks++;
	}

	private void tickInput() {
		if(S.menu != null || S.gamePaused)
			return;
		
		boolean isPiloting = S.selected().getPiloting();
		Human controlling = S.selected();
		
		if(S.vehicle() != null && isPiloting){
			SpaceShip ss = S.vehicle().getMrShip();
			if(S.input.c.isPressed())
				ss.stopAssist();
			else if(S.input.left.isPressed() && !S.input.right.isPressed()){
				ss.setKillEngines(false);
				ss.turnLeft(ss.getTurnForce());
			}
				
			else if(S.input.right.isPressed() && !S.input.left.isPressed()){
				ss.setKillEngines(false);
				ss.turnRight(ss.getTurnForce());
			}
				
			else if(!S.input.x.isPressed() && !S.input.left.isPressed() && !S.input.right.isPressed() && ss.getTurnSpeed() != 0 && !ss.getKillEngines())
				ss.turnAssist();
				
			else if(S.input.up.isPressed() && !S.input.down.isPressed()){
				ss.setKillEngines(false);
				ss.speedUp(ss.getBoostForce());
			}
				
			else if(S.input.down.isPressed() && !S.input.up.isPressed()){
				ss.setKillEngines(false);
				ss.slowDown(ss.getBoostForce());
			}
			return;
		}
		if(S.input.up.isPressed())		controlling.up();
		if(S.input.down.isPressed())	controlling.down();
		if(S.input.left.isPressed())	controlling.left();
		if(S.input.right.isPressed()) 	controlling.right();
	}

	public void updateChunks() {
		if(S.location() != null)
			S.location().checkNewChunk();
		else					
			S.galaxy.checkNewChunk();
	}
	
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		Screen.clearScreen(S.selected());
		boolean piloting = S.selected().getPiloting();
		
		if(S.location() != null && S.vehicle() == null)
			S.location().render();
		
		if(S.vehicle() != null){
			SpaceShip mrShip = S.vehicle().getMrShip();
			if(S.location() == null){
				S.galaxy.render();
				if(piloting && S.hud()){
					StringWriter.writeStringToScreen("x:" + mrShip.getDisplayX(), 5, 5, 0xffffff);
					StringWriter.writeStringToScreen("y:" + mrShip.getDisplayY(), 5, 14, 0xffffff);
					StringWriter.writeStringToScreen("spd:  "+(int)(mrShip.getSpeed()*10)/10D, 5, 23, 0xffffff);
					StringWriter.writeStringToScreen("fuel: "+F.dec(S.vehicle().getFuel()), 5, 32, 0xffffff);
				}
				if(mrShip.getSpeed() < 10){
					if(piloting)
						S.galaxy.advertiseLandingOption(mrShip);
				}
			}else
				S.location().render();
			if(!piloting)
				S.vehicle().draw();
				
		}
		
		if(S.menu != null)
			S.menu.render();
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}
	

	//main
	public static void main(String[] args) {
		new Game().go();
	}
}
