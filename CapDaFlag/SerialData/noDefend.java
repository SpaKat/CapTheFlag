package SerialData;

public class noDefend extends Message {

	private boolean b = false;
	
	public noDefend() {
		setID(10);
	}
	
	public boolean isDefensive() {
		return b;
	}
	
}
