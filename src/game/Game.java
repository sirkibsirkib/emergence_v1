package game;

import gfx.Screen;
import gfx.SpriteSheet;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{
	private static final String NAME	= "Emergence";
	private static final long serialVersionUID = -5603131917885836790L;
	private final int WIDTH = 160;
	private final int HEIGHT = WIDTH/12*9;
	private static final int SCALE = 4;
	
	private boolean running = false;
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] screenPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	private JFrame frame;
	private Screen screen;
	private InputHandler input;
	private SpriteSheet forest;
	private static final int FPS = 30;
	private static final int UPS = 30;
	private static final int CHUNK_SIZE = F.snap(200, 16);
	private Room room;
	private Planet planet;
	private View view;
	private Human mrGuy;
	
	public Game() {
		setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		prepareJFrame();
	}

	private void prepareJFrame() {
		frame = new JFrame(NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void init(){
		screen = new Screen(screenPixels, this);
		forest = new SpriteSheet("/spriteSheet.png", screen, 16);
		input = new InputHandler(this);
		room = new Room(this);
		mrGuy = (Human) room.addToChunk(new Human(F.snap(100, 16), F.snap(100, 16)));
		view = new View(mrGuy);
		planet = new Planet(0, 743763, 300);
	}

	public synchronized void go() {
		running = true;
		new Thread(this).start();
	}
	
	public synchronized void stop() {
		running = false;
	}
	
	public void run() {
		long lastTick = System.nanoTime();
		long lastRender = lastTick;
		double nsPerTick = 1000000000D/UPS;
		double nsPerRender = 1000000000D/FPS;
		
		init();
		
		while(running){
			try {
				Thread.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
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
		if(input.up.isPressed()) mrGuy.up();
		if(input.down.isPressed()) mrGuy.down();
		if(input.left.isPressed()) mrGuy.left();
		if(input.right.isPressed()) mrGuy.right();
		
//		room.bound(mrGuy);
//		System.out.println(mrGuy.getX() + " " + mrGuy.getY());
		
		if(screen.leavingCurrentChunk()){
			room.clearChunk();
			room.genChunkAt(view.getFollowing());
			room.addToChunk(mrGuy);
			screen.setCurrentChunk();
		}
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		
		screen.clearScreen();
		room.drawAllSprites(forest);
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		
		g.dispose();
		bs.show();
	}
	

	public static void main(String[] args) {
		new Game().go();
	}

	public Screen getScreen() {
		return screen;
	}

	public View getView() {
		return view;
	}

	public Planet getPlanet() {
		return planet;
	}

	public int getChunkSize() {
		return CHUNK_SIZE;
	}

	public int getScreenWidth() {
		return WIDTH;
	}
	
	public int getScreenHeight() {
		return HEIGHT;
	}
}
