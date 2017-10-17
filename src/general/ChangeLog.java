package general;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ChangeLog {
	private List<Change> changes;
	private char category;
	private int xId, yId;
	private boolean altered;
	
	public ChangeLog(char category, int xId, int yId){
		this.xId = xId;
		this.yId = yId;
		this.category = category;
		changes = new ArrayList<>();
		altered = false;
	}
	
	public void newChange(double x, double y, int data){
		Change found = getChangeAt(x, y);
		if(found == null)
			changes.add(new Change(x, y, data));
		else
			found.data = data;
		altered = true;
	}
	
	public boolean matches(char category, int xId, int yId){
		return this.category == category && xId == this.xId && yId == this.yId;
	}
	
	public int getDataAt(double x, double y){
		Change found = getChangeAt(x, y);
		if(found != null)
			return found.data;
		return -1;
	}
	
	public boolean getAltered(){
		return altered;
	}
	
	public void hasBeenSaved(){
		altered = false;
	}

	public void saveToFile(String savePath) {
		File file = new File(savePath + "cl_" + category + '_' + xId + '_' + yId + ".txt");
		PrintWriter writer = null;
		
		try {writer = new PrintWriter(file, "UTF-8");}
		catch (FileNotFoundException e) {/*System.out.println("FILE NOT FOUND");*/}
		catch (UnsupportedEncodingException e) {/*System.out.println("ENCODING UNSUPPORTED");*/}
		
		for(int i = 0; i < changes.size(); i++)
			writeChangeToFile(writer, changes.get(i));
		writer.close();
		altered = false;
	}

	//private
	
	private void writeChangeToFile(PrintWriter writer, Change change) {
		writer.println(change.x + "\t" + change.y + "\t" + change.data);
	}
	
	private Change getChangeAt(double d, double e){
		for(int i = 0; i < changes.size(); i++){
			Change next = changes.get(i);
			if(next.isAt(d, e))
				return next;
		}
		return null;
	}
	
	public int numberOfChanges() {
		return changes.size();
	}
	
	private class Change{
		double x;
		double y;
		private int data;
		
		public Change(double d, double e, int data){
			this.x = d;
			this.y = e;
			this.data = data;
		}

		public boolean isAt(double d, double e) {
			return d == this.x && e == this.y;
		}
	}
}
