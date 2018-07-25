package SerialData;

public class FlagLocations extends Message {

	private double RedX;
	private double RedY;
	private double BlueY;
	private double BlueX;
	public FlagLocations(double rx,double ry, double bx,double by) {
		this();
		RedX = rx;
		RedY = ry;
		BlueY = by;
		BlueX = bx;
	}
	public FlagLocations() {
		setID(2);
	}
	public double getBlueX() {
		return BlueX;
	}
	public double getBlueY() {
		return BlueY;
	}
	public double getRedX() {
		return RedX;
	}
	public double getRedY() {
		return RedY;
	}
}
