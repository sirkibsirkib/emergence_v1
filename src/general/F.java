package general;

public abstract class F {
	public static double rdg(double max) {
		return Math.random()*max;
	}
	
	public static int snap(double d, int grid){
		return (int)(d)/grid*grid;
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
	
	public static double distance(double x1, double y1, double x2, double y2){
		return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
	}
	
	public static double dirFromTo(double x1, double y1, double x2, double y2){
		return dirTo(x2-x1, y2-y1);
	}
	
	public static double dirTo(double x, double y){
		double result =  Math.atan(y / x);
		if(x < 0)
			return Math.PI-result;
		else{
			if(y < 0)//q1
				return -result;
			else//q4
				return Math.PI*2-result;
		}
	}

	public static int radToDeg(double radians){
		return (int)(radians/Math.PI/2*360);
	}
	
	public static double sqr(double x){
		return x*x;
	}

	public static int desaturate(int col, double retention) {
		int r = (col & 0xff0000)>>(4*4);
		int g = (col & 0x00ff00)>>(2*4);
		int b = col & 0x0000ff;
		
		int r2 = (int)(r*retention) + (int)(0xff*retention/2);
		int g2 = (int)(g*retention) + (int)(0xff*retention/2);
		int b2 = (int)(b*retention) + (int)(0xff*retention/2);
		return (r2)<<(4*4) | (g2)<<(2*4) | b2;
	}
	
	public static double dec(double d){
		return (int)(d*10)/10D;
	}
	
	public static double abs(double x){
		return Math.abs(x);
	}

	public static double dirDifference(double sourceA, double targetA) {
		double a = targetA - sourceA;
		if(a > Math.PI)	a -= Math.PI*2;
		else if(a < -Math.PI) a += Math.PI*2;
		return a;
	}

	public static int min(int... x) {
		int min = Integer.MAX_VALUE;
		for(int i = 0; i < x.length; i++)
			if(x[i] < min)
				min = x[i];
		return min;
	}

	public static int max(int... x) {
		int max = Integer.MIN_VALUE;
		for(int i = 0; i < x.length; i++)
			if(x[i] > max)
				max = x[i];
		return max;
	}

	public static int mod(int i, int j) {
		return Math.abs(i%j);
	}

	public static int avg(int... x) {
		if(x.length == 0)
			return 0;
		int sum = 0;
		for(int i = 0; i < x.length; i++)
			sum += x[i];
		return sum/x.length;
	}

	public static double nPow(double a, double b) {
		boolean neg = a < 0;
		if(neg)
			return -Math.pow(-a, b);
		return Math.pow(a, b);
	}
}
