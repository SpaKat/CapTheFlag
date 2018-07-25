package SerialData;

public class Stats extends Message {

	private FlagLocations fl;
	private PlayerStats ps;
	public Stats() {
		setID(3);
	}
	public Stats(FlagLocations fl,PlayerStats ps) {
		this();
		this.fl = fl;
		this.ps = ps;
	}
	
	

	public FlagLocations getFl() {
		return fl;
	}
	public PlayerStats getPs() {
		return ps;
	}
}
