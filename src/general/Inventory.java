package general;

import java.util.ArrayList;
import java.util.List;

import abstracts.Thing;
import menu.Menu;
import menu.MenuOption;

public class Inventory {
	private List<ThingStack> stacks;
	
	public Inventory(){
		stacks = new ArrayList<>();
	}
	
	public void add(Thing x){
		if(x == null)
			throw new Error("adding null to inventory");
		ThingStack s = findStackFor(x);
		if(s != null)
			s.tryAddToStack(x, 1);
		else
			stacks.add(new ThingStack(x, 1));
	}
	
	private ThingStack findStackFor(Thing x) {
		for(int i = 0; i < stacks.size(); i++){
			if(stacks.get(i).thingsMatch(x))
				return stacks.get(i);
		}
		return null;
	}

	public boolean remove(Thing x){
		ThingStack ts = findStackFor(x);
		if(ts != null){
			ts.remove(1);
			if(ts.getNumber() < 1)
				stacks.remove(ts);
			return true;
		}
		return false;
	}
	
	public void selectInventoryItem(){
		MenuOption[] options = new MenuOption[stacks.size()+1];
		options[0] = new MenuOption("<close>", "return", "", 0, null);
		for(int i = 0; i < stacks.size(); i++){
			ThingStack next = stacks.get(i);
			options[i+1] = new MenuOption(next.getNumber() + "x " + next.getThingName(), "invChoice", "", next.getNumber(), next.getThingCopy());
		}
		S.menu = new Menu("Inventory:", options);
	}
}
