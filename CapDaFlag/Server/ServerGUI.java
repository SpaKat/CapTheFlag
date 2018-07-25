package Server;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ServerGUI extends Application {
	BorderPane bp;
	GameServer gameServer;
	public ServerGUI() {
		gameServer = new GameServer();
		Pane gameBoard = new Pane();
		gameBoard.setStyle("-fx-background-color: black; -fx-border-color: green ; -fx-border-width: 5"); 
		gameBoard.setPrefSize(400, 400);
		bp = new BorderPane(gameBoard);


		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000/60.0), e ->  {
			gameServer.getServerClients().forEach(client ->{
				if (!client.isAlive()) {
					gameBoard.getChildren().remove(client.getCircle());
				}else {

					if (gameBoard.getChildren().contains(client.getCircle())) {
						try {
							
						client.getCircle().setLayoutX(client.getCircle().getLayoutX() + (Math.cos(client.getHeading() ) ) );
						client.getCircle().setLayoutY(client.getCircle().getLayoutY() + (Math.sin(client.getHeading() ) ) );
				
						}catch (NullPointerException enull) {
							// TODO: handle exception
						}
						
					}else {
						gameBoard.getChildren().add(client.getCircle());
					}
				}

			});
			gameServer.clean();
			//System.out.println(gameServer.getServerClients().size());

		}));
		timeline.play();

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
