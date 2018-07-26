

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ServerGUI extends Application {
	private BorderPane bp;
	private GameServer gameServer;
	private Pane gameBoard = new Pane();
	private Timeline movetimeline = new Timeline();
	private ToggledCircle Blueflag = new ToggledCircle(5);
	private ToggledCircle RedFlag = new ToggledCircle(5);
	private double BluestartX;
	private double BluestartY;
	private double RedstartX;
	private double RedstartY;
	private Home homebase;
	private boolean start = false;
	private OtherPlayers otherPlayers;
	public ServerGUI() {
		gameServer = new GameServer();

		gameBoard.setStyle("-fx-background-color: black; -fx-border-color: green ; -fx-border-width: 5"); 
		gameBoard.setPrefSize(400, 400);
		bp = new BorderPane(gameBoard);
		bp.setBottom(controls());

		Blueflag.setStyle("-fx-fill: blue;-fx-stroke: gold;-fx-stroke-width: 2;");
		RedFlag.setStyle("-fx-fill: red ;-fx-stroke: gold;-fx-stroke-width: 2;");
		gameServer.setFlaglocations(new FlagLocations(RedFlag.getLayoutX(), RedFlag.getLayoutY(), Blueflag.getLayoutX(), Blueflag.getLayoutY()));
		RedstartX = 55;
		RedstartY = 55;
		BluestartX = gameBoard.getWidth()-55;
		BluestartY = gameBoard.getHeight()-55;
		homebase = new Home(RedstartX, RedstartY, BluestartX, BluestartY);
		gameServer.setHomebase(homebase);
		otherPlayers = new OtherPlayers(null);
		gameServer.setOtherPlayers(otherPlayers);
		movetimeline.setCycleCount(Timeline.INDEFINITE);
		movetimeline.getKeyFrames().add(new KeyFrame(Duration.millis(1), e ->  {
			try {
				SpawnClients();
				MoveClients();
				FlagControl();
				UpdateStats();
				DefenderRadiusCheck();
				DefenderKill();
				CheckforWin();
				checkforDeadcicles();
			}catch (ArrayIndexOutOfBoundsException d) {
				System.err.println("FOund");
			}

			//System.out.println(gameServer.getServerClients().size());
		}));
		movetimeline.play();

	}

	private void DefenderRadiusCheck() {
		for (int i = 0; i < gameServer.getServerClients().size(); i++) {
			GameServerClient defender = gameServer.getServerClients().get(i); 

			double radX = Math.abs( RedFlag.getLayoutX() - Blueflag.getLayoutX())/5;
			double radY = Math.abs( RedFlag.getLayoutY() - Blueflag.getLayoutY())/5;

			if(defender.isDefender()) {
				if(defender.isTeamBlue()) {
					double x = Math.abs( defender.getCircle().getLayoutX() - Blueflag.getLayoutX());
					double y = Math.abs( defender.getCircle().getLayoutY() - Blueflag.getLayoutY());
					//System.out.println(x>radX && y>radY);
					if (x>radX && y>radY) {
						defender.noDefence();
					}else {
						defender.free();
					}
				}else {
					double x = Math.abs( defender.getCircle().getLayoutX() - RedFlag.getLayoutX());
					double y = Math.abs( defender.getCircle().getLayoutY() - RedFlag.getLayoutY());
					//System.out.println(x>radX && y>radY);
					if (x>radX && y>radY) {
						defender.noDefence();
					}else {
						defender.free();
					}
				}
			}
		}
	}

	private void checkforDeadcicles() {

		for (int i = 0; i < gameBoard.getChildren().size(); i++) {
			Circle piece  = (Circle) gameBoard.getChildren().get(i);
			boolean killpiece = true;
			if (piece.equals(Blueflag)) {
				killpiece = false;
			}
			if (piece.equals(RedFlag)) {
				killpiece = false;
			}
			for (int j = 0; j < gameServer.getServerClients().size(); j++) {
				Circle cleintCircle = gameServer.getServerClients().get(j).getCircle(); 
				if (piece.equals(cleintCircle)) {
					killpiece = false;
				}
			}
			if (killpiece) {
				gameBoard.getChildren().remove(piece);
			}
			//System.out.println(gameServer.getServerClients().size());
		}



	}

	private void DefenderKill() {
		for (int i = 0; i < gameServer.getServerClients().size(); i++) {
			GameServerClient defender = gameServer.getServerClients().get(i); 
			if (defender.isDefender()) {
				for (int j = 0; j < gameServer.getServerClients().size(); j++) {
					GameServerClient attacker = gameServer.getServerClients().get(j); 
					if(!attacker.isDefender()) {
						double deltax = Math.abs( defender.getCircle().getLayoutX() - attacker.getCircle().getLayoutX());
						double deltay = Math.abs( defender.getCircle().getLayoutY() - attacker.getCircle().getLayoutY());
						double rad = defender.getCircle().getRadius() + attacker.getCircle().getRadius();
						if ( deltax < rad && deltay <rad) {
							gameServer.killcilent(attacker);
						}
					}
				}

			}
		}
	}

	private void CheckforWin() {
		if(start ) {
			double deltredx = Math.abs(RedFlag.getLayoutX() - BluestartX);
			double deltredy = Math.abs(RedFlag.getLayoutY() - BluestartY);

			double deltbluex = Math.abs(Blueflag.getLayoutX() - RedstartX);
			double deltbluey = Math.abs(Blueflag.getLayoutY() - RedstartY);

			if (deltredx < 1 && deltredy < 1) {
				//blue wins
				WinpopUP("Blue Wins");
				gameServer.killcilents();
				start = false;
				movetimeline.stop();

			}

			if (deltbluex < 1 && deltbluey < 1) {
				//red wins
				WinpopUP("Red Wins");
				gameServer.killcilents();
				start = false;
				movetimeline.stop();
			}
		}
	}

	private void WinpopUP(String string) {

		Text text = new Text(string);
		text.setFont(new Font("old english text", 60));
		Stage stage = new Stage();
		stage.setScene(new Scene(new BorderPane(text)));
		stage.show();
	}

	private void UpdateStats() {

		FlagLocations fl = new FlagLocations(RedFlag.getLayoutX(),RedFlag.getLayoutY(),Blueflag.getLayoutX(),Blueflag.getLayoutY());
		homebase = new Home(RedstartX, RedstartY, BluestartX, BluestartY);

		for (int i = 0; i < gameServer.getServerClients().size(); i++) {
			GameServerClient client = gameServer.getServerClients().get(i); 
			client.setFlaglocations(fl);
			client.setHomebase(homebase);
			OtherPlayer[] op = new OtherPlayer[gameServer.getServerClients().size()-1];
			int counter = 0;
			for (int j = 0; j < gameServer.getServerClients().size(); j++) {
				GameServerClient otherclient = gameServer.getServerClients().get(j);
				if(!client.equals (otherclient)){
					op[counter++] = new OtherPlayer(otherclient.getCircle().getLayoutX(), otherclient.getCircle().getLayoutY(), otherclient.isTeamBlue(), otherclient.isDefender());
				}
			}
			otherPlayers = new OtherPlayers(op);
			client.setOtherPlayers(otherPlayers);
		}
	}

	private void SpawnClients() {
		try {
			for (int i = 0; i < gameServer.getServerClients().size(); i++) {
				GameServerClient client = gameServer.getServerClients().get(i); 
				if (!client.isAlive()) {
					gameBoard.getChildren().remove(client.getCircle());
				}else {
					if (!gameBoard.getChildren().contains(client.getCircle())) {
						spawn(client);
					}
				}
			}
			gameServer.clean();
		}catch (ArrayIndexOutOfBoundsException d) {
			System.err.println("FOund");
		}
	}

	private void MoveClients() {
		for (int i = 0; i < gameServer.getServerClients().size(); i++) {
			GameServerClient client = gameServer.getServerClients().get(i); 
			if (client.isAlive()) {
				if (gameBoard.getChildren().contains(client.getCircle())) {
					try {
						moveVaildate(client);
					}catch (NullPointerException enull) {

					}
				}
			}
		}
	}

	private void FlagControl() {
		RedFlag.toFront();
		Blueflag.toFront();
		boolean nevertakenred = true;
		boolean nevertakenblue = true;
		for (int i = 0; i < gameServer.getServerClients().size(); i++) {
			GameServerClient client = gameServer.getServerClients().get(i); 
			double delx = client.getCircle().getLayoutX();
			double dely = client.getCircle().getLayoutY();
			try {
				if (client.isTeamBlue()) {
					if (!client.isDefender()) {
						delx -= RedFlag.getLayoutX();
						dely -= RedFlag.getLayoutY();
						if (Math.abs(delx)  < 1 && Math.abs(dely) < 1) {
							if (!RedFlag.isTaken() && nevertakenred) {
								RedFlag.layoutXProperty().bind(client.getCircle().layoutXProperty());
								RedFlag.layoutYProperty().bind(client.getCircle().layoutYProperty());
								RedFlag.setTaken(true);
								nevertakenred = false;
							}

						}
					}
				}else {
					if (!client.isDefender()) {
						delx -= Blueflag.getLayoutX();
						dely -= Blueflag.getLayoutY();
						if (Math.abs(delx)  < 1 && Math.abs(dely) < 1) {
							if (!Blueflag.isTaken() && nevertakenblue) {
								Blueflag.layoutXProperty().bind(client.getCircle().layoutXProperty());
								Blueflag.layoutYProperty().bind(client.getCircle().layoutYProperty());
								Blueflag.setTaken(true);
								nevertakenred = false;
							}
						}
					}

				}
			}catch(Exception e ) {

			}
			if (nevertakenred) {
				RedFlag.setTaken(false);
				Blueflag.setTaken(false);
			}
		}
	}

	private Pane controls() {
		HBox timeCon = timeCon();

		HBox flagCon = flagcon();



		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(timeCon,flagCon);
		return vbox;
	}

	private HBox flagcon() {
		HBox flagCon = new HBox();
		flagCon.setAlignment(Pos.CENTER);
		Button spawn = new Button("Spawn Flags");
		spawn.setOnAction(e ->{
			try {
				gameBoard.getChildren().addAll(RedFlag,Blueflag);
			}catch (Exception n) {
			}
			RedFlag.layoutXProperty().unbind();
			RedFlag.layoutYProperty().unbind();
			RedstartX = 55;
			RedstartY = 55;

			RedFlag.relocate(RedstartX, RedstartY);

			start =true;

			Blueflag.layoutXProperty().unbind();
			Blueflag.layoutYProperty().unbind();
			BluestartX = gameBoard.getWidth()-55;
			BluestartY = gameBoard.getHeight()-55;
			Blueflag.relocate(BluestartX, BluestartY);

			gameServer.setFlaglocations(new FlagLocations(RedFlag.getLayoutX(), RedFlag.getLayoutY(), Blueflag.getLayoutX(), Blueflag.getLayoutY()));
		});
		flagCon.getChildren().addAll(spawn);
		return flagCon;
	}

	private HBox timeCon() {
		HBox timeCon = new HBox();
		timeCon.setAlignment(Pos.CENTER);
		Button play = new Button("Play");
		play.setOnAction(e ->{
			movetimeline.playFromStart();
		});
		Button stop = new Button("Stop");
		stop.setOnAction(e ->{
			movetimeline.stop();
		});
		timeCon.getChildren().addAll(play,stop);
		return timeCon;
	}

	private void spawn(GameServerClient client) {
		gameBoard.getChildren().add(client.getCircle());
		Random rn = new Random();
		int rad = 100;
		if(client.isTeamBlue()) {

			double bluex = BluestartX+rn.nextInt(rad)-rad/2;
			double bluey = BluestartY +rn.nextInt(rad)-rad/2;

			while (!( bluex> 5+client.getCircle().getRadius() 
					&& bluex< gameBoard.getWidth()- 10-client.getCircle().getRadius() 
					&& bluey> 5+client.getCircle().getRadius() 
					&& bluey< gameBoard.getHeight()- 10 -client.getCircle().getRadius() )){

				bluex = BluestartX+rn.nextInt(rad)-rad/2;
				bluey = BluestartY +rn.nextInt(rad)-rad/2;
			} 

			client.getCircle().relocate(bluex, bluey);
		}else {
			double redx = RedstartX+rn.nextInt(rad)-rad/2;
			double redy = RedstartY +rn.nextInt(rad)-rad/2;
			while (!( redx> 5+client.getCircle().getRadius() 
					&& redx< gameBoard.getWidth()- 5-client.getCircle().getRadius() 
					&& redy> 5+client.getCircle().getRadius() 
					&& redy< gameBoard.getHeight()- 5 -client.getCircle().getRadius() )){

				redx = RedstartX+rn.nextInt(rad)-rad/2;
				redy = RedstartY +rn.nextInt(rad)-rad/2;
			} 
			client.getCircle().relocate(redx, redy);
		}

	}

	private void moveVaildate(GameServerClient client) {
		double x = client.getCircle().getLayoutX() + (Math.cos(client.getHeading() )  /(1000.0/60));
		double y = client.getCircle().getLayoutY() + (Math.sin(client.getHeading() ) /(1000.0/60));

		if (x> 5+client.getCircle().getRadius() && x< gameBoard.getWidth()- 5-client.getCircle().getRadius() ) {
			client.getCircle().setLayoutX(x);
		}
		if (y> 5+client.getCircle().getRadius() && y< gameBoard.getHeight()- 5 -client.getCircle().getRadius() ) {
			client.getCircle().setLayoutY(y );
		}
	}

	@Override
	public void start(Stage stage)  {
		stage.setTitle("Server");
		Scene scene = new Scene(bp);
		stage.setScene(scene);
		stage.setOnCloseRequest(e ->{
			gameServer.interrupt();
			gameServer.die();
		});
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
