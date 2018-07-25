package Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import SerialData.FlagLocations;

public class GameServer extends Thread{

	private ServerSocket serverSocket ;
	private int port = 8008;
	private ArrayList<GameServerClient> serverClients;
	private FlagLocations flaglocations;
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
				if(serverClients.size()<51) {
					synchronized (serverClients) {
						serverClients.add(new GameServerClient(socket,team,flaglocations));
						team = !team;
					}
				}
			}
			serverSocket.close();
		} catch (Exception e) {
			//e.printStackTrace();

		}
		serverClients.forEach(e ->{
			e.interrupt();
		});

	}
	public void setFlaglocations(FlagLocations flaglocations) {
		this.flaglocations = flaglocations;
	}
	public synchronized void clean() {
		for (int i = 0; i < serverClients.size(); ) {
			if (!serverClients.get(i).isAlive()) {
				serverClients.remove(i);
			}else {
				i++;
			}
		}
	}
	public synchronized ArrayList<GameServerClient> getServerClients() {
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
