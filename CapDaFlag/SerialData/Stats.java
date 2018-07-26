package SerialData;

public class Stats extends Message {

	private FlagLocations fl;
	private PlayerStats ps;
	private Home homebase;
	public Stats() {
		setID(3);
	}
	public Stats(FlagLocations fl,PlayerStats ps,Home home) {
		this();
		this.fl = fl;
		this.ps = ps;
		this.homebase = home;
	}
	
	

	public FlagLocations getFl() {
		return fl;
	}
	public PlayerStats getPs() {
		return ps;
	}
	public Home getHomebase() {
		return homebase;
	}
}
