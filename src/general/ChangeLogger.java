package general;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChangeLogger {
	private List<ChangeLog> logs;
	private static final String SAVE_PATH = "C:/Users/Christopher/Desktop/emergenceSaves/";
	
	public ChangeLogger(){
		logs = new ArrayList<>();
	}
	
	public ChangeLog getLogAt(char category, int x, int y){
		//try memory
		for(int i = 0; i < logs.size(); i++){
			ChangeLog next = logs.get(i);
			if(next.matches(category, x, y))
				return next;
		}
		//try savefile
		try{
			String fileName = "cl_" + category + "_" + x + "_" + y + ".txt";
			return loadFromFile(fileName, category, x, y);
		}catch(IOException e){
			//resort to new cl
			return addLog(new ChangeLog(category, x, y));
		}
	}

	public int numberOfLogs(){
		return logs.size();
	}
	
	public void saveAlteredLogs(){
		for(int i = 0; i < logs.size(); i++){
			ChangeLog next = logs.get(i);
			if(next.getAltered())
				next.saveToFile(SAVE_PATH);
		}
	}
	
	//private
	
	private ChangeLog addLog(ChangeLog cl){
		logs.add(cl);
		return cl;
	}
	
	private ChangeLog loadFromFile(String fileName, char category, int x, int y) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(SAVE_PATH+fileName));
		String everything;
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        everything = sb.toString();
	    } finally {
	        br.close();
	    } 
//	    System.out.println("loading from " + fileName);
	    return buildCLFromString(everything, category, x, y);
	    
	}

	private ChangeLog buildCLFromString(String fileContents, char category, int x, int y) {
		ChangeLog loaded = new ChangeLog(category, x, y);
		Scanner fileContentsScanner = new Scanner(fileContents);
		while(fileContentsScanner.hasNext()){
			loaded.newChange(fileContentsScanner.nextDouble(), fileContentsScanner.nextDouble(), fileContentsScanner.nextInt());
		}
		fileContentsScanner.close();
		return loaded;
	}
}
