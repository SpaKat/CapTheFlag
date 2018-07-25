package Client;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import SerialData.Player;

public class GameClient extends Thread {

	public GameClient() {
		this.start();
	}
	
	@Override
	public void run() {
		try {
			Socket s = new Socket("192.168.1.81",8008);
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			Player p = new Player();
			for (int i = 0; i < 50; i++) {
				p.setHeading(Math.random()*2*Math.PI);
				out.writeObject(p);
				Thread.sleep(1000);
			}
			
			
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		for (int i = 0; i < 50; i++) {
			new GameClient();
		}
		
	}

}
