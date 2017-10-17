package abstracts;

import general.ChangeLog;
import general.Grid;
import ship.Crew;
import ship.Human;
import surface.View;

public abstract class Environment {
	protected Grid grid;
	protected Crew crew;
	protected ChangeLog cl;
	protected View v;
	
	public Environment(ChangeLog cl){
		grid = new Grid();
		crew = new Crew();
		this.cl = cl;
	}
	
	public void cDelete(Thing f) {
		cl.newChange(f.getX(), f.getY(), 0);
		grid.remove(f);
	}
	
	public Grid getGrid(){
		return grid;
	}
	
	public void add(Thing t){
		grid.add(t);
	}
	
	public void clear() {
		grid.clear();
	}
	
	public View getView(){
		return v;
	}
	
	public void addCrewMember(Human h) {
		crew.addMember(h);
		grid.addSolid(h);
	}
	
	public void removeCrewmember(Human h) {
		crew.removeMember(h);
		grid.remove(h);
	}
	
	public Thing findPickUpAbleWithinRange(Thing x, double range){
		return grid.findPickUpAbleWithinRange(x, range);
	}
	
	public void setView(Thing t){
		v = new View(t);
	}
}
