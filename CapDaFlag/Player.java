

import javafx.scene.shape.Circle;

public class Player extends Message  {

	// false = offensive
	private boolean defensive = true; 
	private double heading = 0;
	private boolean blueteam = true; 
	public Player() {
		setID(1);
	}
	public void changeType(){
		defensive = !defensive;
	}
	@Override
	public String toString() {
		return "TEST";
	}
	
	public double getHeading() {
		return heading;
	}
	public void setHeading(double heading) {
		this.heading = heading;
	}
	public boolean isDefensive() {
		return defensive;
	}
	public void setDefensive(boolean defensive) {
		this.defensive = defensive;
	}
	public boolean isBlueteam() {
		return blueteam;
	}
	public void setBlueteam(boolean blueteam) {
		this.blueteam = blueteam;
	}
}
