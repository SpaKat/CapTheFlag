

public class PlayerStats extends Message {

	private double x;
	private double y;
	private boolean blueteam;
	public PlayerStats(double x, double y,boolean blueteam) {
		setID(4);
		this.x = x;
		this.y = y;
		this.blueteam = blueteam;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public boolean isBlueteam() {
		return blueteam;
	}
}
