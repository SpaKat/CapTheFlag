

public class OtherPlayers extends Message {

	private OtherPlayer[] otherPlayers;
	
	public OtherPlayers( OtherPlayer[] otherPlayers) {
		this.otherPlayers =  otherPlayers;
	}
	
	public OtherPlayer[] getOtherPlayers() {
		return otherPlayers;
	}
	
}
