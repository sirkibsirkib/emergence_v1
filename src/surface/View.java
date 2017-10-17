package surface;

import space.SpaceShip;
import abstracts.Thing;


public class View {
	private Thing following;
	
	public View(Thing following){
		this.following = following;
	}

	void setToFollow(Thing f){
		following = f;
	}
	
	public double getX(){
		if(following != null)
			return following.xCenter() + following.getX();
		return 0;
	}
	
	public double getY(){
		if(following != null)
			return following.yCenter() + following.getY();
		return 0;
	}

	public Thing getFollowing() {
		return following;
	}

	public double getMoveSpeed() {
		if(following instanceof SpaceShip)
			return ((SpaceShip) following).getSpeed();
		return 0;
	}
}
