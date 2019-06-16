
public class Agent extends GameClient{

	
	public Agent(String ip,boolean blueteam) {
		super(ip,blueteam);
	}
	
	@Override
	public double mycontrols(double heading) {
		/** 												CODE STARTS HERE                   							 **/
		/**
		COMMANDS:
		BLUE:
		
			Get starting home x location:
				homelocation.getBlueStartX();
			Get starting home y location:
				homelocation.getBlueStartY();
			Get Flag x location
				flagLocations.getBlueX();
			Get Flag x location
				flagLocations.getBlueY();
		
		RED:
		
			Get starting home x location:
				homelocation.getRedStartX();
			Get starting home y location:
				homelocation.getRedStartY();
			Get Flag x location
				flagLocations.getRedX();
			Get Flag x location
				flagLocations.getRedY();
			
		PLAYER DATA:
			Get player mode 
				player.isDefensive();
			Change player mode
			 	player.setDefensive(true);  -> defensive mode
			 						false   -> offensive
			Change Heading
				player.setHeading( <<new heading>>  ); new heading is a variable of type double
			Get player x location
				playerStats.getX() 
			Get player y location
				playerStats.getY() 
			get player team
				playerStats.isBlueteam()
			
		GET INTEL FROM ANOTHER PLAYER:
			Get array of all other players: (The array is of type OtherPlayer)
				otherPlayers.getOtherPlayers();		
			OtherPlayer[] op = otherPlayers.getOtherPlayers();
				int index = 0;
			Get other players x location
				op[index].getX();
			Get other players y location
				op[index].getY();
			Get other players mode
				op[index].isDefender();
			Get other players team
				op[index].isBlueteam()
		
		**/
		
		// move if on blue team
		
		player.setDefensive(true);
		
		if(playerStats.isBlueteam()) {
			if( Math.abs( flagLocations.getRedX() - playerStats.getX()  ) < 1 &&  Math.abs( flagLocations.getRedY() - playerStats.getY()  ) <1  ) {
				// go get the Flag
				double deltax = homelocation.getBlueStartX() - playerStats.getX();
				double deltay = homelocation.getBlueStartY() - playerStats.getY();
				heading = Math.atan2(deltay, deltax);
				//	System.out.println(deltax + "______________" + deltay);
				player.setHeading(heading);

			}
			if (! (Math.abs( flagLocations.getRedX() - playerStats.getX()  ) < 9 &&  Math.abs( flagLocations.getRedY() - playerStats.getY()  ) <9)){
				// take the flag home
				double deltax = flagLocations.getRedX() - playerStats.getX();
				double deltay = flagLocations.getRedY() - playerStats.getY();
				heading = Math.atan2(deltay, deltax);
				//System.out.println(deltax + "______________" + deltay);
				player.setHeading(heading);

			}
			//System.out.println(heading *180/Math.PI);
		}else {
			// move if on red team 
			if( Math.abs( flagLocations.getBlueX() - playerStats.getX()  ) < 1 &&  Math.abs( flagLocations.getBlueY() - playerStats.getY()  ) <1  ) {
				double deltax = homelocation.getRedStartX() - playerStats.getX();
				double deltay = homelocation.getRedStartY() - playerStats.getY();
				heading = Math.atan2(deltay, deltax);
				//	System.out.println(deltax + "______________" + deltay);
			}
			if (! (Math.abs( flagLocations.getBlueX() - playerStats.getX()  ) < 9 &&  Math.abs( flagLocations.getBlueY() - playerStats.getY()  ) <9)){
				double deltax = flagLocations.getBlueX() - playerStats.getX();
				double deltay = flagLocations.getBlueY() - playerStats.getY();
				heading = Math.atan2(deltay, deltax);
				//System.out.println(deltax + "______________" + deltay);
			}
		}
		/** 												CODE END HERE                   							 **/
		return heading;
	}
	public static void main(String[] args) throws Exception{
		for(int i = 0; i< 20; i ++)
		{
			new Agent("127.0.0.1",true); // change ip , the boolean value is blue team = true  -   red team = false
		}
	}
}
