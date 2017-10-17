package ship;

import general.Grid;
import general.Inventory;
import general.S;
import surface.Airlock;
import surface.Lander;
import surface.World;
import abstracts.Environment;
import abstracts.Solid;
import abstracts.Thing;

public class Human extends Solid{
	private int SPEED = 2;
	private Inventory inventory;
	private World location;
	private Ship vehicle;
	private boolean piloting;
	
	public Human(double x, double y, World location) {
		super(x, y, 1);
		setBoundingBox(4, 7, 12, 14);
		piloting = false;
		this.location = location;
	}
	
	public Human(double x, double y, Ship vehicle) {
		super(x, y, 1);
		setBoundingBox(4, 7, 12, 14);
		piloting = false;
		this.vehicle = vehicle;
		if(vehicle != null)
			this.location = vehicle.getLocation();
	}
	
	public void setLocation(World l){
		location = l;
	}
	
	public void togglePiloting(){
		piloting = piloting ? false : true;
	}
	
	public boolean getPiloting(){
		return piloting;
	}
	
	public World getLocation(){
		return location;
	}
	
	public void addToInventory(Thing f){
		if(inventory == null)
			inventory = new Inventory();
		inventory.add(f);
	}
	
	public boolean removeFromInventory(Thing f){
		if(inventory == null)
			return false;
		return inventory.remove(f);
	}

	public int getSpriteSheetX() {
		return 0;
	}

	public int getSpriteSheetY() {
		return 3;
	}
	
	private boolean tryMove(int relX, int relY, Grid grid){
		x += relX;
		y += relY;
		Thing collidedWith = grid.findCollision(this, true);
		if(collidedWith != null){
			x += -relX;
			y += -relY;
			return false;
		}
		return true;
	}

	public void up() {
		tryMove(0, -SPEED, grid());
	}
	
	public void down() {
		tryMove(0, SPEED, grid());
	}
	
	public void left() {
		tryMove(-SPEED, 0, grid());
	}
	
	public void right() {
		tryMove(SPEED, 0, grid());
	}

	public void setSpriteScale(double spriteScale) {
		this.spriteScale = spriteScale;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Thing copy() {
		if(vehicle != null)
			return new Human(x, y, vehicle);
		return new Human(x, y, location);
	}
	
	@Override
	public void checkForSpecialCollision(Solid other){
		if(other instanceof Airlock && location != null && vehicle != null){
			vehicle.leaveAirlock(this);
		}
		if(other instanceof Lander && location != null){
			location.enterLander(this, (Lander) other);
		}
	}

	public void setPiloting(boolean b) {
		piloting = b;
	}

	public Ship getVehicle() {
		return vehicle;
	}
	
	public Ship setVehicle(Ship vehicle) {
		this.vehicle = vehicle;
		return vehicle;
	}
	
	public Environment environment(){
		if(vehicle != null)
			return vehicle;
		if(location != null)
			return location;
		return S.galaxy;
	}
	
	public Grid grid(){
		return environment().getGrid();
	}

	public void tryPickUp() {
		Thing pickedUp = environment().findPickUpAbleWithinRange(this, 12);
		if(pickedUp != null){
			environment().cDelete(pickedUp);
			addToInventory(pickedUp);
		}
	}

	@Override
	public double getDrawRotation() {
		return 0;
	}

	@Override
	public double getDrawScale() {
		return 1;
	}
}
