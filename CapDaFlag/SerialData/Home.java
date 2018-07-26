package SerialData;

public class Home extends Message {

	
	private double RedStartX;
	private double RedStartY;
	private double BlueStartY;
	private double BlueStartX;
	public Home(double rx,double ry, double bx,double by) {
		this();
		RedStartX = rx;
		RedStartY = ry;
		BlueStartY = by;
		BlueStartX = bx;
	}
	public Home() {
		setID(5);
	}
	public double getBlueStartX() {
		return BlueStartX;
	}
	public double getBlueStartY() {
		return BlueStartY;
	}
	public double getRedStartX() {
		return RedStartX;
	}
	public double getRedStartY() {
		return RedStartY;
	}
	
}
