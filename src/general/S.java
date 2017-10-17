package general;

import gfx.SpriteSheet;
import menu.Menu;
import ship.Crew;
import ship.Fleet;
import ship.Human;
import ship.Ship;
import space.Galaxy;
import surface.World;

public class S {
	public static final int WIDTH = 250; //200
	public static final int HEIGHT = WIDTH/12*9;
	public static final int SCALE = 4;
	
	public static final int CHUNK_WIDTH = 720;
	public static final int CHUNK_SIZE = F.snap(WIDTH, 16) + 64;
	public static final int GRID_SIZE = 16;
	
	public static Game game;
	public static InputHandler input;
	public static ChangeLogger logger = new ChangeLogger();
	public static Galaxy galaxy = new Galaxy(logger.getLogAt('g', 0, 0));
	public static Menu menu;
	public static SpriteSheet forest;
	public static SpriteSheet letters;
	public static Fleet fleet = new Fleet();
	
	public static boolean gamePaused = false;
	public static boolean hud = true;

	public static Crew crew = new Crew();
	
	public static World getWorldAlreadyLoaded(double x, double y){
		for(int i = 0; i < crew.size(); i++){
			Human next = crew.get(i);
			if(next.getLocation() != null && next.getLocation().isLocatedInSpaceAt(x, y)){
//				System.out.println("World already loaded!");
				return next.getLocation();
			}
		}
//		System.out.println("world not loaded");
		return null;
	}
	
	public static Human addMember(Human h){
		return crew.addMember(h);
	}
	
	public static void selectDown(){
		crew.selectDown();
		focusViews();
	}

	public static void focusViews() {
		System.out.println("focus views");
		if(vehicle() == null)
			location().setView(selected());
		else{
			vehicle().setView(selected());
			if(location() != null)
				location().setView(vehicle().getLander());
			else
				S.galaxy.setView(vehicle().getMrShip());
		}
	}
	
	public static Human selected(){
		return crew.getSelected();
	}
	
	public static World location(){
		return selected().getLocation();
	}
	
	public static Ship vehicle(){
		return selected().getVehicle();
	}

	public static void toggleGamePaused() {
		gamePaused = gamePaused? false : true;
	}

	public static void selectUp() {
		crew.selectUp();
		focusViews();
	}

	public static void toggleHud() {
		hud = hud ? false : true;
	}

	public static boolean hud() {
		return hud;
	}

	public static void select(int digit) {
		if(crew.select(digit - 1))
			focusViews();
	}
}
