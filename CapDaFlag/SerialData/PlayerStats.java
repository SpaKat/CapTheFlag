package SerialData;

import javafx.beans.property.SimpleDoubleProperty;

public class PlayerStats extends Message {

	private SimpleDoubleProperty x = new SimpleDoubleProperty();
	private SimpleDoubleProperty y = new SimpleDoubleProperty();
	private boolean blueteam;
	public PlayerStats(boolean blueteam) {
		setID(4);
		this.blueteam = blueteam;
	}
	public SimpleDoubleProperty getX() {
		return x;
	}
	public SimpleDoubleProperty getY() {
		return y;
	}
	public double getXvalue() {
		return x.doubleValue();
	}
	public double getYvalue() {
		return y.doubleValue();
	}
	public boolean isBlueteam() {
		return blueteam;
	}
}
