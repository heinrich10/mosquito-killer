package spaceinvaders;

//import java.awt.Color;
//import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

public class Game {
	boolean leftPressed = false;
	boolean rightPressed = false;
	boolean firePressed = false;
	boolean waitingForKeyPress = false;
	
	boolean gameRunning = false;
	boolean logicRequiredThisLoop = false;
	
	String message;
	
	int alienCount = 0;
	
	private ArrayList entities = new ArrayList();
	private ArrayList removeList = new ArrayList();
	Entity ship = null;
	
	private long lastFire = 0;
	private long firingInterval = 500;
	private double moveSpeed = 300;
	
	private long lastFpsTime = 0;
	private long fps = 0;
	
	
	public void startGame() {
		// clear out any existing entities and intialise a new set
		entities.clear();
		initEntities();
		
		// blank out any keyboard settings we might currently have
		leftPressed = false;
		rightPressed = false;
		firePressed = false;
	}
	
	private void initEntities() {
		// create the player ship and place it roughly in the center of the screen
		ship = new ShipEntity(this,"sprites/ship.gif",370,550);
		entities.add(ship);
		alienCount = 0;
		
	}
	
	public void updateLogic() {
		logicRequiredThisLoop = true;
	}
	
	public void removeEntity(Entity entity) {
		removeList.add(entity);
	}
	
	public void notifyDeath() {
		message = "Oh no! They got you, try again?";
		waitingForKeyPress = true;
	}
	
	public void notifyWin() {
		message = "Well done! You Win!";
		waitingForKeyPress = true;
	}
	
	public void notifyAlienKilled() {
		// reduce the alient count, if there are none left, the player has won!
		alienCount--;
		
		if (alienCount == 0) {
			notifyWin();
		}
		
		// if there are still some aliens left then they all need to get faster, so
		// speed up all the existing aliens
		for (int i=0;i<entities.size();i++) {
			Entity entity = (Entity) entities.get(i);
			
			if (entity instanceof AlienEntity) {
				// speed up by 2%
				entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);
			}
		}
	}
	
	public void tryToFire() {
		// check that we have waiting long enough to fire
		if (System.currentTimeMillis() - lastFire < firingInterval) {
			return;
		}
		
		// if we waited long enough, create the shot entity, and record the time.
		lastFire = System.currentTimeMillis();
		ShotEntity shot = new ShotEntity(this,"sprites/shot.gif",ship.getX()+10,ship.getY()-30);
		
		entities.add(shot);
	}
	
	
	public void gameLoop() {
		long lastLoopTime = SystemTimer.getTime();
	
		int count1 = 0;
		Entity stationary = null;
		
		stationary = new AlienEntity(this, 150, 250);
		entities.add(stationary);
		stationary = new AlienEntity(this, 193, 250);
		entities.add(stationary);
		stationary = new AlienEntity(this, 150, 279);
		entities.add(stationary);
		stationary = new AlienEntity(this, 193, 279);
		entities.add(stationary);
		
		

		stationary = new AlienEntity(this, 570, 130);
		entities.add(stationary);
		stationary = new AlienEntity(this, 613, 130);
		entities.add(stationary);
		stationary = new AlienEntity(this, 570, 159);
		entities.add(stationary);
		stationary = new AlienEntity(this, 613, 159);
		entities.add(stationary);
	
		// keep looping round til the game ends
		while (gameRunning) {
			// work out how long its been since the last update, this
			// will be used to calculate how far the entities should
			// move this loop
			//temp declare
			Entity alien = null;
			
			long delta = SystemTimer.getTime() - lastLoopTime;
			lastLoopTime = SystemTimer.getTime();

			//count
			
			//System.out.println(count1++);
			//System.out.println(lastFpsTime);
			
			// update the frame counter
			lastFpsTime += delta;
			fps++;
			
			//test
			
			
			// update our FPS counter if a second has passed since
			// we last recorded
			if (lastFpsTime >= 1000) {
				//container.setTitle(windowTitle+" (FPS: "+fps+")");
				lastFpsTime = 0;
				fps = 0;
				
				//random generation of alien
				Random num = new Random();
				num.nextInt(761);
				
				if(alienCount < 20){
					alien = new AlienEntity(this, num.nextInt(761), num.nextInt(300));
					entities.add(alien);
					alienCount++;
				}
							
			}
			
			// Get hold of a graphics context for the accelerated 
			// surface and blank it out
			//Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			//g.setColor(Color.black);
			//g.fillRect(0,0,800,800);
			
			// cycle round asking each entity to move itself
			if (!waitingForKeyPress) {
				for (int i=0;i<entities.size();i++) {
					Entity entity = (Entity) entities.get(i);
					
					entity.move(delta);
				}
			}
			
			// cycle round drawing all the entities we have in the game
			//for (int i=0;i<entities.size();i++) {
			//	Entity entity = (Entity) entities.get(i);
				
			//	entity.draw(g);
			//}
			
			// brute force collisions, compare every entity against
			// every other entity. If any of them collide notify 
			// both entities that the collision has occured
			for (int p=0;p<entities.size();p++) {
				for (int s=p+1;s<entities.size();s++) {
					Entity me = (Entity) entities.get(p);
					Entity him = (Entity) entities.get(s);
					
					if (me.collidesWith(him)) {
						me.collidedWith(him);
						him.collidedWith(me);
					}
				}
			}
			
			// remove any entity that has been marked for clear up
			entities.removeAll(removeList);
			removeList.clear();

			// if a game event has indicated that game logic should
			// be resolved, cycle round every entity requesting that
			// their personal logic should be considered.
			if (logicRequiredThisLoop) {
				for (int i=0;i<entities.size();i++) {
					Entity entity = (Entity) entities.get(i);
					entity.doLogic();
				}
				
				logicRequiredThisLoop = false;
			}
			
			// if we're waiting for an "any key" press then draw the 
			// current message 
			if (waitingForKeyPress) {
				//g.setColor(Color.white);
				//g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,250);
				//g.drawString("Press any key",(800-g.getFontMetrics().stringWidth("Press any key"))/2,300);
			}
			
			// finally, we've completed drawing so clear up the graphics
			// and flip the buffer over
			//g.dispose();
			//strategy.show();
			
			// resolve the movement of the ship. First assume the ship 
			// isn't moving. If either cursor key is pressed then
			// update the movement appropraitely
			
			
			
			
			for (int i=0;i<entities.size();i++) {
				Entity entity = (Entity) entities.get(i);
				
				if (entity instanceof AlienEntity) {
					entity.setVerticalMovement(50);
					entity.setHorizontalMovement(0);
					//entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);
				}
			}
			
			ship.setHorizontalMovement(0);
			
			if ((leftPressed) && (!rightPressed)) {
				ship.setHorizontalMovement(-moveSpeed);
				

				
			} else if ((rightPressed) && (!leftPressed)) {
				ship.setHorizontalMovement(moveSpeed);
				
			}
			
			// if we're pressing fire, attempt to fire
			if (firePressed) {
				tryToFire();
			}
			
			// we want each frame to take 10 milliseconds, to do this
			// we've recorded when we started the frame. We add 10 milliseconds
			// to this and then factor in the current time to give 
			// us our final value to wait for
			SystemTimer.sleep(lastLoopTime+10-SystemTimer.getTime());
		}
	}
}
