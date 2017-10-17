package space;

import general.F;
import general.S;
import gfx.Screen;

import java.text.DecimalFormat;

import ship.Ship;
import surface.View;
import abstracts.Coordinate;
import abstracts.Thing;

public class SpaceShip extends Thing{
	private double direction, xSpeed, ySpeed, turnSpeed, displayX, displayY;
	private double boostForce = 0.4;
	private double turnForce = 0.008;
	private boolean resetSpriteCode = false,
			killEngines = false;
	private Ship ship;
	char spriteCode = '0';
	
	public SpaceShip(Ship ship, double x, double y){
		super(x, y);
		displayX = x;
		displayY = y;
		this.ship = ship;
		direction = 1;
		xSpeed = 0.001;
		ySpeed = 0;
		turnSpeed = 0;
	}
	
	public Ship getShip(){
		return ship;
	}
	
	private void updateDisplayXY(){
		double locDistortion = F.sqr(getSpeed()/3);
		displayX = (int) (getX()/40);
		displayY = (int) (getY()/40);
		if(locDistortion > .5){
			displayX += (F.rdg(locDistortion*2) - locDistortion);
			displayY += (F.rdg(locDistortion*2) - locDistortion);
		}
		displayX = F.dec(displayX);
		displayY = F.dec(displayY);
	}
	public void setKillEngines(boolean killed) {
		killEngines = killed;
	}
	
	public boolean getKillEngines(){
		return killEngines;
	}
	
	public String getDisplayX(){
		DecimalFormat formatter = new DecimalFormat("#,###.0");
		String result = formatter.format(displayX);
		while(result.length() < 12)
			result = ' ' + result;
		return result;
	}
	
	public String getDisplayY(){
		DecimalFormat formatter = new DecimalFormat("#,###.0");
		String result = formatter.format(displayY);
		while(result.length() < 12)
			result = ' ' + result;
		return result;
	}
	
	public void speedUp(double change){
		if(ship.getFuel() <= 0)
			return;
		xSpeed += Math.cos(direction) * change;
		ySpeed -= Math.sin(direction) * change;
		setSpriteCode('w');
		ship.decreaseFuel(.01);
	}
	
	public void slowDown(double change){
		if(ship.getFuel() <= 0)
			return;
		speedUp(-change);
		setSpriteCode('s');
		ship.decreaseFuel(.01);
	}
	
	public void tick(){
		direction += turnSpeed;
		if(killEngines){
			if(getSpeed() > .25)
				stopAssist();
			else
				killEngines = false;
		}
			
		x += xSpeed;
		y += ySpeed;
		wrapDirectionAroundCircle();
		if(S.game.ticks % 30 == 0)
			updateDisplayXY();
		if(resetSpriteCode)
			setSpriteCode('0');
		else
			resetSpriteCode = true;
	}

	private void wrapDirectionAroundCircle() {
		if(direction > Math.PI*2)
			direction -= Math.PI*2;
		if(direction < 0)
			direction += Math.PI*2;
	}
	
	public void turnAssist(){
		double dMotion = F.dirTo(xSpeed, ySpeed);
		double dif = direction-dMotion;
		if(dif < 0) dif += Math.PI*2;
		else if(dif > Math.PI*2) dif -= Math.PI*2;
		
		if(turnSpeed > 0)
			turnRight(turnSpeed >= turnForce ? turnForce : turnSpeed);
		else if(turnSpeed < 0)
			turnLeft(turnSpeed <= -turnForce ? turnForce : -turnSpeed);
	}
	
	public void stopAssist(){
		if(getSpeed() < .1) return;
		double dif = F.dirDifference(direction, directionOfMotion());
		
		double dirSpe = desiredSpeed(dif);
		if(Math.abs(dif) < .2)
			if(Math.abs(turnSpeed) > .02)
				turnAssist();
			else
				slowDown(boostForce);
		else if(turnSpeed > dirSpe)
			turnRight(turnForce);
		else if(turnSpeed < dirSpe)
			turnLeft(turnForce);
	}

	private double desiredSpeed(double dif) {
		return dif/10;
	}
	
	private void setSpriteCode(char newCode){
		resetSpriteCode = false;
		spriteCode = newCode;
	}
	
	private double directionOfMotion(){
		return F.dirTo(xSpeed, ySpeed);
	}

	public void turnLeft(double d) {
		if(ship.getFuel() <= 0 || turnSpeed > .2)
			return;
		turnSpeed += d;
		setSpriteCode('a');
		ship.decreaseFuel(.006);
	}
	
	public void turnRight(double d) {
		if(ship.getFuel() <= 0 || turnSpeed < -.2)
			return;
		turnSpeed -= d;
		setSpriteCode('d');
		ship.decreaseFuel(.006);
	}

	public double getDirection() {
		return direction;
	}
	
	public int getSpriteCode(){
		return spriteCode;
	}

	public void stop() {
		xSpeed = 0;
		ySpeed = 0;
	}

	public double getXSpeed() {
		return xSpeed;
	}
	
	public double getYSpeed() {
		return ySpeed;
	}

	public double getSpeed() {
		return F.distance(0, 0, xSpeed, ySpeed);
	}

	public void accelerateToward(SpacePlanet near, double acc) {
		double dir = F.dirFromTo(x, y, near.getX(), near.getY());
		xSpeed += Math.cos(dir)*acc;
		ySpeed += -Math.sin(dir)*acc;
	}
	
	public void fallToward(SpacePlanet near) {
		orient(0.01);
		accelerateToward(near, near.getRadius()/9_000D);
	}

	private void orient(double d) {
		double dMotion = F.dirTo(xSpeed, ySpeed);
		double dif = F.dirDifference(direction, dMotion);
		
		double dirSpe = desiredSpeed(dif);
		if(Math.abs(dif) > .2){
			if(turnSpeed > dirSpe)
				direction -= d;
			else if(turnSpeed < dirSpe)
				direction += d;
		}
	}

	public void noSlowerThan(double d) {
		if(getSpeed() < d){
			killEngines = false;
			speedUp(boostForce);
		}
	}
	
	public double getBoostForce() {
		return boostForce;
	}

	public void setBoostForce(double boostForce) {
		this.boostForce = boostForce;
	}

	public double getTurnForce() {
		return turnForce;
	}

	public void setTurnForce(double turnForce) {
		this.turnForce = turnForce;
	}

	public double getTurnSpeed() {
		return turnSpeed;
	}

	public int getSpriteSheetX() {
		return 0;
	}

	public int getSpriteSheetY() {
		return 0;
	}

	public Thing copy() {
		return new SpaceShip(ship, x, y);
	}
	
	public Coordinate getStopCoordinate(){
		double stopTime = getSpeed() / boostForce;
		double stopDistance = stopTime * getSpeed() / 2;
		Coordinate result = getRelative(directionOfMotion(), stopDistance);
		return result;
	}
	
	public void draw(View v, boolean flatProjection){
		Screen.drawShip(this, getDirection(), 1, v);
	}

	public double getDrawRotation() {
		return 0;
	}

	public double getDrawScale() {
		return 1;
	}
}
