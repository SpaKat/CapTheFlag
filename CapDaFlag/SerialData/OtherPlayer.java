package SerialData;
import java.io.Serializable;

public class OtherPlayer implements Serializable {


	private double X;
	private double Y;
	private boolean blueteam;
	private boolean defender;
	public OtherPlayer( double x , double y,boolean blueteam,boolean defender) {
		X = x;
		Y = y;
		this.blueteam =blueteam;
		this.defender = defender;
	}
	public double getX() {
		return X;
	}
	public double getY() {
		return Y;
	}
	public boolean isBlueteam() {
		return blueteam;
	}
	public boolean isDefender() {
		return defender;
	}
}
