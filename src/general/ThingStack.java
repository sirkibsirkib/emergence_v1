package general;

import abstracts.Thing;

public class ThingStack {
	private Thing myThing;
	private int number;
	
	public ThingStack(Thing thing, int number){
		this.myThing = thing.copy();
		this.number = number;
	}
	
	public boolean tryAddToStack(Thing o, int number){
		if(thingsMatch(o)){
			this.number += number;
			return true;
		}
		return false;
	}
	
	public boolean thingsMatch(Thing o){
		return o.getName().equals(myThing.getName());
	}
	
	public int getNumber(){
		return number;
	}
	
	public void remove(int number){
		this.number -= number;
	}

	public String getThingName() {
		return myThing.getName();
	}

	public Thing getThingCopy() {
		return myThing.copy();
	}
}
