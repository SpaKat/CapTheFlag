package SerialData;

public class Stats extends Message {

	private FlagLocations fl;
	private PlayerStats ps;
	private Home homebase;
	private OtherPlayers otherPlayers;
	public Stats() {
		setID(3);
	}
	public Stats(FlagLocations fl,PlayerStats ps,Home home,OtherPlayers otherPlayers) {
		this();
		this.fl = fl;
		this.ps = ps;
		this.homebase = home;
		this.otherPlayers = otherPlayers;
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
	public OtherPlayers getOtherPlayers() {
		return otherPlayers;
	}
}
