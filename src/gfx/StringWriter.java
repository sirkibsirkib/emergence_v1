package gfx;

public abstract class StringWriter {
	private static SpriteSheet sheet;
	
	public static void writeStringToScreen(String s, int screenX, int screenY, int colour){
		if(sheet == null)
			sheet = new SpriteSheet("/letters.png", 5);
		
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			
			if(Character.isWhitespace(c))
				continue;
			char charCode = charCode(c);
			switch(charCode){
			case '0':	writeDigit(c, screenX + (6*i), screenY, colour);		continue;
			case 'a':	writeLowercase(c, screenX + (6*i), screenY, colour);	continue;
			case 'A':	writeUppercase(c, screenX + (6*i), screenY, colour);	continue;
			case '?':	writePunctuation(c, screenX + (6*i), screenY, colour);	continue;
			}
		}
	}
	
	private static void writeLowercase(char c, int screenX, int screenY, int colour) {
		int sheetX = (c - 'a')*5;
		Screen.drawFromSheet(sheet, sheetX, 0, 5, 7, colour, screenX, screenY);
	}
	
	private static void writeUppercase(char c, int screenX, int screenY, int colour) {
		int sheetX = (c - 'A')*5;
		Screen.drawFromSheet(sheet, sheetX, 7, 5, 7, colour, screenX, screenY);
	}

	private static void writeDigit(char c, int screenX, int screenY, int colour) {
		int sheetX = (c - '0')*5;
		Screen.drawFromSheet(sheet, sheetX, 2*7, 5, 7, colour, screenX, screenY);
	}
	
	private static void writePunctuation(char c, int screenX, int screenY, int colour) {
		switch(c){
		case '-':	Screen.drawFromSheet(sheet, 10*5, 2*7, 5, 7, colour, screenX, screenY);	return;
		case '.':	Screen.drawFromSheet(sheet, 11*5, 2*7, 5, 7, colour, screenX, screenY);	return;
		case ':':	Screen.drawFromSheet(sheet, 12*5, 2*7, 5, 7, colour, screenX, screenY);	return;
		case '<':	Screen.drawFromSheet(sheet, 13*5, 2*7, 5, 7, colour, screenX, screenY);	return;
		case '>':	Screen.drawFromSheet(sheet, 14*5, 2*7, 5, 7, colour, screenX, screenY);	return;
		case '?':	Screen.drawFromSheet(sheet, 15*5, 2*7, 5, 7, colour, screenX, screenY);	return;
		case ',':	Screen.drawFromSheet(sheet, 16*5, 2*7, 5, 7, colour, screenX, screenY);	return;
		}
		throw new Error("UNKNOWN WRITE CHAR <" + c + ">");
		
	}

	private static char charCode(char c) {
		if('a' <= c && c <= 'z')
			return 'a';
		if('A' <= c && c <= 'Z')
			return 'A';
		if('0' <= c && c <= '9')
			return '0';
		return '?';
	}
}
