package general;

public final class Colour {
	private int r, g, b;
	public static final Colour BLACK = new Colour(0, 0, 0);
	public static final Colour WHITE = new Colour(255, 255, 255);

	public Colour(int r, int g, int b){
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Colour(int hex){
		setHex(hex);
	}
	
	public int getHex(){
		return (r<< (4*4)) | (g<< (2*4)) | b;
	}
	
	public int getR() {
		return r;
	}
	
	public void setHex(int hex){
		r = hex & 0xff0000 >> (4*4);
		g = hex & 0x00ff00 >> (2*4);
		b = hex & 0x0000ff;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}
	
	public Colour darken(double retention){
		return new Colour((int) (r*retention), (int) (g*retention), (int) (b*retention));
	}

	public Colour average(Colour o) {
		int newR = F.avg(r, o.getR());
		int newG = F.avg(g, o.getG());
		int newB = F.avg(b, o.getB());
		return new Colour(newR, newG, newB);
	}
	
//	private void print(String name){
//		System.out.printf("%s\nR: %d\nG: %d\nB: %d\n\n", name, r, b, g);
//	}
}
