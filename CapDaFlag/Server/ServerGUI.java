package Server;

import java.util.Random;

import SerialData.FlagLocations;
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
import javafx.stage.Stage;
import javafx.util.Duration;

public class ServerGUI extends Application {
	private BorderPane bp;
	private GameServer gameServer;
	private Pane gameBoard = new Pane();
	private Timeline movetimeline = new Timeline();
	private Circle Blueflag = new Circle(5);
	private Circle RedFlag = new Circle(5);
	public ServerGUI() {
		gameServer = new GameServer();

		gameBoard.setStyle("-fx-background-color: black; -fx-border-color: green ; -fx-border-width: 5"); 
		gameBoard.setPrefSize(400, 400);
		bp = new BorderPane(gameBoard);
		bp.setBottom(controls());

		Blueflag.setStyle("-fx-fill: blue;-fx-stroke: gold;-fx-stroke-width: 4;");
		RedFlag.setStyle("-fx-fill: red ;-fx-stroke: gold;-fx-stroke-width: 4;");
		gameServer.setFlaglocations(new FlagLocations(RedFlag.getLayoutX(), RedFlag.getLayoutY(), Blueflag.getLayoutX(), Blueflag.getLayoutY()));
		Timeline spawntimeline= new Timeline();
		spawntimeline.setCycleCount(Timeline.INDEFINITE);
		spawntimeline.getKeyFrames().add(new KeyFrame(Duration.millis(1), e ->  {
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
		}));
		spawntimeline.play();
		movetimeline.setCycleCount(Timeline.INDEFINITE);
		movetimeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000/60.0), e ->  {
			try {
				for (int i = 0; i < gameServer.getServerClients().size(); i++) {
					GameServerClient client = gameServer.getServerClients().get(i); 
					if (client.isAlive()) {
						if (gameBoard.getChildren().contains(client.getCircle())) {
							try {

								moveVaildate(client);

							}catch (NullPointerException enull) {
								// TODO: handle exception
							}
						}
					}
				}
			}catch (ArrayIndexOutOfBoundsException d) {
				System.err.println("FOund");
			}

			//System.out.println(gameServer.getServerClients().size());
		}));
		movetimeline.play();

	}

	private Pane controls() {
		HBox timeCon = timeCon();

		HBox flagCon = new HBox();
		flagCon.setAlignment(Pos.CENTER);
		Button spawn = new Button("Spawn Flags");
		spawn.setOnAction(e ->{
			try {
				gameBoard.getChildren().addAll(RedFlag,Blueflag);
			}catch (Exception n) {
			}
			RedFlag.relocate(55, 55);
			Blueflag.relocate(gameBoard.getWidth()-55, gameBoard.getHeight()-55);
			gameServer.setFlaglocations(new FlagLocations(RedFlag.getLayoutX(), RedFlag.getLayoutY(), Blueflag.getLayoutX(), Blueflag.getLayoutY()));
		});
		flagCon.getChildren().addAll(spawn);



		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(timeCon,flagCon);
		return vbox;
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
		if(client.isTeamBlue()) {
			client.getCircle().relocate(Blueflag.getLayoutX()+rn.nextInt(80)-40, Blueflag.getLayoutY() +rn.nextInt(80)-40);
		}else {
			client.getCircle().relocate(RedFlag.getLayoutX()+rn.nextInt(80)-40, RedFlag.getLayoutY() +rn.nextInt(80)-40);

		}

	}

	private void moveVaildate(GameServerClient client) {
		double x = client.getCircle().getLayoutX() + (Math.cos(client.getHeading() ) );
		double y = client.getCircle().getLayoutY() + (Math.sin(client.getHeading() ) );

		if (x> 5+client.getCircle().getRadius() && x< gameBoard.getWidth()- 5-client.getCircle().getRadius() ) {
			client.getCircle().setLayoutX(x);
		}
		if (y> 5+client.getCircle().getRadius() && y< gameBoard.getHeight()- 5 -client.getCircle().getRadius() ) {
			client.getCircle().setLayoutY(y );
		}
		if (!client.isDefender()) { //TODO
			if (client.isTeamBlue()) {
				if (
						(client.getCircle().getLayoutX() + client.getCircle().getRadius() - RedFlag.getLayoutX() < 0 ||
								client.getCircle().getLayoutX() + client.getCircle().getRadius() - RedFlag.getLayoutX() < 0 ) && 
						(  client.getCircle().getLayoutY() + client.getCircle().getRadius() - RedFlag.getLayoutY() < 0    ||
								client.getCircle().getLayoutY() + client.getCircle().getRadius() - RedFlag.getLayoutY() < 0  )
						) {
					RedFlag.layoutXProperty().bind(client.getCircle().layoutXProperty());
					RedFlag.layoutYProperty().bind(client.getCircle().layoutYProperty());
				}
			}else {

			}
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
