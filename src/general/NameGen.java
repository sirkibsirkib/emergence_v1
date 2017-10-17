package general;

import java.util.Random;

public abstract class NameGen {
	private static Random r;
	private static StringBuilder s;
	private static final char[] vowels = {'a', 'a', 'a', 'a', 'e', 'e', 'e', 'e', 'i', 'i', 'i', 'o', 'o', 'o', 'u', 'u', 'y'};
	private static final char[] consonants = {'b', 'b', 'b', 'c', 'c', 'd', 'f', 'g', 'h', 'j', 'j',
											'k', 'l', 'm', 'm', 'n', 'n', 'p', 'p', 'q', 'r', 'r', 's', 's',
											't', 't', 'v', 'w', 'x', 'z'};
	
	public static String get(int seed){
		r = new Random(seed);
		s = new StringBuilder();
		if(chance(seed%2)) c();
		int syllables = rng(3)+2;
		for(int i = 0; i < syllables; i++){
			v();
			if(rng(5) != 0)
				c();
		}
		if(s.length() <= 3)
			return get(rng(seed + 20));
			
		s.setCharAt(0, Character.toUpperCase(s.charAt(0)));
		return s.toString();
	}
	
	private static void c(){
		char next = consonants[rng(consonants.length)];
		s.append(next);
		if(chance(4))
			staple(next);
	}
	
	private static void v(){
		char next = vowels[rng(vowels.length)];
		s.append(next);
	}
	
	private static int rng(int range){
		return (int) (r.nextDouble()*range);
	}
	
	private static boolean chance(int sides){
		return rng(sides) == 0;
	}
	
	private static void staple(char c){		
		switch(c){
		case 'a':	s.append('e');	return;
		case 'e':	s.append('e');	return;
		case 'i':	s.append('o');	return;
		case 'o':	s.append('u');	return;
		case 'u':	s.append('a');	return;
		}
		
		switch(c){
		case 's':
		case 't':
		case 'k':
		case 'c':{
			if(chance(2))	s.append('h');
			else			s.append('r');	return;
		}
		case 'r':
		case 'p':
		case 'z':	s.append('h');	return;
		case 'd':	s.append('g');	return;
		case 'l':	s.append('l');	return;
		}
	}
}
