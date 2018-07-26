package Server;

import javafx.scene.shape.Circle;

public class ToggledCircle extends Circle {
	private boolean taken= false;
	
	public ToggledCircle(int i) {
		super(i);
	}
	public void setTaken(boolean taken) {
		this.taken = taken;
	}
	public boolean isTaken() {
		return taken;
	}
}
