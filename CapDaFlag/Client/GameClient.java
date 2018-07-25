package Client;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import SerialData.FlagLocations;
import SerialData.Message;
import SerialData.Player;
import SerialData.PlayerStats;
import SerialData.Stats;

public class GameClient extends Thread {

	public GameClient() {
		this.start();
	}
	private FlagLocations fl;
	private PlayerStats ps;
	@Override
	public void run() {
		try {
			Socket s = new Socket("127.0.0.1",8008);
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			Player p = new Player();
			
			
			double heading = Math.random()*2*Math.PI;
			
			
			
			
			for (int i = 0; i < 10000; i++) {
				
				
				p.setHeading(heading += 2*Math.PI/5000);
				
				
				
				if(i%1000 == 0) {p.changeType();}
				out.writeObject(p);
				Thread.sleep(1);
				out.writeObject(new FlagLocations());
				out.reset();
				Thread.sleep(1);
				Message message = (Message) in.readObject();
				switch (message.getID()) {
				case 3:
					Stats stats = (Stats) message;
					fl = stats.getFl();
					ps = stats.getPs();
					System.out.println(fl.getRedX());
					System.out.println(ps.getXvalue());
					break;
				default:
					//System.out.println("NO");
					break;
				}
			}
			
			
			s.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception{
		for (int i = 0; i < 2; i++) {
			new GameClient();
			Thread.sleep(20);
		}
		
	}

}
