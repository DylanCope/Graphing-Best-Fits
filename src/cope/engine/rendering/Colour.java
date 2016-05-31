package cope.engine.rendering;

public class Colour {
	
	public static final Colour WHITE    = new Colour(255, 255, 255);
	public static final Colour BLACK    = new Colour(0, 0, 0);
	public static final Colour RED      = new Colour(255, 0, 0);
	public static final Colour GREEN    = new Colour(0, 225, 0);
	public static final Colour BLUE     = new Colour(0, 0, 255);
	public static final Colour CYAN     = new Colour(0, 255, 255);
	public static final Colour GREY     = new Colour(100, 100, 100);
	public static final Colour DARKGREY = new Colour(50, 50, 50);
	private int[] values;
	
	public Colour(int r, int g, int b) {
		values = new int[] {r, g, b, 255};
	}
	
	public Colour(int r, int g, int b, int a) {
		values = new int[] {r, g, b, a};
	}
	
	public Colour add(Colour b) {
		return new Colour(b.getR() + values[0], b.getG() + values[1],
				b.getB() + values[2], b.getA() + values[3]);
	}
	
	public Colour invert() {
		return new Colour(255 - values[0], 255 - values[1], 255 - values[2]);
	}
	
	public int getR() {
		return values[0];
	}
	
	public int getG() {
		return values[1];
	}
	
	public int getB() {
		return values[2];
	}
	
	public int getA() {
		return values[3];
	}
	
	public void setR(int r) {
		values[0] = r;
	}
	
	public void setG(int g) {
		values[1] = g;
	}
	
	public void setB(int b) {
		values[2] = b;
	}
	
	public void setA(int a) {
		values[3] = a;
	}
	
	public String toString() {
		return "(" + values[0] + ", " + values[1] + ", " 
				+ values[2] + ", " + values[3] +")";
	}
	
}
