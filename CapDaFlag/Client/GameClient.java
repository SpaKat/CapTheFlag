package Client;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import SerialData.FlagLocations;
import SerialData.Home;
import SerialData.Message;
import SerialData.Player;
import SerialData.PlayerStats;
import SerialData.Stats;
import SerialData.noDefend;

public class GameClient extends Thread {
	Player p = new Player();
	public GameClient() {
		this.start();
	}

	public GameClient(int i) {
		this();
		if (i == 1) {
			p.changeType();
		}else {

		}
	}

	private FlagLocations fl;
	private PlayerStats ps;
	private Home home;
	@Override
	public void run() {
		try {
			Socket s = new Socket("127.0.0.1",8008);
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());



			double heading = Math.random()*2*Math.PI;
			out.writeObject(p);
			out.writeObject(new Stats());
			out.reset();

			for (int i = 0; i < 5000; i++) {


				p.setHeading(heading);

				out.writeObject(new Stats());
				out.reset();
				Thread.sleep(1);

				//if(i%50 == 0) {p.changeType();}


				Message message = (Message) in.readObject();
				int id = message.getID();
				if (id == 1) {
					//player
					p = (Player) message;
					//System.out.println(((Player) message).getHeading());
				}
				if (id == 10) {
					noDefend tempP = (noDefend) message;
					//System.out.println(tempP.isDefensive() +"     "+p.isDefensive());
					p.setDefensive(tempP.isDefensive());
				}
				if (id == 3) {

					Stats stats = (Stats) message;
					fl = stats.getFl();
					ps = stats.getPs();
					home = stats.getHomebase();
					if(ps.isBlueteam()) {
						if( Math.abs( fl.getRedX() - ps.getX()  ) < 1 &&  Math.abs( fl.getRedY() - ps.getY()  ) <1  ) {
							double deltax = home.getBlueStartX() - ps.getX();
							double deltay = home.getBlueStartY() - ps.getY();
							heading = Math.atan2(deltay, deltax);
						//	System.out.println(deltax + "______________" + deltay);
							p.setHeading(heading);

						}

						if (! (Math.abs( fl.getRedX() - ps.getX()  ) < 9 &&  Math.abs( fl.getRedY() - ps.getY()  ) <9)){
							double deltax = fl.getRedX() - ps.getX();
							double deltay = fl.getRedY() - ps.getY();
							heading = Math.atan2(deltay, deltax);
							//System.out.println(deltax + "______________" + deltay);
							p.setHeading(heading);

						}
						//System.out.println(heading *180/Math.PI);
					}
					/*else {

						if( Math.abs( fl.getBlueX() - ps.getX()  ) < 1 &&  Math.abs( fl.getBlueY() - ps.getY()  ) <1  ) {
							double deltax = home.getRedStartX() - ps.getX();
							double deltay = home.getRedStartY() - ps.getY();
							heading = Math.atan2(deltay, deltax);
							//	System.out.println(deltax + "______________" + deltay);

						}

						if (! (Math.abs( fl.getBlueX() - ps.getX()  ) < 9 &&  Math.abs( fl.getBlueY() - ps.getY()  ) <9)){
							double deltax = fl.getBlueX() - ps.getX();
							double deltay = fl.getBlueY() - ps.getY();
							heading = Math.atan2(deltay, deltax);
							//System.out.println(deltax + "______________" + deltay);

						}


					}*/
					//System.out.println(fl.getRedX());
					//System.out.println(ps.getX());


					out.writeObject(p);
					out.flush();

				}
			}

			s.close();
		} catch (Exception e) {

		}
	}
	public static void main(String[] args) throws Exception{
		for (int i = 0; i < 1; i++) {
			new GameClient(2);
			new GameClient(2);
			Thread.sleep(20);
		}

	}

}
