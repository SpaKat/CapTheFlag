package Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class GameServer extends Thread{

	private ServerSocket serverSocket ;
	private int port = 8008;
	private ArrayList<GameServerClient> serverClients;
	public GameServer() {
		serverClients = new ArrayList<>();
		this.start();
	}

	@Override
	public void run() {
		try {
			serverSocket = new  ServerSocket(port);
			boolean team = true;
			while (!this.isInterrupted()) {
				Socket socket = serverSocket.accept();
				serverClients.add(new GameServerClient(socket,team));
				team = !team;
			}
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		serverClients.forEach(e ->{
			e.interrupt();
		});
		
	}
	public void clean() {
		for (int i = 0; i < serverClients.size(); i++) {
			if (!serverClients.get(0).isAlive()) {
				serverClients.remove(0);
			}
		}
	}
	public ArrayList<GameServerClient> getServerClients() {
		return serverClients;
	}
	public static void main(String[] args) {
		new GameServer();
	}

	public void die() {
		try {
			
			serverSocket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
