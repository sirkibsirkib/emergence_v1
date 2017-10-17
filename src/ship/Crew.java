package ship;

import java.util.ArrayList;
import java.util.List;

public class Crew {
	private List<Human> members;
	private int selected;
	
	public Crew(){
		members = new ArrayList<>();
	}
	
	public Human addMember(Human member){
		members.add(member);
		return member;
	}
	
	public int size(){
		return members.size();
	}
	
	public Human get(int index){
		return members.get(index);
	}
	
	public void removeMember(int index){
		members.remove(index);
		if(selected < members.size()-1 && selected > 0)
			selected--;
	}
	
	public Human getSelected(){
		if(members.size() > 0)
			return members.get(selected);
		else
			return null;
	}
	
	public void selectDown(){
		selected--;
		if(selected < 0)
			selected = members.size()-1;
	}
	
	public void selectUp(){
		selected++;
		if(selected >= members.size())
			selected = 0;
	}

	public boolean removeMember(Human h) {
		return members.remove(h);
	}

	public boolean select(int digit) {
		if(members.size() > digit){
			selected = digit;
			return true;
		}
		return false;
	}
}
