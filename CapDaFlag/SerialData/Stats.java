package SerialData;

public class Stats extends Message {

	private FlagLocations fl;
	private PlayerStats ps;
	public Stats(FlagLocations fl,PlayerStats ps) {
		setID(3);
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
