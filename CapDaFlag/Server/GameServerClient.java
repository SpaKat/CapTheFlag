package Server;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import SerialData.FlagLocations;
import SerialData.Message;
import SerialData.Player;
import SerialData.PlayerStats;
import SerialData.Stats;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameServerClient extends Thread{

	private Socket socket;
	private Player player;
	private Circle circle = new Circle(10,Color.WHITE);
	private boolean teamBlue;
	private FlagLocations flaglocations;
	private PlayerStats playerStats;
	public GameServerClient(Socket socket, boolean team, FlagLocations flaglocations) {
		this.setName("GameServerClient");
		this.socket = socket;
		this.teamBlue = team;
		this.flaglocations = flaglocations;
		circle.setOnMouseClicked(e ->{
			System.out.println(this.getName());
		});
		this.start();
	}

	private void colorCircle(boolean team) {
		String color;
		String border;
		if (team) {
			color = "-fx-fill: blue;";
		} else {
			color = "-fx-fill: red;";
		}
		if (player.isDefensive()) {
			border = "-fx-stroke: white;"
					+ "-fx-stroke-width: 2;";
		}else {
			border = "-fx-stroke: black;"
					+ "-fx-stroke-width: 2;";
		}
		circle.setStyle(color + border);
	}

	@Override
	public void run() {
		try {


			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			try {
				while (!this.isInterrupted()) {
					playerStats = new PlayerStats( circle.getLayoutX(), circle.getLayoutY(), teamBlue);
					Message message = (Message) in.readObject();
					switch (message.getID()) {
					case 1:
						//player
						player = (Player) message;
						colorCircle(teamBlue);
						//System.out.println(((Player) message).getHeading());
						break;
					case 3:
						out.writeObject(new Stats(flaglocations,playerStats));
					//	System.out.println("SENT");
						out.reset();
						break;
					default:
						//System.out.println("NO");
						break;
					}
					Thread.sleep(1);
				}


			}catch (EOFException e) {
				//System.err.println("OUTo");
			}
		}catch (Exception e) {
			//e.printStackTrace();		
		}
		//System.out.println("Died");
	}
	public Circle getCircle() {
		return circle;
	}
	public boolean isTeamBlue() {
		return teamBlue;
	}
	public double getHeading() {
		return player.getHeading();
	}

	public boolean isDefender() {
		// TODO Auto-generated method stub
		return player.isDefensive();
	}
}
