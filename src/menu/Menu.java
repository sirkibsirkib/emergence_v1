package menu;

import general.S;
import gfx.Screen;
import gfx.StringWriter;

import java.util.ArrayList;
import java.util.List;

public class Menu {
	private String prompt;
	private List<MenuOption> options;
	private int hovering;
	
	
	public Menu(String prompt, MenuOption... opts){
		this.prompt = prompt;
		options = new ArrayList<>();
		for(MenuOption x : opts)
			options.add(x);
		hovering = 0;
	}
	
	public void render(){
		Screen.darkenBox(28, 18, 30+(longestOptionText()*6), 30 + (options.size()*8));
		StringWriter.writeStringToScreen(prompt, 30, 20, 0xdddddd);
		for(int i = 0; i < options.size(); i++)
			renderOption(options.get(i), i);
	}
	
	private int longestOptionText(){
		int length = prompt.length();
		for(int i = 0; i < options.size(); i++){
			MenuOption next = options.get(i);
			int nextLength = next.getText().length() + 3;
			if(nextLength > length)
				length = nextLength;
		}
		return length;
	}

	private void renderOption(MenuOption o, int vNumber) {
		int colour = vNumber == hovering ? 0x0022ff : 0xffffff;
		StringWriter.writeStringToScreen((vNumber+1) + "> " + o.getText(), 30, 30 + 8*vNumber, colour);
	}

	public void upPressed() {
		if(hovering > 0)
			hovering--;
	}
	
	public void downPressed() {
		if(hovering < options.size()-1)
			hovering++;
	}

	public void enter() {
		MenuListener.listen(options.get(hovering));
	}

	public void jumpToOption(int digit) {
		System.out.println("digit:  " + digit);
		if(digit < options.size()){
			hovering = digit;
			S.focusViews();
		}
	}
}
