package game;

public class View {
	private Feature following;
	
	public View(Feature following){
		this.following = following;
	}

	void setToFollow(Feature f){
		following = f;
	}
	
	public int getX(){
		if(following != null)
			return following.getX();
		else
			return 0;
	}
	
	public int getY(){
		if(following != null)
			return following.getY();
		else
			return 0;
	}

	public Feature getFollowing() {
		return following;
	}
}
