package ship;

import java.util.ArrayList;
import java.util.List;

public class Fleet {
	List<Ship> ships;
	
	public Fleet(){
		ships = new ArrayList<>();
	}
	
	public int size(){
		return ships.size();
	}
	
	public Ship get(int i){
		return ships.get(i);
	}
	
	public Ship add(Ship x){
		ships.add(x);
		return x;
	}
}
