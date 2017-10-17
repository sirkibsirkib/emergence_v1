package general;

import java.util.ArrayList;
import java.util.List;

import surface.View;
import abstracts.Coordinate;
import abstracts.Solid;
import abstracts.Thing;
import categories.PickUpAble;

public class Grid {
	private List<Thing> ls;
	
	public Grid(){
		ls = new ArrayList<>();
	}
	
	public boolean remove(Thing e){
		return ls.remove(e);
	}
	
	public Thing add(Thing e){
		ls.add(e);
		return e;
	}
	
	public void addSolid(Solid so) {
		int offset = 5;
		boolean settled = findCollision(so, false) == null;
		while(!settled){
			so.offsetX(offset);
			settled = findCollision(so, false) == null;
			if(!settled){//r
				so.offsetX(-offset);
				so.offsetY(offset);
				settled = findCollision(so, false) == null;
			}
			if(!settled){//r
				so.offsetY(-offset);
				so.offsetX(-offset);
				settled = findCollision(so, false) == null;
			}
			if(!settled){//r
				so.offsetX(offset);
				so.offsetY(-offset);
				settled = findCollision(so, false) == null;
			}
			if(!settled)
				so.offsetY(offset);
			offset *= 2;
		}
		add(so);
	}
	
	public Thing find(double x, double y){
		for(int i = 0; i < ls.size(); i++){
			Thing next = ls.get(i);
			if(next.getX() == x && next.getY() == y)
				return next;
		}
		return null;
	}
	
	public boolean contains(Thing e){
		return ls.contains(e);
	}
	
	public int overWriteAt(Thing e, double x, double y){
		int foundCount = 0;
		Thing next = find(x, y);
		while(next != null){
			foundCount++;
			remove(next);
			next = find(x, y);
		}
		add(e);
		return foundCount;
	}
	
	public int size(){
		return ls.size();
	}
	
	public Thing get(int index){
		return ls.get(index);
	}
	
	public void clear(){
		ls.clear();
	}

	public Solid findCollision(Solid finding, boolean triggering) {
		if(!finding.isSolid())
			return null;
		for(int i = 0; i < ls.size(); i++){
			Thing found = ls.get(i);
			if(found instanceof Solid && found != finding){
				Solid foundSolid = (Solid) found;
				if(finding.collidesWith(foundSolid) && foundSolid.isSolid()){
					if(triggering){
						foundSolid.checkForSpecialCollision(foundSolid);
						finding.checkForSpecialCollision(foundSolid);
					}
					return foundSolid;
				}
			}
		}
		return null;
	}
	
	public Thing findPickUpAbleWithinRange(Thing x, double range) {
		for(int i = 0; i < ls.size(); i++){
			Thing next = ls.get(i);
			if(next instanceof PickUpAble && next != x && x.distanceTo(next) < range)
				return next;
		}
		return null;
	}
	
	public boolean floatThingToEndOfList(Thing x){
		boolean found = ls.contains(x);
		if(!found) return false;
		ls.remove(x);
		ls.add(x);
		return true;
	}
	
	public void swapWithNext(int index){
		Thing get = get(index+1);
		ls.remove(index+1);
		ls.add(index, get);
	}
	
	public void draw(View v, boolean flatProjection){
		for(Thing x : ls)
			x.draw(v, flatProjection);
	}
	
	public boolean thingWithin(Coordinate x, int range){
		for(int i = 0; i < ls.size(); i++){
			if(ls.get(i).distanceTo(x) < range)
				return true;
		}
		return false;
	}
}
