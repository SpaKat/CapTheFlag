package Server;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import SerialData.Message;
import SerialData.Player;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameServerClient extends Thread{

	private Socket socket;
	private Player player;
	private Circle circle = new Circle(10,Color.WHITE);
	public GameServerClient(Socket socket, boolean team) {
		this.socket = socket;
		if (team) {
			circle.setFill(Color.BLUE);
		} else {
			circle.setFill(Color.RED);
		}
		this.start();
	}

	@Override
	public void run() {
		try {


			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

			try {
				while (!this.isInterrupted()) {
					Message message = (Message) in.readObject();
					switch (message.getID()) {
					case 1:
						//player
						player = (Player) message;
						System.out.println("YES!!!!!!");
						break;
					default:
						//System.out.println("NO");
						break;
					}
					Thread.sleep(1);
				}
				
				
			}catch (EOFException e) {
				System.err.println("OUTo");
			}
		}catch (Exception e) {
			e.printStackTrace();		
		}
		System.out.println("Died");
	}
	public Circle getCircle() {
		return circle;
	}

	public double getHeading() {
		// TODO Auto-generated method stub
		return player.getHeading();
	}
}
