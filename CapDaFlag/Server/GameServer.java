package Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import SerialData.FlagLocations;
import SerialData.Home;

public class GameServer extends Thread{

	private ServerSocket serverSocket ;
	private int port = 8008;
	private ArrayList<GameServerClient> serverClients;
	private FlagLocations flaglocations;
	private Home homebase;
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
						serverClients.add(new GameServerClient(socket,team,flaglocations,homebase));
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
	

	public void die() {
		try {

			serverSocket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setHomebase(Home homebase) {
		this.homebase = homebase;
	}
	public Home getHomebase() {
		return homebase;
	}

	public void killcilents() {
		for (int i = 0; i < serverClients.size(); i++) {
			serverClients.get(i).kill();
		}
	}

	public void killcilent(GameServerClient attacker) {
		 serverClients.get(serverClients.indexOf(attacker)).kill();
		 
	}
}
