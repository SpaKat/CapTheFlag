package Server;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import SerialData.FlagLocations;
import SerialData.Home;
import SerialData.Message;
import SerialData.OtherPlayers;
import SerialData.Player;
import SerialData.PlayerStats;
import SerialData.Stats;
import SerialData.noDefend;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameServerClient extends Thread{

	private Socket socket;
	private Player player =new Player();
	private Circle circle = new Circle(10,Color.WHITE);
	private boolean teamBlue;
	private FlagLocations flaglocations;
	private PlayerStats playerStats;
	private Home homebase;
	private int sendCommand = 0;
	private OtherPlayers otherPlayers;
	public GameServerClient(Socket socket, boolean team, FlagLocations flaglocations,Home homebase,OtherPlayers otherPlayers) {
		this.setName("GameServerClient");
		this.socket = socket;
		this.teamBlue = team;
		this.flaglocations = flaglocations;
		this.homebase = homebase;
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
					int id = message.getID();


					if(id == 1) {
						//player
						player = (Player) message;
						if (sendCommand == 10) {
							player.setDefensive(false);
						}
						colorCircle(teamBlue);

						//System.out.println(((Player) message).getHeading());
					}
					if(id == 3) {
						out.writeObject(new Stats(flaglocations,playerStats,homebase,otherPlayers));
						//	System.out.println("SENT");
						out.flush();
						Thread.sleep(1);
					}


					switch (sendCommand) {
					case 10:
						if(player.isDefensive()) {
							player.setDefensive(false);
							out.writeObject(new noDefend());
							out.reset();
							Thread.sleep(1);
						}
						//System.out.println("SEnt");
						break;

					default:
						break;
					}
					Thread.sleep(1);
					//System.out.println(player.isDefensive());
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
	public void setFlaglocations(FlagLocations flaglocations) {
		this.flaglocations = flaglocations;
	}
	public void setHomebase(Home homebase) {
		this.homebase = homebase;
	}

	public void kill() {
		try {

			socket.close();
			this.interrupt();
		} catch (IOException e) {
			System.err.println("Die");
		}
	}

	public void noDefence() {
		sendCommand = 10;
	}
	public void free() {
		sendCommand = 0;
	}
	public void setOtherPlayers(OtherPlayers otherPlayers) {
		this.otherPlayers = otherPlayers;
	}
}
