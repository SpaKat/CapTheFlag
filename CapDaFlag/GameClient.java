
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class GameClient extends Thread {
	
	public Player player = new Player();
	public String ip;
	public FlagLocations flagLocations;
	public PlayerStats playerStats;
	public Home homelocation;
	public OtherPlayers otherPlayers;
	
	public GameClient(String ip,boolean blueteam) {
		this.ip = ip;
		player.setBlueteam(blueteam);
		this.start();
	}
	@Override
	public void run() {
		try {
			Socket s = new Socket(ip,8008);
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			double heading = 0;
			out.writeObject(player);
			out.writeObject(new Stats());
			out.reset();
			boolean running = true;
			while(running){
				player.setHeading(heading);
				out.writeObject(new Stats());
				out.reset();
				Thread.sleep(3);
				Message message = (Message) in.readObject();
				int id = message.getID();
				if (id == 1) {
					player = (Player) message;
				}
				if (id == 10) {
					noDefend tempP = (noDefend) message;
					player.setDefensive(tempP.isDefensive());
				}
				if (id == 3) {
					Stats stats = (Stats) message;
					flagLocations = stats.getFl();
					playerStats = stats.getPs();
					homelocation = stats.getHomebase();
					otherPlayers = stats.getOtherPlayers();
				//	System.out.println(otherPlayers.getOtherPlayers().length);
					heading = mycontrols(heading);
					out.writeObject(player);
					out.reset();
				}
				
			}

			s.close();
			// print Death
		} catch (Exception e) {

		}
	}

	public double mycontrols(double heading) {
		player.setDefensive(true);
		return heading+Math.PI*.0005;
	}
	

}



















