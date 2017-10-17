package game;

public abstract class F {
	public static double rdg(double max) {
		return Math.random()*max;
	}
	

	
	public static int snap(int input, int grid){
		return input/grid*grid;
	}

	public static int grayFromDouble(double i){
		if(i > 1) return 0xffffff;
		if(i < 0) return 0x000000;
		
		int part = (int)(i * 0xff);
		int result = part + (part<<8) + (part<<16);
		return result;
	}
	
	public static int rng(int i) {
		return (int)(Math.random()*i);
	}
	

	
	
	public static double manhattan(double x1, double y1, double x2, double y2){
		return Math.abs(x1-x2) + Math.abs(y1-y2);
	}
}
