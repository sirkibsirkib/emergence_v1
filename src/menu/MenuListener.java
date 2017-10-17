package menu;

import general.S;

public abstract class MenuListener {
	static void listen(MenuOption in){
		String id = in.getId();
		String code = in.getCode();
		double value = in.getValue();
		Object ref = in.getRef();
		
		switch(id){
		case "return":	S.menu = null;				break;
		case "ask":		S.menu = areYouSure(code);	break;
		case "exit":	System.exit(0);				break;
		}
	}

	private static Menu areYouSure(String code) {
		MenuOption oNotSure = new MenuOption("No", "return", "", 0, null);
		MenuOption oYesSure = new MenuOption("Yes", code, "", 0, null);
		return new Menu("Are you sure?", oNotSure, oYesSure);
	}
}
